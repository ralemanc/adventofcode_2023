package com.github.raalemanc.adventofcode2023;

import static java.util.function.Predicate.not;

import io.vavr.Tuple2;
import io.vavr.Tuple3;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
        .map(this::matchesCard)
        .map(Tuple2::_2)
        .filter( m -> m > 0)
        .map(m -> (int)Math.pow(2, m - 1))
        .reduce(0, Integer::sum);
    return String.valueOf(points);
  }

  @ShellMethod
  public String scratchcards_copies(@ShellOption(defaultValue = "") String fileName) throws URISyntaxException, IOException {
    final var file = FileUtil.getFile(fileName, DEFAULT_FILE_NAME);
    final Map<Integer, Integer> copies = new HashMap<>();
    final AtomicInteger maxCardGame = new AtomicInteger(0);
    Files.lines(file).sequential()
        .map(this::readCard)
        .map(this::matchesCard)
        .forEach( c ->  {
          final var card = c._1();
          final int value = this.increaseCopies(copies, card, 1);
          for(int i = 1; i <= c._2(); i++) {
            this.increaseCopies(copies, card + i, value);
          }
          maxCardGame.incrementAndGet();
        });

    final var totalCopies = copies.entrySet().stream().filter( e -> e.getKey() <= maxCardGame.get()).map(Entry::getValue).reduce(0, Integer::sum);

    return String.valueOf(totalCopies);
  }

  private Integer increaseCopies(final Map<Integer, Integer> copies, final Integer key, final Integer value) {
    return copies.compute(key, (k,v) -> (v == null) ? value : v + value);
  }

  private Tuple3<Integer, Set<Integer>, Set<Integer>> readCard(final String line) {
    final var game = line.split(":");
    final var card = game[1].split("\\|");
    final Set<Integer> winningNumbers = Arrays.stream(card[0].trim().split(" ")).map(String::trim).filter(not(String::isEmpty))
        .map(Integer::valueOf).collect(Collectors.toSet());
    final Set<Integer> selectedNumbers = Arrays.stream(card[1].trim().split(" ")).map(String::trim).filter(not(String::isEmpty))
        .map(Integer::valueOf).collect(Collectors.toSet());
    final var gameNumber = Integer.valueOf(game[0].replace("Card", "").trim());
    return new Tuple3<>(gameNumber, winningNumbers, selectedNumbers);
  }

  private Tuple2<Integer, Integer> matchesCard(Tuple3<Integer, Set<Integer>, Set<Integer>> card) {
    final var matches = (int) card._2().stream().filter(n -> card._3().contains(n)).count();
    return new Tuple2<>(card._1(), matches);
  }

}