package io.alex.bankaccount.resources;

import io.alex.model.Operation;
import io.alex.operationEnum.OperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class FakeOperations {

    public static Clock fixedClock = Clock.fixed(Instant.parse("2022-11-23T18:16:30.00Z"), ZoneId.of("UTC"));
    static Clock fixedClockToCompare = Clock.fixed(Instant.parse("2022-11-26T18:16:30.00Z"), ZoneId.of("UTC"));

    public static Operation fakeOperation = Operation.builder()
                    .date(LocalDateTime.now(fixedClockToCompare))
                    .amount(BigDecimal.valueOf(200.2345).setScale(2, RoundingMode.HALF_EVEN))
                    .balance(BigDecimal.valueOf(400).setScale(2, RoundingMode.HALF_EVEN))
                    .operationType(OperationType.WITHDRAW)
                    .build();

    public static Operation fakeOperation2 = Operation.builder()
                    .date(LocalDateTime.now(fixedClock))
                    .amount(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_EVEN))
                    .balance(BigDecimal.valueOf(600).setScale(2, RoundingMode.HALF_EVEN))
                    .operationType(OperationType.DEPOSIT)
                    .build();

    public static Operation fakeOperation2Expected = Operation.builder()
            .date(LocalDateTime.now(fixedClock))
            .amount(BigDecimal.valueOf(200))
            .balance(BigDecimal.valueOf(800))
            .operationType(OperationType.DEPOSIT)
            .build();



    public static Operation fakeWithdrawOperationToSave = Operation.builder()
                    .date(LocalDateTime.now(fixedClock))
                    .amount(BigDecimal.valueOf(200.23))
                    .balance(BigDecimal.valueOf(199.77))
                    .operationType(OperationType.WITHDRAW)
                    .build();

    public static Operation fakeWithdrawRegisterOperation =  Operation.builder()
                    .date(LocalDateTime.now(fixedClock))
                    .amount(BigDecimal.valueOf(200.23))
                    .balance(BigDecimal.valueOf(199.77))
                    .operationType(OperationType.WITHDRAW)
                    .build();

    public static Operation fakeDepositOperationToSave = Operation.builder()
            .date(LocalDateTime.now(fixedClock))
            .amount(BigDecimal.valueOf(200.23))
            .balance(BigDecimal.valueOf(199.77))
            .operationType(OperationType.DEPOSIT)
            .build();

    public static Operation fakeDepositRegisterOperation =  Operation.builder()
            .date(LocalDateTime.now(fixedClock))
            .amount(BigDecimal.valueOf(200.23))
            .balance(BigDecimal.valueOf(199.77))
            .operationType(OperationType.DEPOSIT)
            .build();
    public static List<Operation> fakeOperationListExpected = List.of(Operation.builder()
                    .date(LocalDateTime.now(fixedClock))
                    .amount(BigDecimal.valueOf(200))
                    .balance(BigDecimal.valueOf(400))
                    .operationType(OperationType.WITHDRAW)
                    .build(),
            Operation.builder()
                    .date(LocalDateTime.now(fixedClockToCompare))
                    .amount(BigDecimal.valueOf(200))
                    .balance(BigDecimal.valueOf(600))
                    .operationType(OperationType.DEPOSIT)
                    .build()
    );
}
