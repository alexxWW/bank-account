package io.alex.bankaccount.printer;

import io.alex.printer.ConsoleFormattedOperationsPrinter;
import io.alex.utils.OperationsFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static io.alex.bankaccount.resources.FakeOperations.fakeOperation2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OperationsPrinterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Mock
    private OperationsFormatter operationFormatString;

    @InjectMocks
    private ConsoleFormattedOperationsPrinter consoleFormattedOperationsPrinter;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should display formatted account statement and balance in console")
    void printAccountStatementTest() {
        String expectedDisplay = """
        Transaction: DEPOSIT for an amount of 200$, on the date 2022-11-23T18:16:30. At this date the amount available on your account is 600$.\r\n";
        """;
        when(operationFormatString.format(List.of(fakeOperation2))).thenReturn(expectedDisplay);
        consoleFormattedOperationsPrinter.print(List.of(fakeOperation2));

        assertEquals(expectedDisplay, outContent.toString());

    }

}
