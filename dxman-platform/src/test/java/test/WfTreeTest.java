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
    customiseOrder("361c2db2-570a-46ba-970c-0a38a953a8e3", "4c499646-8466-4558-a810-cdfce699ed72", 0); 
    // CustomerService, SenderService
    customiseOrder("361c2db2-570a-46ba-970c-0a38a953a8e3", "19c4f9a8-9e74-4935-bab0-4df6c70fe638", 1);    
    
    // SenderService, PostService
    customiseOrder("19c4f9a8-9e74-4935-bab0-4df6c70fe638", "deb5da24-a6c0-4bca-8c2c-804c78ae6d3b", 0);
    // SenderService, EmailService.sendWelcEmail
    customiseOrder("19c4f9a8-9e74-4935-bab0-4df6c70fe638", "96ca54ac-79f2-4905-b9b9-96d895c8e246", 1);
    // PostService, Courier1.sendWelcStd
    customiseOrder("deb5da24-a6c0-4bca-8c2c-804c78ae6d3b", "edf8367d-6ce1-4fc7-91b5-cbbab389d348", 0);
    // PostService, Courier2.sendWelcFast
    customiseOrder("deb5da24-a6c0-4bca-8c2c-804c78ae6d3b", "feb2ba20-7e5a-4436-aff9-4d5725cf5154", 1);
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
        
    wfInputs.put("4c8d974d-5c6b-4edd-8097-2cad2b60d730", "Damian Arellanes"); // SEQ3.createRecord.name
    wfInputs.put("163742ee-584c-4bd6-845e-45e068b0543b", "Damian Arellanes"); // SEQ3.sendWelcStd.name
    wfInputs.put("be1f1af4-5ccc-4348-9fe9-3e7ee165cc9b", "Damian Arellanes"); // SEQ3.sendWelcFast.name
    
    wfInputs.put("1fc8b900-40e9-4ac5-bd8f-7ce8750f4fcc", "Oxford St"); // SEQ3.createRecord.addr
    wfInputs.put("71a682e8-e394-41fa-820e-d2ff78f6dca2", "Oxford St"); // SEQ3.sendWelcStd.addr
    wfInputs.put("b8d450f7-7a0c-4d4f-bdc4-f8854d1adbf2", "Oxford St"); // SEQ3.sendWelcFast.addr
    
    wfInputs.put("f56eb7e4-74a2-4bac-9da5-d8517d88eea7", "damian.arellanes@gmail.com"); // SEQ3.createRecord.email
    wfInputs.put("7d5b8d9e-8229-4b58-89bf-457fd8506204", "damian.arellanes@gmail.com"); // SEQ3.sendWelcEmail.email
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    
    DXManWfOutputs wfOutputs = new DXManWfOutputs();    
    wfOutputs.add("701c2de8-0275-451f-8495-942da0e4a9d4"); // SEQ3.sendWelcStd.res
    wfOutputs.add("54879ac4-822c-44d1-9781-50fea718dc2f"); // SEQ3.sendWelcFast.res
    wfOutputs.add("80ee0e6c-9d24-4211-9f53-64acc3cdc5a2"); // SEQ3.createRecord.id
    //wfOutputs.add("SEQ3.sendWelcEmail.res");
    
    return wfOutputs;
  }
    
}
