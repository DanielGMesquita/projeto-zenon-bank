package br.com.zenon.fraud;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

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
        try {
          Transaction transaction = extractTransactionLineFromCSV(record);
          transactions.add(extractTransactionLineFromCSV(record));
        } catch (IllegalArgumentException e) {
          System.err.println(
              "Error parsing record at line "
                  + record.getRecordNumber()
                  + ": "
                  + e
                  + ". Skipping this record.");
          continue;
        }
      }
    }
    return transactions;
  }

  public List<Transaction> getInvalidTransactions(String filePath) throws IOException {
    List<Transaction> invalidTransactions = new ArrayList<>();
    try (ValidatorFactory factory =
        Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()) {
      Validator validator = factory.getValidator();
      Reader reader = Files.newBufferedReader(Paths.get(filePath));
      try (CSVParser parser =
          CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get().parse(reader)) {
        Iterator<CSVRecord> iterator = parser.iterator();
        for (int i = 0; i < 1000 && iterator.hasNext(); i++) {
          CSVRecord record = iterator.next();
          try {
            Transaction transaction = extractTransactionLineFromCSV(record);
            Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

            if (!violations.isEmpty()) {
              String message =
                  violations.stream()
                      .map(ConstraintViolation::getMessage)
                      .collect(Collectors.joining());
              throw new IllegalArgumentException("Validation failed for transaction: " + message);
            } else {
              invalidTransactions.add(transaction);
            }
          } catch (IllegalArgumentException e) {
            System.err.println(
                "Error parsing record at line "
                    + record.getRecordNumber()
                    + ": "
                    + e
                    + ". Skipping this record.");
            continue;
          }
        }
      }
    }
    return invalidTransactions;
  }

  private Transaction extractTransactionLineFromCSV(CSVRecord record) {
    try (ValidatorFactory factory =
        Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()) {
      Validator validator = factory.getValidator();
      try {
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
        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(
            "Invalid number format in record: " + record.toString(), e);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid enum value in record: " + record.toString(), e);
      }
    }
    return null;
  }
}
