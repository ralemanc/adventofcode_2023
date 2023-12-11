package com.github.raalemanc.adventofcode2023;

import static java.util.function.Predicate.not;

import io.vavr.Tuple2;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
class ScratchcardsCommand {

  private static final String DEFAULT_FILE_NAME = "cards_day4.txt";

  @ShellMethod
  public String scratchcards(@ShellOption(defaultValue = "") String fileName) throws URISyntaxException, IOException {
    final var file = FileUtil.getFile(fileName, DEFAULT_FILE_NAME);
    final var points = Files.lines(file).parallel()
        .map(this::readCard)
        .map(this::pointsCard)
        .reduce(0, Integer::sum);
    return String.valueOf(points);
  }

  private Tuple2<Set<Integer>, Set<Integer>> readCard(final String line) {
    final var card = line.split(":")[1].split("\\|");
    final Set<Integer> winningNumbers = Arrays.stream(card[0].trim().split(" ")).map(String::trim).filter(not(String::isEmpty))
        .map(Integer::valueOf).collect(Collectors.toSet());
    final Set<Integer> numbers = Arrays.stream(card[1].trim().split(" ")).map(String::trim).filter(not(String::isEmpty))
        .map(Integer::valueOf).collect(Collectors.toSet());
    return new Tuple2<>(winningNumbers, numbers);
  }

  private int pointsCard(Tuple2<Set<Integer>, Set<Integer>> card) {
    final var matches = card._1().stream().filter(n -> card._2().contains(n)).count();
    if (matches == 0) {
      return 0;
    } else {
      return (int) Math.pow(2, matches - 1);
    }
  }

}