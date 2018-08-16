package leds;

import com.dxman.execution.*;
import static leds.Config.*;

/**
 * @author Damian Arellanes
 */
public class WfTreePar0 extends DXManWfTreePar {

  public WfTreePar0(String id, String uri) {    
    super(id, uri);
  }
    
  @Override
  public void design() {  
        
    DXManWfTreeInv on2 = new DXManWfTreeInv("on", CONNECTOR_CONFIGS.get("Led2").getUri());
    DXManWfTreeInv off2 = new DXManWfTreeInv("off", CONNECTOR_CONFIGS.get("Led2").getUri());
    DXManWfTreeInv on3 = new DXManWfTreeInv("on", CONNECTOR_CONFIGS.get("Led3").getUri());
    DXManWfTreeInv off3 = new DXManWfTreeInv("off", CONNECTOR_CONFIGS.get("Led3").getUri());
    
    composeWf(on2, 2);
    composeWf(off2, 1);
    composeWf(on3, 1);
  }
  
  @Override
  public DXManWfInputs getInputs() {
    return new DXManWfInputs();
  }
  
  @Override
  public DXManWfOutputs getOutputs() {
    return new DXManWfOutputs();
  }
}
