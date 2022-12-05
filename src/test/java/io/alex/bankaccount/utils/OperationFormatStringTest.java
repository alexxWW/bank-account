package io.alex.bankaccount.utils;

import io.alex.utils.OperationsFormatter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.alex.bankaccount.resources.FakeOperations.fakeOperation2;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationFormatStringTest {

    private final OperationsFormatter operationFormatString;

    OperationFormatStringTest() {
        this.operationFormatString = new OperationsFormatter();
    }

    @Test
    void displayFormattedOperationsHistoryTest() {
        String expectedFormattedOperation = "A deposit operation for an amount of 200.00$, on Wednesday, 23 November, 2022. At this date the amount available on your account is 600.00$.";

        assertEquals(expectedFormattedOperation, operationFormatString.format(List.of(fakeOperation2)));
    }
}
