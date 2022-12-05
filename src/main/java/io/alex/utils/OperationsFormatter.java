package io.alex.utils;

import io.alex.model.Operation;
import io.alex.operationEnum.OperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class OperationsFormatter {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.ENGLISH);
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    public String format(List<Operation> operations) {
        String display = "";
        Operation mostRecentOperation = operations
                .stream()
                .max(Comparator.comparing(Operation::getDate))
                .orElseThrow();

        decimalFormat.format(mostRecentOperation.getBalance());

        if (operations.isEmpty()) {
            display = "No history available for this account";
        } else {

            for (Operation operation : operations) {
                decimalFormat.format(operation.getBalance());
                String operationType = operation.getOperationType().equals(OperationType.WITHDRAW) ? "A withdrawal operation": "A deposit operation";
                BigDecimal amountRounded = operation.getAmount().setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal balanceRounded = mostRecentOperation.getBalance().setScale(2, RoundingMode.HALF_EVEN);
                String date = operation.getDate().format(dateTimeFormatter);

                display = "" +
                        operationType +
                        " for an amount of " +
                        amountRounded +
                        "$, " +
                        "on " +
                        date +
                        ". " + "At this date the amount available on your account is " +
                        balanceRounded +"$.";
            }
        }
        return display;
    }
}
