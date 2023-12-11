package com.github.raalemanc.adventofcode2023;

import io.vavr.Tuple2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
class GearRatiosCommand {

  //TODO a lot of refactors

  @ShellMethod
  public String gear_power(@ShellOption(defaultValue = "") String fileName) throws URISyntaxException, IOException {
    final var file = FileUtil.getFile(fileName, "engine_schematic_day3.txt");
    //TODO: Rename this wrong name
    Set<Set<String>> powers = new HashSet<>();
    Map<String, Tuple2<Integer, Integer>> numbers = new HashMap<>();
    int id = 0;
    try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file)))) {
      String line;
      int lineNumber = -1;
      while ((line = br.readLine()) != null) {
        lineNumber++;
        char[] currentLine = line.toCharArray();
        String number = "0";
        for(int i = 0; i < currentLine.length; i++) {
          final var character = currentLine[i];
          if (character == '*') {
            this.addPowerPositions(powers, lineNumber, i);
            this.addNumber(numbers, number, lineNumber,  i, id++);
            number = "0";
          } else if (Character.isDigit(character)) {
            number += character;
          } else {
            this.addNumber(numbers, number, lineNumber,  i, id++);
            number = "0";
          }
        }
        this.addNumber(numbers, number, lineNumber,  currentLine.length, id++);
      }
    }
    final var result = powers.stream()
        .map(p -> this.powerAdjacentNumbers(p, numbers))
        .reduce(0, Integer::sum);
    return String.valueOf(result);
  }

  private Integer powerAdjacentNumbers(final Set<String> powerPosition, final Map<String, Tuple2<Integer, Integer>> numbers) {
    final List<Integer> values = powerPosition.stream().map(p -> numbers.get(p)).filter(Objects::nonNull).distinct().map(Tuple2::_1).toList();
    if (values.size() == 2) {
      return values.get(0) * values.get(1);
    } else {
      return 0;
    }
  }

  private void addPowerPositions(Set<Set<String>> powers, int lineNumber, int position) {
    final Set<String> adjacent = new HashSet<>();
    this.addAdjacent(adjacent, lineNumber, position);
    powers.add(adjacent);
  }

  private void addNumber(final Map<String, Tuple2<Integer, Integer>> numbers, String number, final int lineNumber, final int followingPosition, int id) {
    if ("0".equals(number)) {
      return;
    }
    number = number.replaceFirst("0", "");
    int position = followingPosition - number.length();
    final Tuple2<Integer, Integer> value = new Tuple2<>(Integer.valueOf(number), id);

    for (int i = 0; i < number.length(); i ++) {
      numbers.put(lineNumber + "-" + (position + i), value);
    }
  }
  @ShellMethod
  public String gear(@ShellOption(defaultValue = "") String fileName) throws URISyntaxException, IOException {
    final var file = FileUtil.getFile(fileName, "engine_schematic_day3.txt");
    final List<Integer> validNumbers = new ArrayList<>();

    try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file)))) {
      char[] previousLine = new char[1];
      Set<String> symbols = new HashSet<>();
      int lineNumber = -1;
      String line;
      while ((line = br.readLine()) != null) {
        lineNumber++;
        char[] currentLine = line.toCharArray();
        for(int i = 0; i < currentLine.length; i++) {
          final var character = currentLine[i];
          if(character != '.' && !Character.isDigit(character)) {
            this.addAdjacent(symbols, lineNumber, i);
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

  private void addAdjacent(final Set<String> adjacent, final int lineNumber, final int position) {
    //TODO: Remove negative
    adjacent.add((lineNumber - 1) + "-" + (position - 1));
    adjacent.add((lineNumber - 1) + "-" + position);
    adjacent.add((lineNumber - 1) + "-" + (position + 1));
    adjacent.add(lineNumber + "-" + (position - 1));
    adjacent.add(lineNumber + "-" + position);
    adjacent.add(lineNumber + "-" + (position + 1));
    adjacent.add((lineNumber + 1) + "-" + (position - 1));
    adjacent.add((lineNumber + 1) + "-" + position);
    adjacent.add((lineNumber + 1) + "-" + (position + 1));
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