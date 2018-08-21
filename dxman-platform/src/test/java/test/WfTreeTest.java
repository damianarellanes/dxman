package test;

import com.dxman.execution.DXManWorkflowTree;
import com.dxman.execution.DXManWfInputs;
import com.dxman.execution.DXManWfOutputs;

/**
 * @author Damian Arellanes
 */
public class WfTreeTest extends DXManWorkflowTree {

  @Override
  public void design() {
    
    customiseOrder("SEQ3", "createRecord", 0);
    customiseOrder("SEQ3", "SEQ2", 1);    
    customiseOrder("SEQ2", "SEQ1", 0);
    customiseOrder("SEQ2", "sendWelcEmail", 1);
    customiseOrder("SEQ1", "sendWelcStd", 0);
    customiseOrder("SEQ1", "sendWelcFast", 1);
    
    //addDataChannel("Post.sendWelcStd.addr", "Courier1.sendWelcStd.addr");
  }

  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
    wfInputs.put("SEQ3.createRecord.name", "Damian Arellanes");
    wfInputs.put("SEQ3.sendWelcStd.name", "Damian Arellanes");
    wfInputs.put("SEQ3.sendWelcFast.name", "Damian Arellanes");
    
    wfInputs.put("SEQ3.createRecord.addr", "Oxford St");
    wfInputs.put("SEQ3.sendWelcStd.addr", "Oxford St");
    wfInputs.put("SEQ3.sendWelcFast.addr", "Oxford St");
    
    wfInputs.put("SEQ3.createRecord.email", "damian.arellanes@gmail.com");
    wfInputs.put("SEQ3.sendWelcEmail.email", "damian.arellanes@gmail.com");
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    DXManWfOutputs wfOutputs = new DXManWfOutputs();    
    wfOutputs.add("SEQ3.sendWelcStd.res");
    wfOutputs.add("SEQ3.sendWelcFast.res");
    //wfOutputs.add("SEQ3.sendWelcEmail.res");
    return wfOutputs;
  }
    
}
