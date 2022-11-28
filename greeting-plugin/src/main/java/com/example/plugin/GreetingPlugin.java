package com.example.plugin;

import com.example.plugin.tasks.*;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GreetingPlugin implements Plugin<Project> {
  public void apply(Project project) {

    project.getTasks().create("sum", SumTask.class).setGroup("calculation");
    project.getTasks().create("sub", SubTask.class).setGroup("calculation");
    project.getTasks().create("jwt", JwtTask.class).setGroup("jwttasks");
    project.getTasks().create("vmopts", VmOptsTask.class).setGroup("vmoptstasks");
    project.getTasks().create("qa", QaTask.class).setGroup("calculation");

  }
}
