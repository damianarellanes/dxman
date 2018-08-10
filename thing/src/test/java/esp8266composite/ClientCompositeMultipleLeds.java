package esp8266composite;

import com.dxman.execution.*;
import java.util.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class ClientCompositeMultipleLeds {
  
  private static final String LED1_CONNECTOR = "LedService1";  
  private static final String LED2_CONNECTOR = "LedService2";  
  private static final String LED3_CONNECTOR = "LedService3";
  private static final String SEQ_CONNECTOR = "MyComposite";
  
  public static DXManWfSpec simpleWf() throws JSONException {  
        
    // Defines the subnodes for the workflow tree
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
    DXManWfInvocation on3 = new DXManWfInvocation(
      "on", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );
    DXManWfInvocation off3 = new DXManWfInvocation(
      "off", "coap://192.168.0.5:5683/" + LED3_CONNECTOR
    );
    
    // Defines a sequence
    DXManWfSequencer seq0 = new DXManWfSequencer(
      "seq0", "coap://192.168.0.5:5683/" + SEQ_CONNECTOR
    );    
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(off1, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(0,1,2,3,4,5)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(on1, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(6,7)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(off2, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(8,9,10,11,12,21)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(on3, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(13,14,15,16,17,18,19,20)))
      )
    );
    seq0.finishSequence();

    return new DXManWfSpec("wf1", seq0);
  }
  
  public static void main(String[] args) throws JSONException {
    
    DXManWorkflowManager wfManager = new DXManWorkflowManager();
    DXManWorkflowData wfInputs = new DXManWorkflowData();
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    wfManager.executeWorkflow(simpleWf(), wfInputs, wfOutputs);
  }    
}
