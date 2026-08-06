package br.com.zenon.fraud;

import static java.lang.IO.println;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.repository.TransactionRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class FraudMain {
  public static void main(String[] args) throws IOException {

    TransactionIngestor transactionIngestor = new TransactionIngestor();
    System.out.println(
        "Select an option:\n"
            + "1 - Fraud analysis from file\n"
            + "2 - Find transaction by origin client\n"
            + "3 - Benchmark between list and map using last transaction\n"
            + "4 - Analyse errors in bad data");

    Properties properties = new Properties();

    try (InputStream input =
        FraudMain.class.getClassLoader().getResourceAsStream("application.properties")) {

      if (input == null) {
        throw new IllegalStateException("application.properties not found");
      }

      properties.load(input);
    }

    String filePath = properties.getProperty("input.file");
    String fileWithErrorsPath = properties.getProperty("input.file.with.bad.data");

    try (Scanner scanner = new Scanner(System.in)) {
      String option = scanner.nextLine();
      List<Transaction> transactions = new ArrayList<>();
      if (option.equals("1") || option.equals("2") || option.equals("3")) {
        transactions = transactionIngestor.getTransactions(filePath);
      } else if (option.equals("4")) {
        transactions = transactionIngestor.getTransactions(fileWithErrorsPath);
      }

      TransactionRepository transactionRepository = new TransactionListRepository(transactions);
      switch (option) {
        case "1" -> {
          FraudAnalyser fraudAnalyser = new FraudAnalyser(transactionIngestor, filePath);

          println("Fraud count:" + fraudAnalyser.fraudsCount(transactions));

          println("Top 3 frauds:");
          fraudAnalyser.topThreeFrauds(transactions).forEach(System.out::println);

          println("Top 5 suspect clients:");
          fraudAnalyser.topFiveSuspectsClients(transactions).forEach(System.out::println);

          println("Total fraud amount: " + fraudAnalyser.fraudsSum(transactions));

          println("Frauds by type:");
          fraudAnalyser
              .fraudsByType(transactions)
              .forEach((type, count) -> println(type + ": " + count));
        }
        case "2" -> {
          System.out.println("Enter the origin client name:");
          String nameOriginClient = scanner.nextLine();

          transactionRepository
              .getTransactionByOriginClient(nameOriginClient)
              .ifPresentOrElse(
                  transaction -> System.out.println("Transaction found: " + transaction),
                  () ->
                      System.out.println(
                          "No transaction found for origin client: " + nameOriginClient));
        }
        case "3" -> {
          String nameOriginClient = "C1868032458";

          long startListTime = System.nanoTime();
          transactionRepository
              .getTransactionByOriginClient(nameOriginClient)
              .ifPresentOrElse(
                  transaction -> System.out.println("Transaction found: " + transaction),
                  () ->
                      System.out.println(
                          "No transaction found for origin client: " + nameOriginClient));
          long endListTime =
              (System.nanoTime() - startListTime) / 1_000_000; // Convert to milliseconds
          System.out.println("Time taken: " + endListTime + " ms");

          long startMapTime = System.nanoTime();
          System.out.println(
              "Transaction found: "
                  + transactionRepository
                      .getMapTransactionsByClient(nameOriginClient)
                      .get(nameOriginClient));
          long endMapTime =
              (System.nanoTime() - startMapTime) / 1_000_000; // Convert to milliseconds);
          System.out.println("Time taken: " + endMapTime + " ms");
        }
        case "4" -> {
          System.out.println("Total transactions processed: " + transactions.size());
        }
        default -> System.out.println("Invalid option. Please select 1, 2, or 3.");
      }
    }
  }
}
