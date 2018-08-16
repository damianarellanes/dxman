package leds;

import com.dxman.execution.*;
import static leds.Config.*;

/**
 * @author Damian Arellanes
 */
public class WfTreePar0 extends DXManWfTreePar {

  public WfTreePar0(String id, String uri, 
    DXManWfTree... subWorkflows) {
    
    super(id, uri, subWorkflows);
  }
    
  @Override
  public void design() {  
        
    DXManWfInvocation on2 = new DXManWfInvocation("on", CONNECTOR_CONFIGS.get("Led2").getUri());
    DXManWfInvocation off2 = new DXManWfInvocation("off", CONNECTOR_CONFIGS.get("Led2").getUri());
    DXManWfInvocation on3 = new DXManWfInvocation("on", CONNECTOR_CONFIGS.get("Led3").getUri());
    DXManWfInvocation off3 = new DXManWfInvocation("off", CONNECTOR_CONFIGS.get("Led3").getUri());
    
    composeWf(on2, 2);
    composeWf(off2, 1);
    composeWf(on3, 1);
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
