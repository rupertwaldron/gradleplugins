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

    Component component = null;

    try {
      var file = new File(getRunConfigFilePath().get());
      JAXBContext jaxbContext = JAXBContext.newInstance(Component.class);

      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      component = (Component) jaxbUnmarshaller.unmarshal(file);


      System.out.println("Component = " + component);

      Component updatedComponent = Optional.ofNullable(component).orElse(new Component());

      List<String> vmOptionsToUpdate = Arrays.asList(getVmOption().get().split(","));
      System.out.println("Args = " + vmOptionsToUpdate);

      vmOptionsToUpdate.forEach(vmOption -> {
        updatedComponent.setVmOption(vmOption, vmMapping.get(vmOption));
        System.out.println("Updated Component with " + vmOption + " = " + updatedComponent);
      });



      Marshaller marshaller = jaxbContext.createMarshaller();

      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      marshaller.marshal(updatedComponent, file);


    } catch (Exception ex) {
      throw ex;
    }
  }
}
