package br.com.zenon.fraud;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record Transaction(
    @NotNull @Min(value = 1, message = "Step should be positive") int step,
    @NotNull TransactionType type,
    @NotNull @DecimalMin(value = "0.00", message = "Value should not be negative")
        BigDecimal amount,
    @NotNull String nameOrig,
    @NotNull @DecimalMin(value = "0.00", message = "Value should not be negative")
        BigDecimal oldbalanceOrg,
    @NotNull @DecimalMin(value = "0.00", message = "Value should not be negative")
        BigDecimal newbalanceOrig,
    @NotNull String nameDest,
    @NotNull @DecimalMin(value = "0.00", message = "Value should not be negative")
        BigDecimal oldbalanceDest,
    @NotNull @DecimalMin(value = "0.00", message = "Value should not be negative")
        BigDecimal newbalanceDest,
    @NotNull int isFraud,
    @NotNull int isFlaggedFraud) {}
