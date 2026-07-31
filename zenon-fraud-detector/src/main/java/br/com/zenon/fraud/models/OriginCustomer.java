package br.com.zenon.fraud.models;

import java.math.BigDecimal;

public record OriginCustomer(
    String nameOrig, BigDecimal oldbalanceOrg, BigDecimal newbalanceOrig) {}
