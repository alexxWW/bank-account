package io.alex.service;

import io.alex.exception.AmountNegativeValueException;
import io.alex.exception.InsufficientAmountException;
import io.alex.model.Operation;
import io.alex.operationEnum.OperationType;
import io.alex.printer.OperationsPrinter;
import io.alex.repository.OperationsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public class OperationsServiceImpl implements OperationService {

    private final OperationsRepository operationsRepository;

    private final OperationsPrinter operationsPrinter;

    private final Clock clock;

    public OperationsServiceImpl(OperationsRepository operationsRepository, Clock clock, OperationsPrinter operationsPrinter) {
        this.operationsRepository = operationsRepository;
        this.clock = clock;
        this.operationsPrinter = operationsPrinter;
    }

    @Override
    public Operation withdraw(BigDecimal amountRequested) throws AmountNegativeValueException, InsufficientAmountException {
        if (amountRequested.compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountNegativeValueException("Amount requested can't be negative");
        }
        BigDecimal accountBalance = getAccountBalance();
        BigDecimal amountRounded = amountRequested.setScale(2, RoundingMode.HALF_EVEN);

        if (amountRequested.compareTo(accountBalance) > 0) {
            throw new InsufficientAmountException("Your account balance does not allow the withdrawal operation");
        }

        return createOperation(OperationType.WITHDRAW, amountRounded, accountBalance.subtract(amountRounded));
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

    private Operation createOperation(OperationType operationType, BigDecimal amount, BigDecimal balance) {

        Operation operationToSave = Operation.builder()
                .date(LocalDateTime.now(clock))
                .operationType(operationType)
                .amount(amount)
                .balance(balance)
                .build();

        return operationsRepository.register(operationToSave);
    }

    @Override
    public void displayAccountStatement() {
        List<Operation> operationHistory = operationsRepository.getAll();
        operationsPrinter.print(operationHistory);

    }

    private BigDecimal getAccountBalance() {
        return operationsRepository.findOperationByOrderByDate().map(Operation::getBalance).orElse(BigDecimal.ZERO);
    }
}

