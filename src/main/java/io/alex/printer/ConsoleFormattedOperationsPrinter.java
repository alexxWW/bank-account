package io.alex.printer;

import io.alex.model.Operation;
import io.alex.utils.OperationsFormatter;

import java.util.List;

public class ConsoleFormattedOperationsPrinter implements OperationsPrinter {

    private final OperationsFormatter operationFormatter;

    public ConsoleFormattedOperationsPrinter(OperationsFormatter operationFormatString) {
        this.operationFormatter = operationFormatString;
    }

    @Override
    public void print(List<Operation> operations) {
        String operationsStatement = operationFormatter.format(operations);

        System.out.print(operationsStatement);
    }
}
