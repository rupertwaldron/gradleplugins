package com.example.plugin.tasks;

import com.example.plugin.models.Component;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class VmOptsTask extends DefaultTask {

  @Input
  public abstract Property<String> getRunConfigFilePath();

  @Input
  public abstract Property<String> getVmOption();

  @TaskAction
  public void updateRunConfig() throws IOException, JAXBException {


    String jwtToken = goAndFetchJwt("http://localhost:9000/api/jwt");

    Map<String, String> vmMapping = Map.of("jwtToken", jwtToken, "dbPassword", "monkey");

    JAXBContext jaxbContext = JAXBContext.newInstance(Component.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    Component component = null;

    var file = new File(getRunConfigFilePath().get());

    if (file.isDirectory()) {
      System.out.println(file + " is a directory");
      List<File> files = Arrays.asList(file.listFiles());
      files.forEach(f -> {
        System.out.println("Updating for file -> " + f);
        try {
          updateFile(vmMapping, jaxbUnmarshaller, marshaller, f);
        } catch (JAXBException e) {
          throw new RuntimeException(e);
        }
      });
    } else {
      updateFile(vmMapping, jaxbUnmarshaller, marshaller, file);
    }

  }

  private void updateFile(final Map<String, String> vmMapping, final Unmarshaller jaxbUnmarshaller, final Marshaller marshaller, final File file) throws JAXBException {
    Component component;
    component = (Component) jaxbUnmarshaller.unmarshal(file);

    System.out.println("Component = " + component);

    Component updatedComponent = Optional.ofNullable(component).orElse(new Component());

    List<String> vmOptionsToUpdate = Arrays.asList(getVmOption().get().split(","));
    System.out.println("Args = " + vmOptionsToUpdate);

    vmOptionsToUpdate.forEach(vmOption -> {
      updatedComponent.setVmOption(vmOption, vmMapping.get(vmOption));
      System.out.println("Updated Component with " + vmOption + " = " + updatedComponent);
    });


    marshaller.marshal(updatedComponent, file);
  }

  private String goAndFetchJwt(final String jwtUrl) {

    Base64.Encoder encoder = Base64.getEncoder();
    StringBuilder result = new StringBuilder();
    HttpClient client = HttpClient.newHttpClient();

    String authorization = "username:password";

    HttpRequest request = HttpRequest.newBuilder()
                              .uri(URI.create(jwtUrl))
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
