package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.models.Transaction;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionRepository {
  List<Transaction> getTransactions();

  Optional<Transaction> getTransactionByOriginClient(String nameOriginClient);

  Map<String, Transaction> getMapTransactionsByClient(String client);
}
