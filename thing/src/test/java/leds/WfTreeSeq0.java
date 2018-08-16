package leds;

import com.dxman.execution.*;
import static leds.Config.*;

/**
 * @author Damian Arellanes
 */
public class WfTreeSeq0 extends DXManWfTreeSeq {

  public WfTreeSeq0(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public void design() {  
        
    DXManWfTreeInv on1 = new DXManWfTreeInv("on", CONNECTOR_CONFIGS.get("Led0").getUri());
    DXManWfTreeInv off1 = new DXManWfTreeInv("off", CONNECTOR_CONFIGS.get("Led0").getUri());
    DXManWfTreeInv on2 = new DXManWfTreeInv("on", CONNECTOR_CONFIGS.get("Led1").getUri());
    DXManWfTreeInv off2 = new DXManWfTreeInv("off", CONNECTOR_CONFIGS.get("Led1").getUri());
    
    composeWf(on1, 0,2,4);
    composeWf(off1, 1,3);
    /*composeWf(on1,1,4,8,10,12,14);
    composeWf(on2, 0,5);
    composeWf(off1, 2,6,9,11,13);
    composeWf(off2, 3,7);*/
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
