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

    String vmOptionList = foundVmOptionOrNew.getValue();
    int startIndex = vmOptionList.indexOf(vmOption) + vmOption.length();
    int endIndex = vmOptionList.indexOf(" ");

    String optionValue = vmOptionList.substring(startIndex, endIndex);
    System.out.println("Option value = " + optionValue);

    String updatedVmOption = vmOptionList.replace(optionValue, "=" + value);

    foundVmOptionOrNew.setValue(updatedVmOption);

    System.out.println("Config is now = " + this);
  }
}
