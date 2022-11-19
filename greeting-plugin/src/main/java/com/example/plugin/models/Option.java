package com.example.plugin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;

@AllArgsConstructor
@NoArgsConstructor
public class Option {
  private String name;
  private String value;

  @XmlAttribute
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute
  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
