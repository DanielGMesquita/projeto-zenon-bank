package br.com.zenon.fraud;

import br.com.zenon.fraud.models.Transaction;
import br.com.zenon.fraud.repository.TransactionRepository;
import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {
  private List<Transaction> transactions;

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
}
