package com.github.raalemanc.adventofcode2023;

import static java.util.function.Predicate.not;

import io.vavr.Tuple2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
class SeedCommand {

  private static final String DEFAULT_FILE_NAME = "almanac_day5.txt";

  @ShellMethod
  public String seed(@ShellOption(defaultValue = "") String fileName) throws URISyntaxException, IOException {
    final var file = FileUtil.getFile(fileName, DEFAULT_FILE_NAME);
    try(final BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file)))) {
      final Set<Long> seeds = this.readSeeds(br.readLine());
      br.readLine(); //empty line
      final var seed2Soil = this.readMap(br);
      final var soil2Fertilizer = this.readMap(br);
      final var fertilizer2Water = this.readMap(br);
      final var water2Light = this.readMap(br);
      final var light2Temperature = this.readMap(br);
      final var temperature2Humidity = this.readMap(br);
      final var humidity2Location = this.readMap(br);

      final var min = seeds.stream().parallel()
          .map(i -> this.transform(i, seed2Soil))
          .map(i -> this.transform(i, soil2Fertilizer))
          .map(i -> this.transform(i, fertilizer2Water))
          .map(i -> this.transform(i, water2Light))
          .map(i -> this.transform(i, light2Temperature))
          .map(i -> this.transform(i, temperature2Humidity))
          .map(i -> this.transform(i, humidity2Location))
          .collect(Collectors.summarizingLong(Long::longValue))
          .getMin();
      return String.valueOf(min);
    }
  }

  private Long transform(final Long input, final Map<Tuple2<Long, Long>, Long> map) {
    final var diff = map.entrySet().stream().filter( e -> input >= e.getKey()._1() && input <= e.getKey()._2())
        .map(Entry::getValue)
        .findFirst()
        .orElse(0L);
    final var value =  input - diff;
    if (value <= 0) {
      System.out.println("Negative result: " + value);
    }
    return value;
  }

  private Map<Tuple2<Long, Long>, Long> readMap(final BufferedReader br) throws IOException {
    System.out.println("Reading line : " + br.readLine()); //Ignore Header map:
    final Map<Tuple2<Long, Long>, Long> convertionMap = new HashMap<>();
    String line = br.readLine();
    while (line != null && !"".equals(line.trim())) {
      final var values = line.split(" ");
      final var destination = Long.valueOf(values[0]);
      final var source = Long.valueOf(values[1]);
      final var rangeLength = Long.valueOf(values[2]);
      final var range = new Tuple2<>(source,  source + rangeLength - 1);
      convertionMap.put(range, source - destination);
      line = br.readLine();
    }
    return convertionMap;
  }
  private Set<Long> readSeeds(final String line) {
    System.out.println("Reading line : " + line);
    return Arrays.stream(line.replace("seeds: ", "")
        .split(" "))
        .map(String::trim).filter(not(String::isEmpty))
        .map(Long::valueOf)
        .collect(Collectors.toSet());

  }


}