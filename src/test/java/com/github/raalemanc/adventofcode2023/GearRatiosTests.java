package com.github.raalemanc.adventofcode2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.awaitility.Awaitility.await;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.ShellTestClient.NonInteractiveShellSession;
import org.springframework.shell.test.autoconfigure.ShellTest;

@ShellTest
class GearRatiosTests {

  @Autowired
  ShellTestClient client;

  private static final String GEAR_COMMAND = "gear";

  @Test
  void testGearDay3Part1() {
    final var expectedValue = "4361";
    NonInteractiveShellSession session= client.nonInterative(GEAR_COMMAND, "src/test/resources/engine_schematic_day3_part1.txt").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      ShellAssertions.assertThat(session.screen())
          .containsText(expectedValue);
    });
  }

  @Test
  void testGearDay3Part1_DirectCall() throws URISyntaxException, IOException {
    final var expectedValue = "4361";
    GearRatiosCommand gearRatiosCommand = new GearRatiosCommand();

    final var result = gearRatiosCommand.gear("src/test/resources/engine_schematic_day3_part1.txt");

    assertEquals(expectedValue, result);

  }

}
