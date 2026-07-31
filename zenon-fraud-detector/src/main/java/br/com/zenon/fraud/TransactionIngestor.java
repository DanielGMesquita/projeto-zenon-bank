package br.com.zenon.fraud;

import br.com.zenon.fraud.enums.TransactionType;
import br.com.zenon.fraud.models.Transaction;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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
      for (int i = 0; i < 50000 && iterator.hasNext(); i++) {
        CSVRecord record = iterator.next();
        try (ValidatorFactory factory =
            Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
          Validator validator = factory.getValidator();
          Transaction transaction = extractTransactionLineFromCSV(record);
          Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

          if (!violations.isEmpty()) {
            String message =
                violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining());
            throw new IllegalArgumentException("Validation failed for transaction: " + message);
          } else {
            transactions.add(transaction);
          }
        } catch (IllegalArgumentException e) {
          System.err.println("Erro " + Arrays.toString(record.values()) + ": " + e);
        }
      }
    }
    return transactions;
  }

  private Transaction extractTransactionLineFromCSV(CSVRecord record) {
    return new Transaction(
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
  }
}
