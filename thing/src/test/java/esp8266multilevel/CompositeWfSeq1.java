package esp8266multilevel;

import com.dxman.execution.wttree.DXManWfOutputs;
import com.dxman.execution.wttree.DXManWfInputs;


/**
 * @author Damian Arellanes
 */
public class CompositeWfSeq1 extends DXManWorkflowTreeSeq {
  
  private final String SEQ0_CONNECTOR = "SEQ0";
  private final String CALC1_CONNECTOR = "Calculator1";
  private final String N1_ID_1 = "738a7c44-e3e5-4694-9f88-11915df45576";
  private final String N1_VALUE_1 = "4";
  private final String N2_ID_1 = "7efba417-2769-45d7-8e71-18fa37eb6aba";
  private final String N2_VALUE_1 = "6";  
  private final String RESULT_ID_1 = "d8eda9b4-35ca-44a8-a4e3-b9a17086702c";
  
  public CompositeWfSeq1(String id, String uri) {
    super(id, uri);
  }
  
  @Override
  public void design() {  
        
    // Defines the subnodes for the workflow tree
    DXManWorkflowTreeInv multiply1 = new DXManWorkflowTreeInv(
      "multiply", "coap://192.168.0.5:5683/" + CALC1_CONNECTOR            
    );
    CompositeWfSeq0 seq0 = new CompositeWfSeq0(
      "seq0", "coap://192.168.0.5:5683/" + SEQ0_CONNECTOR
    );
    
    composeWf(multiply1, 1);
    composeWf(seq0, 0,2,3,4);
  }
  
  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
    wfInputs.put(N1_ID_1, N1_VALUE_1);
    wfInputs.put(N2_ID_1, N2_VALUE_1);
    return wfInputs;
  }
  
  @Override
  public DXManWfOutputs getOutputs() {
    DXManWfOutputs wfOutputs = new DXManWfOutputs();
    wfOutputs.add(RESULT_ID_1);
    return wfOutputs;
  }    
}
