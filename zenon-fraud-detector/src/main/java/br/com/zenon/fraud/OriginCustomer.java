package br.com.zenon.fraud;

import java.math.BigDecimal;

public record OriginCustomer(
    String nameOrig, BigDecimal oldbalanceOrg, BigDecimal newbalanceOrig) {}
