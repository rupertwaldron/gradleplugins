package com.example.plugin.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public abstract class JwtTask extends DefaultTask {

  private Base64.Encoder encoder = Base64.getEncoder();

  @Input
  public abstract Property<String> getJwtPath();
  @Input
  abstract Property<String> getUsername();
  @Input
  abstract Property<String> getPassword();

  @TaskAction
  public String goAndFetchJwt() {

    StringBuilder result = new StringBuilder();
    HttpClient client = HttpClient.newHttpClient();

    String authorization = getUsername().get() + ":" + getPassword().get();

    HttpRequest request = HttpRequest.newBuilder()
                              .uri(URI.create(getJwtPath().get()))
                              .header("Content-Type", "application/json")
                              .header("Authorization", "Basic " + encoder.encodeToString(authorization.getBytes()))
                              .POST(HttpRequest.BodyPublishers.noBody())
                              .build();

    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(result::append)
        .join();

    return result.toString();
  }

}
