package br.com.zenon.fraud;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.Stream;

public class ReportMain {
  public static void main(String[] args) throws IOException {
    TransactionReport transactionReport = new TransactionReport();
    Properties properties = new Properties();

    try (InputStream input =
        FraudMain.class.getClassLoader().getResourceAsStream("application.properties")) {

      if (input == null) {
        throw new IllegalStateException("application.properties not found");
      }

      properties.load(input);
    }

    String filePath = properties.getProperty("input.file");

    try (Stream<String> reportStream1 = transactionReport.generateReport(filePath);
        Stream<String> reportStream2 = transactionReport.generateReport(filePath);
        Stream<String> reportStream3 = transactionReport.generateReport(filePath)) {
      long totalLines = reportStream1.count();

      long totalFrauds = transactionReport.getFraudsCountFromStream(reportStream2);



      System.out.println("Total lines in report: " + totalLines);
      System.out.println("Total frauds in report: " + totalFrauds);

    } catch (Exception e) {
      System.err.println("Error generating report: " + e.getMessage());
    }
  }
}
