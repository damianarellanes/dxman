package test;

import com.dxman.deployment.cli.DXManWorkflowTreeEditor;
import com.dxman.design.data.DXManDataChannelPoint;
import com.dxman.execution.*;

/**
 * @author Damian Arellanes
 */
public class WfTreeTest extends DXManWorkflowTreeEditor {

  public WfTreeTest(DXManWorkflowTree workflowTree, String wfId) {
    super(workflowTree, wfId);
  }

  @Override
  public void designControl() {

    // TODO the workflow tree can also include invocation nodes so the argument should be only the invocationNodeId
    
    // CustomerService, LPB.createRecord
    customiseOrder("7d2d1bfd-0cd3-4deb-8789-77e79ee4b17c", "60c04503-a393-43ad-ae0d-8a21667c4841", 0); 
    // CustomerService, SenderService
    customiseOrder("7d2d1bfd-0cd3-4deb-8789-77e79ee4b17c", "18f28652-0d0f-41c8-9196-f722eaa81b85", 1);    
    
    // SenderService, PostService
    customiseOrder("18f28652-0d0f-41c8-9196-f722eaa81b85", "0002eded-5b85-4b07-b4d8-ca0940264b19", 0);
    // SenderService, EmailService.sendWelcEmail
    customiseOrder("18f28652-0d0f-41c8-9196-f722eaa81b85", "295040c7-b796-4acd-9074-4de6ce48bf2c", 1);
    // PostService, Courier1.sendWelcStd
    customiseOrder("0002eded-5b85-4b07-b4d8-ca0940264b19", "30440a59-c486-44fe-9f21-3ae4fc1f8c5d", 0);
    // PostService, Courier2.sendWelcFast
    customiseOrder("0002eded-5b85-4b07-b4d8-ca0940264b19", "3f5a55c5-2675-4e0f-8385-9cf22abf3d24", 1);
  }
  
  @Override
  public void designData() {
    
    /*// SEQ1.sendWelcStd.addr --> SEQ1.addr 
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
    addDataChannel("SEQ1", origin2, destination2);*/
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
        
    wfInputs.put("9879eff5-d765-4788-823a-883e2378b453", "Damian Arellanes"); // SEQ3.createRecord.name
    wfInputs.put("dbea014f-510b-4820-8cc7-2819736e43b1", "Damian Arellanes"); // SEQ3.sendWelcStd.name
    wfInputs.put("1ad3c5d0-a202-48a8-8951-37f099574d08", "Damian Arellanes"); // SEQ3.sendWelcFast.name
    
    wfInputs.put("028edfef-5a40-4923-ba14-54c00fed63aa", "Oxford St"); // SEQ3.createRecord.addr
    wfInputs.put("6feffbce-bc3c-416f-b48a-23f2762ef4aa", "Oxford St"); // SEQ3.sendWelcStd.addr
    wfInputs.put("33cfd0ad-d8a0-4ed8-be0d-652e5674aca3", "Oxford St"); // SEQ3.sendWelcFast.addr
    
    wfInputs.put("b333186e-bfcf-48f4-a209-931fcd3d4117", "damian.arellanes@gmail.com"); // SEQ3.createRecord.email
    wfInputs.put("2fb83ec4-4b25-42b4-9283-d895afdfd0c4", "damian.arellanes@gmail.com"); // SEQ3.sendWelcEmail.email
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    
    DXManWfOutputs wfOutputs = new DXManWfOutputs();    
    wfOutputs.add("95ca5d9f-f7ab-4da1-9fb6-2027c353a871"); // SEQ3.sendWelcStd.res
    wfOutputs.add("714983d0-8620-4234-9d46-85a50b4628e4"); // SEQ3.sendWelcFast.res
    wfOutputs.add("59459f65-d53b-40e1-b760-67462d230091"); // SEQ3.createRecord.id
    //wfOutputs.add("SEQ3.sendWelcEmail.res");
    
    return wfOutputs;
  }
    
}
