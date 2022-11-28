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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QaParamsTest {

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
//            .withArguments("sum")
                             .withProjectDir(projectDir)
                             .build();

    // Verify the result
    assertTrue(result.getOutput().contains("prop A = 43"));
    assertTrue(result.getOutput().contains("prop B = monkey"));
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
                    
        def params = qaparams {
      
        }.retreiveParams()
                    
        println "prop A = ${params.get('propA')}"
        println "prop B = ${params.get('propB')}"
                    
        """;

        return buildGradle;
  }
}
