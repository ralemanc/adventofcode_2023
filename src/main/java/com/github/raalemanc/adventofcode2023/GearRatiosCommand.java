package com.github.raalemanc.adventofcode2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
class GearRatiosCommand {

  @ShellMethod
  public String gear(@ShellOption(defaultValue = "") String fileName) throws URISyntaxException, IOException {
    final var file = FileUtil.getFile(fileName, "engine_schematic_day3.txt");

    char[] previousLine = new char[1];

    final List<Integer> validNumbers = new ArrayList<>();
    try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file)))) {
      String line;
      Set<String> symbols = new HashSet<>();
      int lineNumber = -1;
      while ((line = br.readLine()) != null) {
        lineNumber++;
        char[] currentLine = line.toCharArray();
        for(int i = 0; i < currentLine.length; i++) {
          final var character = currentLine[i];
          if(character != '.' && !Character.isDigit(character)) {
            symbols.add((lineNumber - 1) + "-" + (i - 1));
            symbols.add((lineNumber - 1) + "-" + i);
            symbols.add((lineNumber - 1) + "-" + (i + 1));
            symbols.add(lineNumber + "-" + (i - 1));
            symbols.add(lineNumber + "-" + i);
            symbols.add(lineNumber + "-" + (i + 1));
            symbols.add((lineNumber + 1) + "-" + (i - 1));
            symbols.add((lineNumber + 1) + "-" + i);
            symbols.add((lineNumber + 1) + "-" + (i + 1));
          }
        }

        if(lineNumber != 0) {
          this.processNumbers(previousLine, lineNumber, symbols, validNumbers);
        }
        previousLine = currentLine;
      }
      this.processNumbers(previousLine, lineNumber, symbols, validNumbers);
    }
    final var result = validNumbers.stream().reduce(0, Integer::sum);

    return String.valueOf(result);
  }

  private void processNumbers(char[] line, int lineNumber, Set<String> symbols, List<Integer> validNumbers) {
    String number = "0";
    for(int i = 0; i < line.length; i++) {
      final var character = line[i];
      if (Character.isDigit(character)) {
        number = number + character;
      } else {
        addSymbolAdjacent(number, symbols, lineNumber -1, i, validNumbers);
        number = "0";
      }
    }
    addSymbolAdjacent(number, symbols, lineNumber -1, line.length, validNumbers);
  }

  private void addSymbolAdjacent(String number, final Set<String> symbols, final int lineNumber, final int position, List<Integer> validNumbers) {
    if ("0".equals(number)) {
      return;
    }
    number = number.replaceFirst("0", "");
    int initialColumn = position - number.length();
    for(int i = 0; i < number.length(); i++) {
      if (symbols.contains(lineNumber + "-" + (i + initialColumn))) {
        validNumbers.add(Integer.valueOf(number));
        return;
      }
    }
  }

}