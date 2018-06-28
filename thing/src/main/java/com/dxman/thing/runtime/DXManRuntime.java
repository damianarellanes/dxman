package com.dxman.thing.runtime;

import com.dxman.dataspace.base.DXManDataSpace;

/**
 * @author Damian Arellanes
 */
public class DXManRuntime {
    
  private final DXManThing thing;
  private final DXManDataSpace dataSpace;

  public DXManRuntime(DXManThing thing, DXManDataSpace dataSpace) {
    this.thing = thing;
    this.dataSpace = dataSpace;
  }
  
  public void shutdown() {
    // TODO shutdown dataspace
  }

  public DXManThing getThing() { return thing; }
  public DXManDataSpace getDataSpace() { return dataSpace; }
}
