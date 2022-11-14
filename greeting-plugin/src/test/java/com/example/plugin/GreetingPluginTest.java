package com.example.plugin;

import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.api.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class GreetingPluginTest {
    @Test
    void pluginRegistersATask() {
        // Create a test project and apply the plugin
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("com.example.greeting");

        // Verify the result
        assertNotNull(project.getTasks().findByName("sum"));
        assertNotNull(project.getTasks().findByName("sub"));
        assertNotNull(project.getTasks().findByName("jwt"));
    }
}
