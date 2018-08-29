package com.dxman.design.connectors.composition;

import com.dxman.design.connectors.common.DXManConnectorTemplate;
import com.dxman.design.connectors.common.DXManConnectorType;

/**
 * @author Damian Arellanes
 */
public class DXManSelectorTemplate extends DXManConnectorTemplate {
    
  public DXManSelectorTemplate(String name) {
    super(name, DXManConnectorType.SELECTOR);
  }

  @Override
  public String toString() {
    return "Selector " + getName() +" inputs: " + getInputs().values() ;
  }
}
