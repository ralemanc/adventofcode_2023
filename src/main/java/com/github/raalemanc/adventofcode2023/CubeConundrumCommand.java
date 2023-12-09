package com.github.raalemanc.adventofcode2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
class CubeConundrumCommand {

  @ShellMethod
  public String cube(@ShellOption(defaultValue = "") String fileName, @ShellOption(defaultValue = "12 red, 13 green, 14 blue") String bag) throws IOException, URISyntaxException {
    final var file = getFile(fileName);
    final var cubesInTheBag = this.readCubes(bag);
    //Map<Integer, Map<String,Integer>
    final var games = Files.lines(file).parallel()
        .map(s -> s.split(":"))
        .collect(Collectors.toMap(
            a -> Integer.valueOf(a[0].replace("Game ", "")),
            a-> readSetCubes(a[1]))) ;

    final var result = games.entrySet().stream().parallel()
        .filter(g -> this.filterGame(g, cubesInTheBag))
        .map(Entry::getKey)
        .reduce(0, Integer::sum);

    return String.valueOf(result);
  }

  private List<Map<String, Integer>> readSetCubes(final String setCubes) {
    return Arrays.stream(setCubes.split(";")).map(this::readCubes).toList();
  }

  private boolean filterGame(Entry<Integer, List<Map<String, Integer>>> game, final Map<String, Integer> bag) {
        return !game.getValue().stream().map(Map::entrySet).flatMap(Set::stream)
        .filter(e -> e.getValue() > bag.get(e.getKey()))
        .findAny().isPresent();
  }

  private Map<String, Integer> readCubes(final String bag) {
    return Arrays.stream(bag.split(","))
        .map(String::trim)
        .map(s -> s.split (" "))
        .collect(Collectors.toMap(a -> a[1], a->Integer.valueOf(a[0])));
  }

  private Path getFile(final String fileName) throws URISyntaxException {
    //TODO Improve file finding
    if ("".equals(fileName)) {
      return Paths.get(getClass().getClassLoader().getResource("cube_game_day2.txt").toURI());
    }
    else {
      return Paths.get(fileName);
    }
  }

}