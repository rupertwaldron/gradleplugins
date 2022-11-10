package com.example.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GreetingPlugin implements Plugin<Project> {
    public void apply(Project project) {

        GreetingPluginExtension extension = project.getExtensions()
                                                       .create("greet", GreetingPluginExtension.class);

        project.task("greet").doLast(task -> {
            int sum = extension.getNum1() + extension.getNum2();

            System.out.println("Hello, " + extension.getNum1() + " + " + extension.getNum2() + " = " + sum);
        });
    }
}
