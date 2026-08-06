package br.com.zenon.fraud;

import static java.lang.IO.println;

import br.com.zenon.fraud.models.Transaction;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

public class FraudMain {
  private static final Logger logger = Logger.getLogger(FraudMain.class.getName());

  public static void main(String[] args) throws IOException {

    TransactionIngestor transactionIngestor = new TransactionIngestor();

    logger.info(
        "Application started. Select an option:"
            + "1 - Fraud analysis from file"
            + "2 - Find transaction by origin client"
            + "3 - Analyse errors in bad data");

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
      switch (option) {
        case "1" -> {
          FraudAnalyser fraudAnalyser = new FraudAnalyser(transactionIngestor, filePath);
          List<Transaction> frauds = fraudAnalyser.getFrauds();

          println("Fraud count:" + fraudAnalyser.fraudsCount(frauds));

          println("Top 3 frauds:");
          fraudAnalyser.topThreeFrauds(frauds).forEach(System.out::println);

          println("Top 5 suspect clients:");
          fraudAnalyser.topFiveSuspectsClients(frauds).forEach(System.out::println);

          println("Total fraud amount: " + fraudAnalyser.fraudsSum(frauds));

          println("Frauds by type:");
          fraudAnalyser.fraudsByType(frauds).forEach((type, count) -> println(type + ": " + count));
        }
        case "2" -> {
          System.out.println("Enter the origin client name:");
          String nameOriginClient = scanner.nextLine();

          TransactionListRepository transactionListRepository =
              new TransactionListRepository(transactionIngestor.getTransactions(filePath));
          transactionListRepository
              .getTransactionByOriginClient(nameOriginClient)
              .ifPresentOrElse(
                  transaction -> System.out.println("Transaction found: " + transaction),
                  () ->
                      System.out.println(
                          "No transaction found for origin client: " + nameOriginClient));
        }
        case "3" -> {
          List<Transaction> transactions = transactionIngestor.getTransactions(fileWithErrorsPath);
          System.out.println("Total transactions processed: " + transactions.size());
        }
        default -> System.out.println("Invalid option. Please select 1, 2, or 3.");
      }
    }
  }
}
