package com.github.raalemanc.adventofcode2023;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
class SeedTests {

  @Autowired
  ShellTestClient client;

  private static final String SEED_COMMAND = "seed";

  @Test
  void testSeedDay5Part1() {
    final var expectedValue = "35";
    NonInteractiveShellSession session= client.nonInterative(SEED_COMMAND, "src/test/resources/almanac_day5_part1.txt").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      ShellAssertions.assertThat(session.screen())
          .containsText(expectedValue);
    });
  }


  @Test
  void SeedDay5Part2_DirectCall() throws URISyntaxException, IOException {
    final var expectedValue = "35";
    SeedCommand command = new SeedCommand();

    final var result = command.seed("src/test/resources/almanac_day5_part1.txt");

    assertEquals(expectedValue, result);

  }

}
