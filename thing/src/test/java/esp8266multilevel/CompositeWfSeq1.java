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
public class CompositeWfSeq1 {
  
  private static final String SEQ1 = "SEQ1";
  private static final String SEQ0_CONNECTOR = "SEQ0";
  private static final String CALC1_CONNECTOR = "Calculator1";
  private static final String N1_ID_1 = "738a7c44-e3e5-4694-9f88-11915df45576";
  private static final String N1_VALUE_1 = "4";
  private static final String N2_ID_1 = "7efba417-2769-45d7-8e71-18fa37eb6aba";
  private static final String N2_VALUE_1 = "6";  
  private static final String RESULT_ID_1 = "d8eda9b4-35ca-44a8-a4e3-b9a17086702c";
  
  public static DXManWfSpec getWf() throws JSONException {  
        
    // Defines the subnodes for the workflow tree
    DXManWfInvocation multiply1 = new DXManWfInvocation(
      "multiply", "coap://192.168.0.5:5683/" + CALC1_CONNECTOR            
    );
    DXManWfSequencer seq0 = new DXManWfSequencer(
      "seq0", "coap://192.168.0.5:5683/" + SEQ0_CONNECTOR
    );
    
    // Defines a sequence
    DXManWfSequencer seq1 = new DXManWfSequencer(
      "seq1", "coap://192.168.0.5:5683/" + SEQ1
    );
    seq1.getSubnodeMappers().add(
      new DXManWfNodeMapper(multiply1, new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(1)))
      )
    );
    seq1.getSubnodeMappers().add(
      new DXManWfNodeMapper(CompositeWfSeq0.getWf().getFlow(), new DXManWfSequencerCustom(
        new ArrayList<>(Arrays.asList(0,2,3,4)))
      )
    );
    seq1.finishSequence();

    return new DXManWfSpec("wf1", seq1);
  }
  
  public static DXManWorkflowInputs getInputs() {
    DXManWorkflowInputs wfInputs = new DXManWorkflowInputs();
    wfInputs.put(N1_ID_1, N1_VALUE_1);
    wfInputs.put(N2_ID_1, N2_VALUE_1);
    return wfInputs;
  }
  
  public static DXManWorkflowOutputs getOutputs() {
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    wfOutputs.add(RESULT_ID_1);
    return wfOutputs;
  }
    
}
