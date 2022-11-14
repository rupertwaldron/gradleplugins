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

@WireMockTest(httpPort = 9000)
public class GreetingPluginFunctionalTest {

  private static final String APPLICATION_JSON = "application/json";

  @Test
  public void canRunTask() throws IOException {

    stubFor(post(WireMock.urlEqualTo("/api/jwt"))
                .withBasicAuth("bob", "monkey")
                .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", APPLICATION_JSON)
                                .withBody("dummy token")));

    // Setup the test build
    File projectDir = new File("build/functionalTest");
    Files.createDirectories(projectDir.toPath());
    writeString(new File(projectDir, "settings.gradle"), "");

    String buildGradle = """
        plugins {
            id('com.example.greeting')
        }
                    
        def output = sum {
            num1 = 1
            num2 = 2
        }.calculate()
                    
        def output2 = sub {
            num1 = 1
            num2 = 2
        }.calculate()
                    
        println "1 + 2 = ${output}"
        println "1 - 2 = ${output2}"
                    
        def jwtToken = jwt {
             jwtPath = "http://localhost:9000/api/jwt"
             username = "bob"
             password = "monkey"
        }.goAndFetchJwt()
                    
        println "jwt token = ${jwtToken}"
        """;

    writeString(new File(projectDir, "build.gradle"), buildGradle);

    // Run the build
    BuildResult result = GradleRunner.create()
                             .forwardOutput()
                             .withPluginClasspath()
//            .withArguments("sum")
                             .withProjectDir(projectDir)
                             .build();

    // Verify the result
    assertTrue(result.getOutput().contains("1 + 2 = 3"));
    assertTrue(result.getOutput().contains("1 - 2 = -1"));
    assertTrue(result.getOutput().contains("dummy token"));
  }

  private void writeString(File file, String string) throws IOException {
    try (Writer writer = new FileWriter(file)) {
      writer.write(string);
    }
  }
}
