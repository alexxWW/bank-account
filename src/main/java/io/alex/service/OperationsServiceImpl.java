package io.alex.service;

import io.alex.exception.AmountNegativeValueException;
import io.alex.exception.InsufficientAmountException;
import io.alex.model.Operation;
import io.alex.operationEnum.OperationType;
import io.alex.repository.OperationsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;

public class OperationsServiceImpl implements OperationService {

    private final OperationsRepository operationsRepository;
    private final Clock clock;

    public OperationsServiceImpl(OperationsRepository operationsRepository, Clock clock) {
        this.operationsRepository = operationsRepository;
        this.clock = clock;
    }

    public OperationsServiceImpl(OperationsRepository operationsRepository) {
        this.operationsRepository = operationsRepository;
        this.clock = Clock.systemDefaultZone();
    }

    @Override
    public Operation deposit(BigDecimal depositedAmount) throws AmountNegativeValueException {

        if (depositedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountNegativeValueException("Deposited amount can't be negative");
        }

        BigDecimal balance = getAccountBalance();
        BigDecimal amountRounded = depositedAmount.setScale(2, RoundingMode.HALF_EVEN);

        return createOperation(OperationType.DEPOSIT, amountRounded, balance.add(amountRounded));
    }

    @Override
    public Operation withdraw(BigDecimal amountRequested) {
        return null;
    }

    @Override
    public void displayAccountStatement() {

    }

    private Operation createOperation(OperationType operationType, BigDecimal amount, BigDecimal balance) {

        Operation operationToSave = Operation.builder()
                .date(LocalDateTime.now(clock))
                .operationType(operationType)
                .amount(amount)
                .balance(balance)
                .build();

        return operationsRepository.register(operationToSave);
    }

    private BigDecimal getAccountBalance() {
        return operationsRepository.findOperationByOrderByDate().map(Operation::getBalance).orElse(BigDecimal.ZERO);
    }
}

