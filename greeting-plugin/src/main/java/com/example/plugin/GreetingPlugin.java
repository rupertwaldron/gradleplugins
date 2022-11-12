package com.example.plugin;

import com.example.plugin.tasks.SumTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GreetingPlugin implements Plugin<Project> {
  public void apply(Project project) {

    project.getExtensions().create("calculation", GreetingPluginExtension.class);

//        project.task("greet").doLast(task -> {
//            int sum = extension.getNum1() + extension.getNum2();
//            extension.setResult(sum);
//
//            System.out.println("Hello, " + extension.getNum1() + " + " + extension.getNum2() + " = " + sum);
//        });

    project.getTasks().create("sum", SumTask.class).setGroup("calculation");


  }
}
