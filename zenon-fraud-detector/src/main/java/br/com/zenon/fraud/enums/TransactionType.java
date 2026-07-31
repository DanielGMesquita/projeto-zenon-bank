package br.com.zenon.fraud.enums;

public enum TransactionType {
  PAYMENT,
  CASH_OUT,
  CASH_IN,
  TRANSFER,
  DEBIT;

  private final String transactionType;

  TransactionType() {
    this.transactionType = this.name();
  }

  public String getTransactionType() {
    return transactionType;
  }
}
