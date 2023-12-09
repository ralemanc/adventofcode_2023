package com.github.raalemanc.adventofcode2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
class TrebuchetCommand {

  private static final char ZERO = '0';
  private static final char NINE = '9';

  //TODO Add Enum?
  private static List<String> digits = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

  @ShellMethod
  public String trebuchet(@ShellOption(defaultValue = "") String fileName) throws IOException, URISyntaxException {
    final var file = getFile(fileName);
    final var total = Files.lines(file).parallel()
        .map(this::convertDigit)
        .map(this::digitFromLine)
        .reduce(0, Integer::sum);
    return String.valueOf(total);
  }

  private String convertDigit(final String line) {
    //System.out.print(line + " ");
    final var nextDigitPos = new AtomicInteger(0);
    final var totalPos = new AtomicInteger(0);
    AtomicReference<String> lineReference = new AtomicReference<>(line);
    digits.stream()
        .map(d -> this.getOccurrences(line, d))
        .flatMap(List::stream)
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (key1, key2) -> key1, TreeMap::new))
        .forEach((e,k) -> {
          if (nextDigitPos.get() <= e - totalPos.get()) {
            lineReference.getAndUpdate(l -> l.replaceFirst(k, this.getDigit(k)));
            nextDigitPos.incrementAndGet();
            totalPos.addAndGet(k.length()-1);
          }
        });
    //System.out.print(lineReference.get() + " ");
    return lineReference.get();
  }

  private String getDigit(final String value) {
    return switch(value) {
      case "one" -> "1";
      case "two" -> "2";
      case "three" -> "3";
      case "four" -> "4";
      case "five" -> "5";
      case "six" -> "6";
      case "seven" -> "7";
      case "eight" -> "8";
      case "nine" -> "9";
      default -> value;
    };
  }

  private Integer digitFromLine(final String line) {
    char left = '0';
    char right = '0';
    for (int i = 0; i < line.length(); i++) {
      final var character = line.charAt(i);
      if(character > ZERO && character <= NINE) {
        left = character;
        break;
      }
    }
    for (int j = line.length() -1; j >=0; j--) {
      //TODO: Improve break when j <= i
      final var character = line.charAt(j);
      if(character > ZERO && character <= NINE) {
        right = character;
        break;
      }
    }
    final var value = Integer.valueOf(left + "" + right);
    //System.out.println(value);
    return value;
  }

  private Path getFile(final String fileName) throws URISyntaxException {
    //TODO Improve file finding
    if ("".equals(fileName)) {
     return Paths.get(getClass().getClassLoader().getResource("calibration_day1.txt").toURI());
    }
    else {
      return Paths.get(fileName);
    }
  }

  private List<Entry<Integer, String>> getOccurrences(final String line, final String value) {
    final List<Entry<Integer, String>> occurrences = new ArrayList<>();
    int index = 0;
    while (index != -1) {
      index = line.indexOf(value, index);
      if (index != -1) {
        occurrences.add(new SimpleEntry<>(index, value));
        index += value.length();
      }
    }
    return occurrences;
  }

}
