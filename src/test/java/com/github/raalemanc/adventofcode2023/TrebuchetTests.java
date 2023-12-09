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
class TrebuchetTests {

  @Autowired
  ShellTestClient client;

  private static final String TREBUCHET_COMMAND = "trebuchet";

  @Test
  void testHelp() {
    NonInteractiveShellSession session= client.nonInterative("help").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
          ShellAssertions.assertThat(session.screen())
              .containsText("AVAILABLE COMMANDS");
        });
  }

  @Test
  void testCalibrationDay1Part1() {
    final var expectedValue = "142";
    NonInteractiveShellSession session= client.nonInterative(TREBUCHET_COMMAND, "src/test/resources/calibration_day1_part1.txt").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      ShellAssertions.assertThat(session.screen())
          .containsText(expectedValue);
    });
  }

  @Test
  void testCalibrationDay1Part2() {
    final var expectedValue = "281";
    NonInteractiveShellSession session= client.nonInterative(TREBUCHET_COMMAND, "src/test/resources/calibration_day1_part2.txt").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      ShellAssertions.assertThat(session.screen())
          .containsText(expectedValue);
    });
  }

  @Test
  void testSolutionDay1() {
    final var expectedValue = "54076";
    NonInteractiveShellSession session= client.nonInterative(TREBUCHET_COMMAND, "src/test/resources/calibration_day1.txt").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      ShellAssertions.assertThat(session.screen())
          .containsText(expectedValue);
    });
  }

}
