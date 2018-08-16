package leds;

import com.dxman.execution.*;
import static leds.Config.*;

/**
 * @author Damian Arellanes
 */
public class WfTreeSeq0 extends DXManWfTreeSeq {

  public WfTreeSeq0(String id, String uri, 
    DXManWfTree... subWorkflows) {
    super(id, uri, subWorkflows);
  }
  
  @Override
  public void design() {  
        
    DXManWfInvocation on1 = new DXManWfInvocation("on", CONNECTOR_CONFIGS.get("Led0").getUri());
    DXManWfInvocation off1 = new DXManWfInvocation("off", CONNECTOR_CONFIGS.get("Led0").getUri());
    DXManWfInvocation on2 = new DXManWfInvocation("on", CONNECTOR_CONFIGS.get("Led1").getUri());
    DXManWfInvocation off2 = new DXManWfInvocation("off", CONNECTOR_CONFIGS.get("Led1").getUri());
    
    composeWf(on1,1,4,8,10,12,14);
    composeWf(on2, 0,5);
    composeWf(off1, 2,6,9,11,13);
    composeWf(off2, 3,7);
  }
  
  @Override
  public DXManWorkflowInputs getInputs() {
    return new DXManWorkflowInputs();    
  }
  
  @Override
  public DXManWorkflowOutputs getOutputs() {
    return new DXManWorkflowOutputs();
  }
}
