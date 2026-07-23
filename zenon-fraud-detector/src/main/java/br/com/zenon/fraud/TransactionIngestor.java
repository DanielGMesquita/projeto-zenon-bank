package br.com.zenon.fraud;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class TransactionIngestor {
  public TransactionIngestor() {}

  public List<Transaction> getTransactions(String filePath) throws IOException {
    List<Transaction> transactions = new ArrayList<>();
    Reader reader = Files.newBufferedReader(Paths.get(filePath));
    try (CSVParser parser =
        CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get().parse(reader)) {
      Iterator<CSVRecord> iterator = parser.iterator();
      for (int i = 0; i < 1000 && iterator.hasNext(); i++) {
        CSVRecord record = iterator.next();
        Transaction transaction =
            new Transaction(
                Integer.parseInt(record.get("step")),
                TransactionType.valueOf(record.get("type")),
                new BigDecimal(record.get("amount")),
                record.get("nameOrig"),
                new BigDecimal(record.get("oldbalanceOrg")),
                new BigDecimal(record.get("newbalanceOrig")),
                record.get("nameDest"),
                new BigDecimal(record.get("oldbalanceDest")),
                new BigDecimal(record.get("newbalanceDest")),
                Integer.parseInt(record.get("isFraud")),
                Integer.parseInt(record.get("isFlaggedFraud")));
        transactions.add(transaction);
      }
    }
    return transactions;
  }
}
