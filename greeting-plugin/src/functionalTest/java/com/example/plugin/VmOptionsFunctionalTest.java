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

    createTestConfigFile();

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

  @Test
  public void canRunTaskForMoreVmOptions() throws IOException {

    createTestConfigFile();

    // Setup the test build
    File projectDir = new File("build/functionalTest");
    Files.createDirectories(projectDir.toPath());
    writeString(new File(projectDir, "settings.gradle"), "");

    writeString(new File(projectDir, "build.gradle"), generateGradleForMultipleVmOptions());

    // Run the build
    BuildResult result = GradleRunner.create()
                             .forwardOutput()
                             .withPluginClasspath()
                             .withArguments("vmopts")
                             .withProjectDir(projectDir)
                             .build();

    // Verify the result
    assertTrue(result.getOutput().contains("-DjwtToken=22"));
    assertTrue(result.getOutput().contains("-DdbPassword=monkey"));
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
          runConfigFilePath = "./src/functionalTest/resources/TestConfig.xml"
          vmOption = "jwtToken"
        }
                    
        println "File output = ${output}"
        """;

    return buildGradle;
  }

  private String generateGradleForMultipleVmOptions() {

    String buildGradle = """
        plugins {
            id('com.example.greeting')
        }
                    
        def output = vmopts {
          runConfigFilePath = "./src/functionalTest/resources/TestConfig.xml"
          vmOption = "jwtToken,dbPassword"
        }
                    
        println "File output = ${output}"
        """;

    return buildGradle;
  }

  private void createTestConfigFile() {
    String fileContents = """
        <component name="ProjectRunConfigurationManager">
          <configuration default="false" name="Main" type="SpringBootApplicationConfigurationType" factoryName="Spring Boot" nameIsGenerated="true">
            <option name="ACTIVE_PROFILES" />
            <module name="UsePlugin.main" />
            <option name="SPRING_BOOT_MAIN_CLASS" value="com.ruppyrup.Main" />
            <option name="VM_PARAMETERS" value="-DjwtToken=44.61098798956047 -DdbPassword=bob" />
            <extension name="coverage">
              <pattern>
                <option name="PATTERN" value="com.ruppyrup.*" />
                <option name="ENABLED" value="true" />
              </pattern>
            </extension>
            <method v="2">
              <option name="Make" enabled="true" />
            </method>
          </configuration>
        </component>
        """;

    try (var fileWriter = new FileWriter("./src/functionalTest/resources/TestConfig.xml")) {
      fileWriter.write(fileContents);
    } catch (IOException iox) {
      System.out.println("Exception io = " + iox.getMessage());
    }
  }
}
