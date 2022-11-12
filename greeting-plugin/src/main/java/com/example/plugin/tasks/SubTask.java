package com.example.plugin.tasks;

import com.example.plugin.calculators.Calc;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class SubTask extends DefaultTask {

  private static int staticResult;
  @Input
  public abstract Property<Integer> getNum1();

  @Input
  public abstract Property<Integer> getNum2();


  @TaskAction
  public int calculate() {
    int result = Calc.SUB.calculate(getNum1().get(), getNum2().get());
    staticResult = result;
    System.out.println("Calculating the result... " + result);
    return result;
  }

  public static int getResult() {
    return staticResult;
  }

}
