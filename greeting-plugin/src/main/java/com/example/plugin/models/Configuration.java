package com.example.plugin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Configuration implements Serializable {
  @XmlAttribute
  private String name;
  @XmlAttribute
  private String factoryName;
  @XmlAttribute
  private boolean nameIsGenerated;
  @XmlElement(name = "option")
  private List<Option> options;
  @XmlElement
  private Module module;

  public void setVmOption(final String vmOption, final String value) {
    Option foundVmOptionOrNew = options.stream()
                                    .filter(option -> "VM_PARAMETERS".equals(option.getName()))
                                    .findFirst()
                                    .orElse(new Option());

    System.out.println("Found option is :: " + foundVmOptionOrNew.getValue());

    String vmOptionList = foundVmOptionOrNew.getValue();
    int startIndexOfOption = vmOptionList.indexOf(vmOption);

    String updatedVmOption;

    if (startIndexOfOption == -1) {
      updatedVmOption = vmOptionList + " -D" + vmOption + "=" + value;
    } else {

      int startIndex = startIndexOfOption + vmOption.length();
      int endIndex = vmOptionList.indexOf(" ", startIndex) == -1 ? vmOptionList.length() : vmOptionList.indexOf(" ", startIndex);

      System.out.println("Start index of " + vmOption + " = " + startIndex);
      System.out.println("End index = " + endIndex);

      String optionValue = vmOptionList.substring(startIndex, endIndex);
      System.out.println("Option value = " + optionValue);

      updatedVmOption = vmOptionList.replace(optionValue, "=" + value);
    }

    foundVmOptionOrNew.setValue(updatedVmOption);

    System.out.println("Config is now = " + this);
  }
}
