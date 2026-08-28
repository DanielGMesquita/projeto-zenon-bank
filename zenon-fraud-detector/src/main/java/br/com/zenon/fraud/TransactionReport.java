package br.com.zenon.fraud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TransactionReport {
  public TransactionReport() {}

  public Stream<String> generateReport(String filePath) throws IOException {
    return Files.lines(Paths.get(filePath));
  }

  public int getFraudsCountFromStream(Stream<String> lines) {
    int fraudCount = 0;
    for (String[] lineArray : lines.map(l -> l.split(",")).toArray(String[][]::new)) {
      if (lineArray[9].equals("1")) {
        fraudCount++;
      }
    }
    return fraudCount;
  }
}
