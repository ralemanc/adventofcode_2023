package com.github.raalemanc.adventofcode2023;

import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.ShellTestClient.NonInteractiveShellSession;
import org.springframework.shell.test.autoconfigure.ShellTest;

//@SpringBootTest
@ShellTest
class TrebuchetTests {

  @Autowired
  ShellTestClient client;

  @Test
  void testHelp() {
    NonInteractiveShellSession session= client.nonInterative("help").run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
          ShellAssertions.assertThat(session.screen())
              .containsText("AVAILABLE COMMANDS");
        });
  }

}
