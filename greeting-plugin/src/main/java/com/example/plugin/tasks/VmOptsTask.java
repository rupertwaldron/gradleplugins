package com.example.plugin.tasks;

import com.example.plugin.models.Component;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public abstract class VmOptsTask extends DefaultTask {

  @Input
  public abstract Property<String> getRunConfigFilePath();

  @Input
  public abstract Property<String> getVmOption();

  @TaskAction
  public void updateRunConfig() throws IOException {

    String jwtToken = "22";

    Component component = null;

    try {
      var file = new File(getRunConfigFilePath().get());
      JAXBContext jaxbContext = JAXBContext.newInstance(Component.class);

      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      component = (Component) jaxbUnmarshaller.unmarshal(file);


      System.out.println("Component = " + component);

      Optional.ofNullable(component)
          .ifPresent(comp -> comp.setVmOption(getVmOption().get(), jwtToken));

      System.out.println("Component = " + component);

      Marshaller marshaller = jaxbContext.createMarshaller();

      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      marshaller.marshal(component, file);


    } catch (Exception ex) {
      System.out.println("Exception caught = " + ex.getMessage());
    }

//    component.getOptions().forEach(System.out::println);


//<component name="ProjectRunConfigurationManager">
//  <configuration default="false" name="Main" type="SpringBootApplicationConfigurationType" factoryName="Spring Boot" nameIsGenerated="true">
//    <option name="ACTIVE_PROFILES" />
//    <module name="UsePlugin.main" />
//    <option name="SPRING_BOOT_MAIN_CLASS" value="com.ruppyrup.Main" />
//    <option name="VM_PARAMETERS" value="-DjwtToken=44.61098798956047 -DdbPassword=bob" />
//    <extension name="coverage">
//      <pattern>
//        <option name="PATTERN" value="com.ruppyrup.*" />
//        <option name="ENABLED" value="true" />
//      </pattern>
//    </extension>
//    <method v="2">
//      <option name="Make" enabled="true" />
//    </method>
//  </configuration>
//</component>

//    StringBuilder fileContentSB = new StringBuilder();
//
//    try (var fileReader = new FileReader(getRunConfigFilePath().get())) {
//      int ch;
//      while ((ch=fileReader.read())!=-1)
//        fileContentSB.append((char)ch);
//    } catch (FileNotFoundException e) {
//      System.out.println("Couldn't find file :: " + e.getMessage());
//    }
//
//    String fileContents = fileContentSB.toString();
//
//    String modifiedFileContents = modifyVmOption(fileContents, getVmOption().get(), jwtToken);
//    System.out.println(modifiedFileContents);
//
//    try (var fileWriter = new FileWriter("./src/functionalTest/resources/bob.xml")) {
//      fileWriter.write(modifiedFileContents);
//    } catch (IOException iox) {
//      System.out.println("Exception io = " + iox.getMessage());
//    }


  }


}
