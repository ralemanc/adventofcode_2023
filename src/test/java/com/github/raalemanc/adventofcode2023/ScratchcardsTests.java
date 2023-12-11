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
class ScratchcardsTests {

  @Autowired
  ShellTestClient client;

  private static final String SCRATCHCARDS_COMMAND = "scratchcards";
  private static final String SCRATCHCARDS_COMMAND_COPIES = "scratchcards_copies";

  @Test
  void testScratchcardsDay4Part1() {
    final var expectedValue = "13";
    NonInteractiveShellSession session= client.nonInterative(SCRATCHCARDS_COMMAND, "src/test/resources/cards_day4_part1.txt").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      ShellAssertions.assertThat(session.screen())
          .containsText(expectedValue);
    });
  }

  @Test
  void testScratchcardsDay4Part2() {
    final var expectedValue = "30";
    NonInteractiveShellSession session= client.nonInterative(SCRATCHCARDS_COMMAND_COPIES, "src/test/resources/cards_day4_part2.txt").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      ShellAssertions.assertThat(session.screen())
          .containsText(expectedValue);
    });
  }
  @Test
  void Scratchcards4Part2_DirectCall() throws URISyntaxException, IOException {
    final var expectedValue = "30";
    ScratchcardsCommand gearRatiosCommand = new ScratchcardsCommand();

    final var result = gearRatiosCommand.scratchcards_copies("src/test/resources/cards_day4_part2.txt");

    assertEquals(expectedValue, result);

  }

}
