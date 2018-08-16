package esp8266multilevel;

import com.dxman.execution.DXManWfInvocation;
import com.dxman.execution.DXManWfTree;
import com.dxman.execution.DXManWfTreeSeq;
import com.dxman.execution.DXManWfInputs;
import com.dxman.execution.DXManWfOutputs;
import com.dxman.execution.DXManWfTreeInv;

/**
 * @author Damian Arellanes
 */
public class CompositeWfSeq0 extends DXManWfTreeSeq {
  
  private final String LED1_CONNECTOR = "LedService1";
  private final String LED2_CONNECTOR = "LedService2";

  public CompositeWfSeq0(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public void design() {
    
    DXManWfTreeInv on1 = new DXManWfTreeInv(
      "on", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfTreeInv off1 = new DXManWfTreeInv(
      "off", "coap://192.168.0.5:5683/" + LED1_CONNECTOR
    );
    DXManWfTreeInv on2 = new DXManWfTreeInv(
      "on", "coap://192.168.0.5:5683/" + LED2_CONNECTOR
    );
    DXManWfTreeInv off2 = new DXManWfTreeInv(
      "off", "coap://192.168.0.5:5683/" + LED2_CONNECTOR
    );

    composeWf(on1, 0,1,4,5,8,9,12,13,16,17);
    composeWf(off2, 2,3,6,7,10,11,14,15);
    composeWf(off1, 18);
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
