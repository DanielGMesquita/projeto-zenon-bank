package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(
    int step,
    String type,
    BigDecimal amount,
    String nameOrig,
    BigDecimal oldbalanceOrg,
    BigDecimal newbalanceOrig,
    String nameDest,
    BigDecimal oldbalanceDest,
    BigDecimal newbalanceDest,
    int isFraud,
    int isFlaggedFraud) {}
