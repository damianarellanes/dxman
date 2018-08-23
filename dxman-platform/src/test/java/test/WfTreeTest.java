package test;

import com.dxman.deployment.cli.DXManWorkflowTreeEditor;
import com.dxman.design.data.DXManDataChannelPoint;
import com.dxman.design.services.composite.DXManCompositeServiceTemplate;
import com.dxman.execution.DXManWorkflowTree;
import com.dxman.execution.DXManWfInputs;
import com.dxman.execution.DXManWfOutputs;

/**
 * @author Damian Arellanes
 */
public class WfTreeTest extends DXManWorkflowTreeEditor {

  public WfTreeTest(DXManWorkflowTree workflowTree, String wfId) {
    super(workflowTree, wfId);
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
      "08c0e9f2-7bc4-46ca-9716-89303584a60e"
    );
    DXManDataChannelPoint destination = new DXManDataChannelPoint(
      "SEQ1.addr"
    );
    addDataChannel("SEQ1", origin, destination);
    
    // SEQ1.sendWelcFast.addr --> SEQ1.addr 
    DXManDataChannelPoint origin2 = new DXManDataChannelPoint(
      "1a61a305-5f53-423c-b08b-aa9e6ff51f69"
    );
    DXManDataChannelPoint destination2 = new DXManDataChannelPoint(
      "SEQ1.addr"
    );
    addDataChannel("SEQ1", origin2, destination2);
  }

  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
    
    /*wfInputs.put("SEQ3.createRecord.name", "Adela Molina");
    wfInputs.put("SEQ3.sendWelcStd.name", "Adela Molina");
    wfInputs.put("SEQ3.sendWelcFast.name", "Adela Molina");
    
    wfInputs.put("SEQ3.createRecord.addr", "Mexico");
    wfInputs.put("SEQ3.sendWelcStd.addr", "Mexico");
    wfInputs.put("SEQ3.sendWelcFast.addr", "Mexico");
    
    wfInputs.put("SEQ3.createRecord.email", "adela@yahoo.com");
    wfInputs.put("SEQ3.sendWelcEmail.email", "adela@yahoo.com");*/
        
    wfInputs.put("057eb6c7-33ce-4ac9-926a-ce16545157f8", "Damian Arellanes"); // SEQ3.createRecord.name
    wfInputs.put("a4c6d896-0cab-4513-9ce9-b73a74e1171c", "Damian Arellanes"); // SEQ3.sendWelcStd.name
    wfInputs.put("bf034b58-4f9d-47e0-ad17-8901941f25a2", "Damian Arellanes"); // SEQ3.sendWelcFast.name
    
    wfInputs.put("6af4a773-86fa-427b-b5af-2c00048236c3", "Oxford St"); // SEQ3.createRecord.addr
    wfInputs.put("efca9d20-1f48-459f-85d6-e95264eac31f", "Oxford St"); // SEQ3.sendWelcStd.addr
    wfInputs.put("4bd4998c-112a-48a6-b926-85a98860714e", "Oxford St"); // SEQ3.sendWelcFast.addr
    
    wfInputs.put("be3d5537-9521-4ee4-9a1f-32d5b00f87fd", "damian.arellanes@gmail.com"); // SEQ3.createRecord.email
    wfInputs.put("1d786a62-e876-4e8e-94d0-45fc180f9d87", "damian.arellanes@gmail.com"); // SEQ3.sendWelcEmail.email
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    
    DXManWfOutputs wfOutputs = new DXManWfOutputs();    
    wfOutputs.add("fbea1e2e-b52a-4c6e-a7f1-7f4a4dafdc57"); // SEQ3.sendWelcStd.res
    wfOutputs.add("9d532a12-f337-44ba-a730-47716ce97164"); // SEQ3.sendWelcFast.res
    wfOutputs.add("ddbf2960-1569-4740-bdd0-913eeb9a97b3"); // SEQ3.createRecord.id
    //wfOutputs.add("SEQ3.sendWelcEmail.res");
    
    return wfOutputs;
  }
    
}
