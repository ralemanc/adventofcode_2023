package com.github.raalemanc.adventofcode2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
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
  public String possibleCubes(@ShellOption(defaultValue = "") String fileName, @ShellOption(defaultValue = "12 red, 13 green, 14 blue") String bag) throws IOException, URISyntaxException {
    final var file = getFile(fileName);
    final var cubesInTheBag = this.readCubes(bag);

    final var games = readGames(file);

    final var result = games.entrySet().stream().parallel()
        .filter(g -> this.filterPossibleGame(g, cubesInTheBag))
        .map(Entry::getKey)
        .reduce(0, Integer::sum);

    return String.valueOf(result);
  }

  @ShellMethod
  public String powerCubes(@ShellOption(defaultValue = "") String fileName) throws IOException, URISyntaxException {
    final var file = this.getFile(fileName);

    final var games = readGames(file);

    final var result = games.entrySet().stream().parallel()
        .map(Entry::getValue)
        .map(this::powerCube)
        .reduce(0, Integer::sum);

    return String.valueOf(result);
  }

  private Integer powerCube(List<Map<String, Integer>> game) {
    final Map<String, Integer> maxCubes = new HashMap<>();
    for(final Map<String, Integer> revealed: game) {
      revealed.forEach( (color, value) -> {
        if(maxCubes.getOrDefault(color, 0) < value) {
          maxCubes.put(color, value);
        }
      });
    }
    final var power = maxCubes.values().stream().reduce(1, (a, b) -> a * b);
    return power;
  }



  private List<Map<String, Integer>> readSetCubes(final String setCubes) {
    return Arrays.stream(setCubes.split(";")).map(this::readCubes).toList();
  }

  private boolean filterPossibleGame(Entry<Integer, List<Map<String, Integer>>> game, final Map<String, Integer> bag) {
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

  private Map<Integer, List<Map<String, Integer>>> readGames(final Path file) throws IOException {
    return Files.lines(file).parallel()
        .map(s -> s.split(":"))
        .collect(Collectors.toMap(
            a -> Integer.valueOf(a[0].replace("Game ", "")),
            a-> readSetCubes(a[1])));
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