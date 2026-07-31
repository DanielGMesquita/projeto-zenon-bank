package br.com.zenon.fraud;

import static java.lang.IO.println;

import br.com.zenon.fraud.models.Transaction;
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
  }
}
