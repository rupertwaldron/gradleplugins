package com.example.plugin.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class JwtTask extends DefaultTask {

  @Input
  public abstract Property<String> getJwtPath();

  @TaskAction
  public String goAndFetchJwt() {
    System.out.println("Getting jwt from " + getJwtPath().get());
    return "Returned JWT";
  }

}
