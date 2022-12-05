package io.alex.service;

import io.alex.exception.AmountNegativeValueException;
import io.alex.exception.InsufficientAmountException;
import io.alex.model.Operation;

import java.math.BigDecimal;

public interface OperationService {
    Operation withdraw(BigDecimal amountRequested) throws AmountNegativeValueException, InsufficientAmountException, InsufficientAmountException;

    Operation deposit(BigDecimal depositedAmount) throws AmountNegativeValueException;

    void displayAccountStatement();
}
