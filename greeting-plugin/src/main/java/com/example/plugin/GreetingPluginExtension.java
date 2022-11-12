package com.example.plugin;

import com.example.plugin.tasks.SumTask;

public class GreetingPluginExtension {

  public int getResult() {
    return SumTask.getResult();
  }
}
