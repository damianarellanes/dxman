package leds;

import com.dxman.execution.*;
import static leds.Config.*;

/**
 * @author Damian Arellanes
 */
public class WfTreeSeq0 extends DXManWorkflowTreeSeq {

  public WfTreeSeq0(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public void design() {  
        
    DXManWorkflowTreeInv on1 = new DXManWorkflowTreeInv("on", CONNECTOR_CONFIGS.get("Led0").getUri());
    DXManWorkflowTreeInv off1 = new DXManWorkflowTreeInv("off", CONNECTOR_CONFIGS.get("Led0").getUri());
    DXManWorkflowTreeInv on2 = new DXManWorkflowTreeInv("on", CONNECTOR_CONFIGS.get("Led1").getUri());
    DXManWorkflowTreeInv off2 = new DXManWorkflowTreeInv("off", CONNECTOR_CONFIGS.get("Led1").getUri());
    
    /*composeWf(on1, 0,2,4);
    composeWf(off1, 1,3);*/
    composeWf(on1,1,4,8,10,12,14);
    composeWf(on2, 0,5);
    composeWf(off1, 2,6,9,11,13);
    composeWf(off2, 3,7);
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
