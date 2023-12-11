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

  @Test
  void testScratchcardsDay1Part1() {
    final var expectedValue = "13";
    NonInteractiveShellSession session= client.nonInterative(SCRATCHCARDS_COMMAND, "src/test/resources/cards_day4_part1.txt").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      ShellAssertions.assertThat(session.screen())
          .containsText(expectedValue);
    });
  }

  @Test
  void testGearDay3Part2_DirectCall() throws URISyntaxException, IOException {
    final var expectedValue = "13";
    ScratchcardsCommand gearRatiosCommand = new ScratchcardsCommand();

    final var result = gearRatiosCommand.scratchcards("src/test/resources/cards_day4_part1.txt");

    assertEquals(expectedValue, result);

  }

}
