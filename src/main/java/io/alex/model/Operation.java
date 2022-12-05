package io.alex.model;

import io.alex.operationEnum.OperationType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
public class Operation {

    private LocalDateTime date;
    private BigDecimal amount;
    private OperationType operationType;
    private BigDecimal balance;

    public Operation(LocalDateTime date, BigDecimal amount, OperationType operationType, BigDecimal balance) {
        this.date = date;
        this.amount = amount;
        this.operationType = operationType;
        this.balance = balance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(date, operation.date) && Objects.equals(amount, operation.amount) && operationType == operation.operationType && Objects.equals(balance, operation.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, operationType, balance);
    }
}
