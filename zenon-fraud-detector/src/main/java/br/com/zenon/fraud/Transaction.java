package br.com.zenon.fraud;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record Transaction(
    @NotNull @Min(value = 1, message = "Step must me greater than 0") int step,
    @NotNull String type,
    @NotNull @Min(value = 0, message = "Value should not be negative") BigDecimal amount,
    @NotNull String nameOrig,
    @NotNull @Min(value = 0, message = "Value should not be negative") BigDecimal oldbalanceOrg,
    @NotNull @Min(value = 0, message = "Value should not be negative") BigDecimal newbalanceOrig,
    @NotNull String nameDest,
    @NotNull @Min(value = 0, message = "Value should not be negative") BigDecimal oldbalanceDest,
    @NotNull @Min(value = 0, message = "Value should not be negative") BigDecimal newbalanceDest,
    @NotNull int isFraud,
    @NotNull int isFlaggedFraud) {}
