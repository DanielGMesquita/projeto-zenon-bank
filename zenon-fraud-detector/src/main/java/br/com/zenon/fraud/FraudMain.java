package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class FraudMain {
  private static final Logger logger = Logger.getLogger(FraudMain.class.getName());

  public static void main(String[] args) throws IOException {

    TransactionIngestor transactionIngestor = new TransactionIngestor();

    logger.info("Application started");

    try (Scanner scanner = new Scanner(System.in)) {
      String filePath = scanner.nextLine();

      List<Transaction> transactions = transactionIngestor.getTransactions(filePath);

      for (Transaction transaction : transactions) {
        System.out.println("Transaction " + transaction);
      }
    }
  }
}
