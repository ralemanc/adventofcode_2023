package com.github.raalemanc.adventofcode2023;

import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.ShellTestClient.NonInteractiveShellSession;
import org.springframework.shell.test.autoconfigure.ShellTest;

@ShellTest
class CubeConundrumTests {

  @Autowired
  ShellTestClient client;

  private static final String CUBE_COMMAND = "cube";

  @Test
  void testCubeGameDay1Part1() {
    final var expectedValue = "8";
    NonInteractiveShellSession session= client.nonInterative(CUBE_COMMAND, "src/test/resources/cube_game_day2_part1.txt", "12 red, 13 green, 14 blue").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      ShellAssertions.assertThat(session.screen())
          .containsText(expectedValue);
    });
  }

}
