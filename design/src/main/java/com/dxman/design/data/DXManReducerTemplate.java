package com.dxman.design.data;

/**
 * @author Damian Arellanes
 */
public class DXManReducerTemplate extends DXManDataProcessorTemplate {

  public DXManReducerTemplate(String name, String processorPath, 
    DXManDataProcessorLang processorLanguage) {
    super(name, processorPath, processorLanguage, DXManDataEntityType.REDUCER);
  }
}
