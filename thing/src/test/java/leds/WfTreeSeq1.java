package leds;

import com.dxman.execution.*;

/**
 * @author Damian Arellanes
 */
public class WfTreeSeq1 extends DXManWfTreeSeq {

  public WfTreeSeq1(String id, String uri, 
    DXManWfTree... subWorkflows) {
    super(id, uri, subWorkflows);
  }
  
  @Override
  public void design() {
    
    composeWf("seq0", 0);
    composeWf("par0", 1);
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
