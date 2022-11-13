package com.example.plugin.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class JwtTask extends DefaultTask {

  @Input
  public abstract Property<String> getJwtPath();

  @TaskAction
  public String goAndFetchJwt() {

    StringBuilder result = new StringBuilder();
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
                              .uri(URI.create(getJwtPath().get()))
                              .build();
    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(result::append)
        .join();

    return "Returned JWT -> " + result;
  }

}
