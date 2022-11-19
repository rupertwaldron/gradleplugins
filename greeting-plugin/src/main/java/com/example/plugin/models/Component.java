package com.example.plugin.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
public class Component {
  private String name;
  private List<Option> options;

  @XmlAttribute
  public String getName() {
    return name;
  }

  @XmlElement
  public List<Option> getOptions() {
    return options;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setOptions(final List<Option> options) {
    this.options = options;
  }
}
