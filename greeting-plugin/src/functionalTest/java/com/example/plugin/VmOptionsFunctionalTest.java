package com.example.plugin;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VmOptionsFunctionalTest {

  @Test
  public void canRunTask() throws IOException {

    // Setup the test build
    File projectDir = new File("build/functionalTest");
    Files.createDirectories(projectDir.toPath());
    writeString(new File(projectDir, "settings.gradle"), "");

    writeString(new File(projectDir, "build.gradle"), generateGradle());

    // Run the build
    BuildResult result = GradleRunner.create()
                             .forwardOutput()
                             .withPluginClasspath()
                             .withArguments("vmopts")
                             .withProjectDir(projectDir)
                             .build();

    // Verify the result
    assertTrue(result.getOutput().contains("-DjwtToken=22"));
  }

  private void writeString(File file, String string) throws IOException {
    try (Writer writer = new FileWriter(file)) {
      writer.write(string);
    }
  }


  private String generateGradle() {

    String buildGradle = """
        plugins {
            id('com.example.greeting')
        }
                    
        def output = vmopts {
          runConfigFilePath = "./src/functionalTest/resources/Main.xml"
          vmOption = "jwtToken"
        }
                    
        println "File output = ${output}"
        """;

        return buildGradle;
  }
}
