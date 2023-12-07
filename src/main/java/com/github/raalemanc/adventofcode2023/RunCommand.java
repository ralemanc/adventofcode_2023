package com.github.raalemanc.adventofcode2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class RunCommand {


  @ShellMethod
  public String run(@ShellOption(defaultValue = "") String fileName) throws IOException, URISyntaxException {
    Path file;
    //TODO Improve file finding
    if ("".equals(fileName)) {
      file = Paths.get(getClass().getClassLoader().getResource("calibration.txt").toURI());
    }
    else {
      file = Paths.get(fileName);
    }

    final var total = Files.lines(file).parallel().map(this::digitFromLine).reduce(0, Integer::sum);
    return String.valueOf(total);
  }

  public Integer digitFromLine(final String line) {
    char left = '0';
    char right = '0';
    for (int i = 0; i <= line.length(); i++) {
      final var character = line.charAt(i);
      if(character >= 47 && character <= 57) {
        left = character;
        break;
      }
    }
    for (int j = line.length() -1; j >=0; j--) {
      //TODO: Improve break when j <= i
      final var character = line.charAt(j);
      if(character >= 47 && character <= 57) {
        right = character;
        break;
      }
    }
    return Integer.valueOf(left + "" + right);
  }

}
