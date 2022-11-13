package com.example.plugin;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import static org.junit.Assert.assertTrue;


public class GreetingPluginFunctionalTest {
    @Test
    public void canRunTask() throws IOException {
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
                 jwtPath = "https://ed2245ff-0869-4b36-9f82-5760a6f4ba6e.mock.pstmn.io/jwt"
            }.goAndFetchJwt()
            
            println "jwt token = ${jwtToken}"
            """;

        writeString(new File(projectDir, "build.gradle"), buildGradle);

        // Run the build
        BuildResult result = GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withArguments("sum")
            .withProjectDir(projectDir)
            .build();

        // Verify the result
        assertTrue(result.getOutput().contains("1 + 2 = 3"));
        assertTrue(result.getOutput().contains("1 - 2 = -1"));
        assertTrue(result.getOutput().contains("jwt token = Returned JWT -> hello"));
    }

    private void writeString(File file, String string) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            writer.write(string);
        }
    }
}
