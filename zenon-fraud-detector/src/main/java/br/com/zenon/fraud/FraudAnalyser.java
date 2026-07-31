package br.com.zenon.fraud;

import br.com.zenon.fraud.models.Transaction;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class FraudAnalyser {
  private final TransactionIngestor transactionIngestor;
  private final String filePath;

  public FraudAnalyser(TransactionIngestor transactionIngestor, String filePath)
      throws IOException {
    this.transactionIngestor = transactionIngestor;
    this.filePath = filePath;
  }

  public List<Transaction> topThreeFrauds(List<Transaction> frauds) throws IOException {
    return frauds.stream()
        .sorted(Comparator.comparing(Transaction::amount).reversed())
        .limit(3)
        .toList();
  }

  public int fraudsCount(List<Transaction> frauds) throws IOException {
    return frauds.size();
  }

  public Set<String> topFiveSuspectsClients(List<Transaction> frauds) throws IOException {
    Map<String, BigDecimal> suspectClients = new HashMap<>();

    for (Transaction transaction : frauds) {
      suspectClients.merge(transaction.nameOrig(), transaction.amount(), BigDecimal::add);
    }

    List<Map.Entry<String, BigDecimal>> sortedSuspects = new ArrayList<>(suspectClients.entrySet());
    sortedSuspects.sort(Map.Entry.<String, BigDecimal>comparingByValue().reversed());

    return sortedSuspects.stream().map(Map.Entry::getKey).limit(5).collect(Collectors.toSet());
  }

  public List<Transaction> getFrauds() throws IOException {
    List<Transaction> transactions = transactionIngestor.getTransactions(filePath);

    return transactions.stream().filter(transaction -> transaction.isFraud() == 1).toList();
  }
}
