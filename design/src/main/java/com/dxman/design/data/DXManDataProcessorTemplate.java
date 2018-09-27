package com.dxman.design.data;

import java.util.*;

/**
 * @author Damian Arellanes
 */
public class DXManDataProcessorTemplate extends DXManDataEntityTemplate {
    
  private String processorPath;
  private DXManDataProcessorLang processorLanguage;
  
  private final List<String> writerIds = new ArrayList<>();

  public DXManDataProcessorTemplate() {}
  
  public DXManDataProcessorTemplate(String name, String processorPath, 
    DXManDataProcessorLang processorLanguage, DXManDataEntityType type) {
    super(name, type);
    this.processorPath = processorPath;
    this.processorLanguage = processorLanguage;
  }
  
  public void addWriter(String writerId) {
    getWriterIds().add(writerId);
  }

  public String getProcessorPath() { return processorPath; }
  public void setProcessorPath(String processorPath) {
    this.processorPath = processorPath;
  }
  public List<String> getWriterIds() { return writerIds; }

  public DXManDataProcessorLang getProcessorLanguage() { return processorLanguage; }
  public void setProcessorLanguage(DXManDataProcessorLang processorLanguage) {
    this.processorLanguage = processorLanguage;
  }
}
