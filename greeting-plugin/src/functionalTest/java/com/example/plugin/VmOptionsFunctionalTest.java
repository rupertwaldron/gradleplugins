package com.example.plugin;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
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
public class VmOptionsFunctionalTest {

  private static final String APPLICATION_JSON = "application/json";
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  @BeforeEach
  public void setupWireMock() {
    stubFor(post(WireMock.urlEqualTo("/api/jwt"))
                .withBasicAuth(USERNAME, PASSWORD)
                .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", APPLICATION_JSON)
                                .withBody("dummy-token")));
  }

  @Test
  public void canUpdateOneOption() throws IOException {

    createTestConfigFileWithVmParams("-DjwtToken=44.61098798956047 -DdbPassword=bob", "TestConfig.xml");

    // Setup the test build
    BuildResult result = getBuildResult("jwtToken", "./src/functionalTest/resources/TestConfig.xml");

    // Verify the result
    assertTrue(result.getOutput().contains("-DjwtToken=dummy-token"));
  }

  @Test
  public void canUpdateMoreThanOneOption() throws IOException {

    createTestConfigFileWithVmParams("-DjwtToken=44.61098798956047 -DdbPassword=bob", "TestConfig.xml");

    // Setup the test build
    BuildResult result = getBuildResult("jwtToken,dbPassword", "./src/functionalTest/resources/TestConfig.xml");

    // Verify the result
    assertTrue(result.getOutput().contains("-DjwtToken=dummy-token"));
    assertTrue(result.getOutput().contains("-DdbPassword=monkey"));
  }

  @Test
  public void canAddOptionWhenMissingFromFile() throws IOException {

    createTestConfigFileWithVmParams("-DdbPassword=bob", "TestConfig.xml");


    // Setup the test build
    BuildResult result = getBuildResult("jwtToken,dbPassword", "./src/functionalTest/resources/TestConfig.xml");

    // Verify the result
    assertTrue(result.getOutput().contains("-DjwtToken=dummy-token"));
    assertTrue(result.getOutput().contains("-DdbPassword=monkey"));
  }

  @Test
  public void canUpdateMultipleFilesIfArgIsDirectory() throws IOException {

    createTestConfigFileWithVmParams("-DdbPassword=bob", "TestMulti1.xml");
    createTestConfigFileWithVmParams("-DjwtToken=44.61098798956047 -DdbPassword=bob", "TestMulti2.xml");

    // Setup the test build
    BuildResult result = getBuildResult("jwtToken,dbPassword", "./src/functionalTest/resources");

    // Verify the result
    assertTrue(result.getOutput().contains("-DjwtToken=dummy-token"));
    assertTrue(result.getOutput().contains("-DdbPassword=monkey"));
  }

  private BuildResult getBuildResult(final String jwtToken, final String filePath) throws IOException {
    File projectDir = new File("build/functionalTest");
    Files.createDirectories(projectDir.toPath());
    writeString(new File(projectDir, "settings.gradle"), "");

    writeString(new File(projectDir, "build.gradle"), generateGradleForVmOptions(jwtToken, filePath));

    // Run the build
    return GradleRunner.create()
               .forwardOutput()
               .withPluginClasspath()
               .withArguments("vmopts")
               .withProjectDir(projectDir)
               .build();
  }

  private void writeString(File file, String string) throws IOException {
    try (Writer writer = new FileWriter(file)) {
      writer.write(string);
    }
  }


  private String generateGradleForVmOptions(final String vmOption, final String filePath) {

    String buildGradle = """
        plugins {
            id('com.example.greeting')
        }
                    
        def output = vmopts {
          runConfigFilePath = "%s"
          vmOption = "%s"
        }
                    
        println "File output = ${output}"
        """;

    return String.format(buildGradle, filePath, vmOption);
  }

  private void createTestConfigFileWithVmParams(String paramList, String fileName) {
    String fileContents = """
        <component name="ProjectRunConfigurationManager">
          <configuration default="false" name="Main" type="SpringBootApplicationConfigurationType" factoryName="Spring Boot" nameIsGenerated="true">
            <option name="ACTIVE_PROFILES" />
            <module name="UsePlugin.main" />
            <option name="SPRING_BOOT_MAIN_CLASS" value="com.ruppyrup.Main" />
            <option name="VM_PARAMETERS" value="%s" />
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

    String contentsWithParams = String.format(fileContents, paramList);

    witeContentsToFile(contentsWithParams, fileName);
  }

  private static void witeContentsToFile(final String fileContents, final String fileName) {
    try (var fileWriter = new FileWriter("./src/functionalTest/resources/" + fileName)) {
      fileWriter.write(fileContents);
    } catch (IOException iox) {
      System.out.println("Exception io = " + iox.getMessage());
    }
  }
}
