package esp8266multilevel;

import com.dxman.execution.*;
import java.util.*;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class CompositeWfSeq2 extends DXManWfTreeSeq {
  
  public CompositeWfSeq2(String id, String uri, DXManWfTree... subWorkflows) {
    super(id, uri, subWorkflows);
  }
  
  @Override
  public void design() {  
        
    // Defines a sequence using sub-workflows
    composeWf("seq1", 0);
    composeWf("par0", 1);
  }
  
  @Override
  public DXManWorkflowInputs getInputs() {
    DXManWorkflowInputs wfInputs = new DXManWorkflowInputs();
    /*wfInputs.putAll(CompositeWfSeq1.getInputs());
    wfInputs.putAll(CompositeWfPar0.getInputs());*/
    return wfInputs;
  }
  
  @Override
  public DXManWorkflowOutputs getOutputs() {    
    DXManWorkflowOutputs wfOutputs = new DXManWorkflowOutputs();
    /*wfOutputs.addAll(CompositeWfSeq1.getOutputs());
    wfOutputs.addAll(CompositeWfPar0.getOutputs());*/
    return wfOutputs;
  }    
}
