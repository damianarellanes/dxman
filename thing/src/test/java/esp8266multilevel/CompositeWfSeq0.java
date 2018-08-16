package esp8266multilevel;

import com.dxman.execution.DXManWfInvocation;
import com.dxman.execution.DXManWfTree;
import com.dxman.execution.DXManWfTreeSeq;
import com.dxman.execution.DXManWorkflowInputs;
import com.dxman.execution.DXManWorkflowOutputs;

/**
 * @author Damian Arellanes
 */
public class CompositeWfSeq0 extends DXManWfTreeSeq {
  
  private final String LED1_CONNECTOR = "LedService1";
  private final String LED2_CONNECTOR = "LedService2";

  public CompositeWfSeq0(String id, String uri, DXManWfTree... subWorkflows) {
    super(id, uri, subWorkflows);
  }
  
  @Override
  public void design() {
    
    DXManWfInvocation on1 = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfInvocation off1 = new DXManWfInvocation(
      "off", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfInvocation on2 = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED2_CONNECTOR
    );
    DXManWfInvocation off2 = new DXManWfInvocation(
      "off", "coap://192.168.0.5:5683/" + LED2_CONNECTOR
    );

    composeWf(on1, 0,1,4,5,8,9,12,13,16,17);
    composeWf(off2, 2,3,6,7,10,11,14,15);
    composeWf(off1, 18);
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
