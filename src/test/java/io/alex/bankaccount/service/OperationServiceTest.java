package io.alex.bankaccount.service;

import io.alex.exception.AmountNegativeValueException;
import io.alex.model.Operation;
import io.alex.operationEnum.OperationType;
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
import java.util.Optional;

import static io.alex.bankaccount.resources.FakeOperations.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {

    @Mock
    private OperationsRepository operationsRepository;

    private OperationsServiceImpl operationsServiceImpl;

    @BeforeEach
    void initMock() {
        operationsServiceImpl = new OperationsServiceImpl(operationsRepository, Clock.fixed(Instant.parse("2022-11-23T18:16:30.00Z"), ZoneId.of("UTC"))
        );
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
}
