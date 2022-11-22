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
import java.util.Arrays;
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

    Map<String, String> vmMapping = Map.of("jwtToken", "22", "dbPassword", "monkey");

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
}
