package com.dxman.design.data;

/**
 * @author Damian Arellanes
 */
public class DXManMapperTemplate extends DXManDataProcessorTemplate {
  
  public DXManMapperTemplate(String name, String processorPath, 
    DXManDataProcessorLang processorLanguage) {
    super(name, processorPath, processorLanguage, DXManDataEntityType.MAPPER);
  }
}
