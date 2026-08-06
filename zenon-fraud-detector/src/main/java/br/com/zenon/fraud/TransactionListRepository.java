package br.com.zenon.fraud;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.repository.TransactionRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionListRepository implements TransactionRepository {
  private final List<Transaction> transactions;

  public TransactionListRepository(List<Transaction> transactions) {
    this.transactions = transactions;
  }

  @Override
  public List<Transaction> getTransactions() {
    return transactions;
  }

  @Override
  public Optional<Transaction> getTransactionByOriginClient(String nameOriginClient) {
    return transactions.stream()
        .filter(transaction -> transaction.nameOrig().equals(nameOriginClient))
        .findFirst();
  }

  @Override
  public Map<String, Transaction> getMapTransactionsByClient(String client) {
    return transactions.stream()
        .filter(transaction -> transaction.nameOrig().equals(client))
        .collect(Collectors.toMap(Transaction::nameOrig, transaction -> transaction));
  }
}
