package com.example.plugin;

import com.example.plugin.tasks.SubTask;
import com.example.plugin.tasks.SumTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GreetingPlugin implements Plugin<Project> {
  public void apply(Project project) {

    project.getExtensions().create("calculation", GreetingPluginExtension.class);

    project.getTasks().create("sum", SumTask.class).setGroup("calculation");
    project.getTasks().create("sub", SubTask.class).setGroup("calculation");


  }
}
