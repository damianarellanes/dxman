package com.dxman.deployment.data;

import com.dxman.dataspace.base.DXManDataSpace;
import com.dxman.design.data.DXManDataPipe;

/**
 * @author Damian Arellanes
 */
public class DXManDataDeployer {
    
  private final DXManDataSpace dataSpace;

  public DXManDataDeployer(DXManDataSpace dataSpace) {
    this.dataSpace = dataSpace;
  }
  
  public void deployDataPipe(DXManDataPipe dataPipe) {
    
    dataSpace.registerParameter(
      dataPipe.getParameterId(), 
      dataPipe.getValue(), 
      dataPipe.getWriterId(),
      dataPipe.getReaderIds()
    );
  }
}
