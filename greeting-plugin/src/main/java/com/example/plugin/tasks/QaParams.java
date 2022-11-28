package com.example.plugin.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.util.Map;

public abstract class QaParams extends DefaultTask {

  @TaskAction
  public Map<String, String> getQaParams() {
    return Map.of("propA", "43", "propB", "monkey");
  }

}
