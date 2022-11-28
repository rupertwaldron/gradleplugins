package com.example.plugin.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.util.Map;

public abstract class QaTask extends DefaultTask {
  @Input
  public abstract Property<Integer> getNum1();

  @Input
  public abstract Property<Integer> getNum2();


  @TaskAction
  public int retreiveParams() {
    int result = getNum1().get() + getNum2().get();
    System.out.println("Calculating the result... " + result);
    return result;
  }
}
