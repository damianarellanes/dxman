package leds;

import com.dxman.execution.*;
import static leds.Config.CONNECTOR_CONFIGS;

/**
 * @author Damian Arellanes
 */
public class WfTreeSeq1 extends DXManWfTreeSeq {

  public WfTreeSeq1(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public void design() {
    
    WfTreeSeq0 seq0 = new WfTreeSeq0("seq0", CONNECTOR_CONFIGS.get("SEQ0").getUri());
    WfTreePar0 par0 = new WfTreePar0("par0", CONNECTOR_CONFIGS.get("PAR0").getUri());
    
    composeWf(seq0, 0);
    composeWf(par0, 1);
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
