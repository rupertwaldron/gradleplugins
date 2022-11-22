package com.example.plugin;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VmOptionsFunctionalTest {

  @Test
  public void canUpdateOneOption() throws IOException {

    createTestConfigFileWithVmParams("-DjwtToken=44.61098798956047 -DdbPassword=bob");

    // Setup the test build
    BuildResult result = getBuildResult("jwtToken");

    // Verify the result
    assertTrue(result.getOutput().contains("-DjwtToken=22"));
  }

  @Test
  public void canUpdateMoreThanOneOption() throws IOException {

    createTestConfigFileWithVmParams("-DjwtToken=44.61098798956047 -DdbPassword=bob");

    // Setup the test build
    BuildResult result = getBuildResult("jwtToken,dbPassword");

    // Verify the result
    assertTrue(result.getOutput().contains("-DjwtToken=22"));
    assertTrue(result.getOutput().contains("-DdbPassword=monkey"));
  }

  @Test
  public void canAddOptionWhenMissingFromFile() throws IOException {

    createTestConfigFileWithVmParams("-DdbPassword=bob");

    // Setup the test build
    BuildResult result = getBuildResult("jwtToken,dbPassword");

    // Verify the result
    assertTrue(result.getOutput().contains("-DjwtToken=22"));
    assertTrue(result.getOutput().contains("-DdbPassword=monkey"));
  }

  private BuildResult getBuildResult(final String jwtToken) throws IOException {
    File projectDir = new File("build/functionalTest");
    Files.createDirectories(projectDir.toPath());
    writeString(new File(projectDir, "settings.gradle"), "");

    writeString(new File(projectDir, "build.gradle"), generateGradleForVmOptions(jwtToken));

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


  private String generateGradleForVmOptions(final String vmOption) {

    String buildGradle = """
        plugins {
            id('com.example.greeting')
        }
                    
        def output = vmopts {
          runConfigFilePath = "./src/functionalTest/resources/TestConfig.xml"
          vmOption = "%s"
        }
                    
        println "File output = ${output}"
        """;

    return String.format(buildGradle, vmOption);
  }

  private void createTestConfigFileWithVmParams(String paramList) {
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

    witeContentsToFile(contentsWithParams);
  }

  private static void witeContentsToFile(final String fileContents) {
    try (var fileWriter = new FileWriter("./src/functionalTest/resources/TestConfig.xml")) {
      fileWriter.write(fileContents);
    } catch (IOException iox) {
      System.out.println("Exception io = " + iox.getMessage());
    }
  }
}
