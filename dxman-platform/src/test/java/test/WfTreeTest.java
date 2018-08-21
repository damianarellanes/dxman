package test;

import com.dxman.design.data.DXManDataChannelPoint;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.DXManWorkflowTree;
import com.dxman.execution.DXManWfInputs;
import com.dxman.execution.DXManWfOutputs;

/**
 * @author Damian Arellanes
 */
public class WfTreeTest extends DXManWorkflowTree {

  public WfTreeTest(DXManCompositeServiceTemplate compositeService) {
    super(compositeService);
  }

  @Override
  public void designControl() {
    
    customiseOrder("SEQ3", "createRecord", 0);
    customiseOrder("SEQ3", "SEQ2", 1);    
    customiseOrder("SEQ2", "SEQ1", 0);
    customiseOrder("SEQ2", "sendWelcEmail", 1);
    customiseOrder("SEQ1", "sendWelcStd", 0);
    customiseOrder("SEQ1", "sendWelcFast", 1);
  }
  
  @Override
  public void designData() {
    
    // SEQ1.sendWelcStd.addr --> SEQ1.addr 
    DXManDataChannelPoint origin = new DXManDataChannelPoint(
      "SEQ1.sendWelcStd.addr", "addr", "sendWelcStd", "SEQ1"
    );
    DXManDataChannelPoint destination = new DXManDataChannelPoint(
      "SEQ1.addr", "addr", "", "SEQ1"
    );
    addDataChannel("SEQ1", origin, destination);
    
    // // SEQ1.sendWelcFast.addr --> SEQ1.addr 
    DXManDataChannelPoint origin2 = new DXManDataChannelPoint(
      "SEQ1.sendWelcFast.addr", "addr", "sendWelcFast", "SEQ1"
    );
    DXManDataChannelPoint destination2 = new DXManDataChannelPoint(
      "SEQ1.addr", "addr", "", "SEQ1"
    );
    addDataChannel("SEQ1", origin2, destination2);
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
