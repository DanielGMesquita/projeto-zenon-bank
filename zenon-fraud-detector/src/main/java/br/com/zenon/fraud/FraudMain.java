package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;

public class FraudMain {
  public static void main(String[] args) throws IOException {

    TransactionIngestor transactionIngestor = new TransactionIngestor();
    List<Transaction> transactions = transactionIngestor.getTransactions("data/pay_data.csv");

    for (int index = 0; index < 10; index++) {
      System.out.println("Transaction " + (index + 1) + ": " + transactions.get(index));
    }
  }
}
