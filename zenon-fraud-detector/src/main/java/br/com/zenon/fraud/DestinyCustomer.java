package br.com.zenon.fraud;

import java.math.BigDecimal;

public record DestinyCustomer(
    String nameDest, BigDecimal oldbalanceDest, BigDecimal newbalanceDest) {}
