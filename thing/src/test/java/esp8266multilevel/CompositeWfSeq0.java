package esp8266multilevel;

import com.dxman.execution.DXManWfInvocation;
import com.dxman.execution.DXManWfNodeMapper;
import com.dxman.execution.DXManWfSequencer;
import com.dxman.execution.DXManWfSequencerCustom;
import com.dxman.execution.DXManWfSpec;
import com.dxman.execution.DXManWorkflowInputs;
import com.dxman.execution.DXManWorkflowOutputs;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class CompositeWfSeq0 {
  
  private static final String SEQ0 = "SEQ0";
  private static final String LED1_CONNECTOR = "LedService1";
  private static final String LED2_CONNECTOR = "LedService2";
  
  public static DXManWfSpec getWf() throws JSONException {  
        
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
    
    // Defines a sequence
    DXManWfSequencer seq0 = new DXManWfSequencer(
      "seq0", "coap://192.168.0.5:5683/" + SEQ0
    );    
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(on1, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(0,1,4,5,8,9,12,13,16,17)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(off2, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(2,3,6,7,10,11,14,15)))
      )
    );
    seq0.getSubnodeMappers().add(
      new DXManWfNodeMapper(off1, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(18)))
      )
    );
    seq0.finishSequence();

    return new DXManWfSpec("wf0", seq0);
  }
  
  public static DXManWorkflowInputs getInputs() {
    return new DXManWorkflowInputs();    
  }
  
  public static DXManWorkflowOutputs getOutputs() {
    return new DXManWorkflowOutputs();
  }
    
}
