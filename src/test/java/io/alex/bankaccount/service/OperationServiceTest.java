package io.alex.bankaccount.service;

import io.alex.exception.AmountNegativeValueException;
import io.alex.exception.InsufficientAmountException;
import io.alex.model.Operation;
import io.alex.operationEnum.OperationType;
import io.alex.printer.ConsoleFormattedOperationsPrinter;
import io.alex.repository.OperationsRepository;
import io.alex.service.OperationsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static io.alex.bankaccount.resources.FakeOperations.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {

    @Mock
    private OperationsRepository operationsRepository;

    @Mock
    private ConsoleFormattedOperationsPrinter operationsPrinter;

    private OperationsServiceImpl operationsServiceImpl;

    @BeforeEach
    void initMock() {
        operationsServiceImpl = new OperationsServiceImpl(operationsRepository, Clock.fixed(Instant.parse("2022-11-23T18:16:30.00Z"), ZoneId.of("UTC")), operationsPrinter);
    }

    @Test
    @DisplayName("Should allow the withdrawal with rounded amount and update the balance")
    void withdrawWithRoundedAmountTest() throws InsufficientAmountException, AmountNegativeValueException {

        when(operationsRepository.findOperationByOrderByDate()).thenReturn(Optional.of(fakeOperation));
        when(operationsRepository.register(any())).thenReturn(fakeWithdrawOperationToSave);

        Operation actual = operationsServiceImpl.withdraw(BigDecimal.valueOf(200.2345));

        assertThat(actual).usingRecursiveComparison().isEqualTo(fakeWithdrawRegisterOperation);

        verify(operationsRepository).findOperationByOrderByDate();
        verify(operationsRepository).register(Operation
                .builder()
                .date(LocalDateTime.now(fixedClock))
                .operationType(OperationType.WITHDRAW)
                .amount(BigDecimal.valueOf(200.2345).setScale(2, RoundingMode.HALF_EVEN))
                .balance(BigDecimal.valueOf(199.77).setScale(2, RoundingMode.HALF_EVEN))
                .build());
        verifyNoMoreInteractions(operationsRepository);
    }

    @Test
    @DisplayName("Shouldn't allow withdrawal and raise an Insufficient amount Exception ")
    void cantWithdrawBecauseOfInsufficientMoneyTest() {

        when(operationsRepository.findOperationByOrderByDate()).thenReturn(Optional.ofNullable(fakeOperation));
        assertThrows(InsufficientAmountException.class, () -> operationsServiceImpl.withdraw(BigDecimal.valueOf(1000.00)));

        verifyNoMoreInteractions(operationsRepository);
    }

    @Test
    @DisplayName("Shouldn't allow withdrawal and raise an Amount negative value exception")
    void cantWithdrawBecauseOfNegativeValueRequestedTest() {

        assertThrows(AmountNegativeValueException.class, () -> operationsServiceImpl.withdraw(BigDecimal.valueOf(-400.00)));

        verifyNoMoreInteractions(operationsRepository);
    }

    @Test
    @DisplayName("Should deposit the amount and update the balance")
    void depositTest() throws AmountNegativeValueException {

        when(operationsRepository.findOperationByOrderByDate()).thenReturn(Optional.of(fakeOperation2));
        when(operationsRepository.register(any())).thenReturn(fakeOperation2Expected);

        Operation actual = operationsServiceImpl.deposit(BigDecimal.valueOf(200));

        assertThat(actual).usingRecursiveComparison().isEqualTo(fakeOperation2Expected);

        verify(operationsRepository).findOperationByOrderByDate();
        verify(operationsRepository).register(Operation
                .builder()
                .date(LocalDateTime.now(fixedClock))
                .operationType(OperationType.DEPOSIT)
                .amount(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_EVEN))
                .balance(BigDecimal.valueOf(800).setScale(2, RoundingMode.HALF_EVEN))
                .build());
        verifyNoMoreInteractions(operationsRepository);
    }

    @Test
    @DisplayName("Should call display account statement method")
    void displayAccountBalanceTest() {

        when(operationsRepository.getAll()).thenReturn(List.of(fakeOperation));
        operationsServiceImpl.displayAccountStatement();

        verify(operationsPrinter).print(List.of(fakeOperation));
        verify(operationsRepository).getAll();
        verifyNoMoreInteractions(operationsRepository);
        verifyNoMoreInteractions(operationsPrinter);
    }
}
