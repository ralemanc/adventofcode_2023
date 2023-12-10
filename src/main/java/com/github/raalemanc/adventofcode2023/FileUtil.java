package com.github.raalemanc.adventofcode2023;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

  public static Path getFile(final String fileName, final String defaultFileName) throws URISyntaxException {
    //TODO Improve file finding
    if ("".equals(fileName)) {
      return Paths.get(FileUtil.class.getClassLoader().getResource(defaultFileName).toURI());
    }
    else {
      return Paths.get(fileName);
    }
  }

}
