package leds;

import com.dxman.execution.*;
import static leds.Config.*;

/**
 * @author Damian Arellanes
 */
public class WfTreePar0 extends DXManWorkflowTreePar {

  public WfTreePar0(String id, String uri) {    
    super(id, uri);
  }
    
  @Override
  public void design() {  
        
    DXManWorkflowTreeInv on2 = new DXManWorkflowTreeInv("on", CONNECTOR_CONFIGS.get("Led2").getUri());
    DXManWorkflowTreeInv off2 = new DXManWorkflowTreeInv("off", CONNECTOR_CONFIGS.get("Led2").getUri());
    DXManWorkflowTreeInv on3 = new DXManWorkflowTreeInv("on", CONNECTOR_CONFIGS.get("Led3").getUri());
    DXManWorkflowTreeInv off3 = new DXManWorkflowTreeInv("off", CONNECTOR_CONFIGS.get("Led3").getUri());
    
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
