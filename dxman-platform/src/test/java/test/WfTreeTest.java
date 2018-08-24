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
    customiseOrder("02ed043e-8747-4c28-b3ce-e7789daf5e50", "aff3ac33-6e77-4249-b3c2-f30d60e6ad99", 0); // CustomerService, LPB.createRecord
    /*customiseOrder("ebd8dac3-24b6-417b-a117-7efe6921bf89", "SEQ2", 1);    
    customiseOrder("SEQ2", "SEQ1", 0);
    customiseOrder("SEQ2", "sendWelcEmail", 1);
    customiseOrder("SEQ1", "sendWelcStd", 0);
    customiseOrder("SEQ1", "sendWelcFast", 1);*/
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
        
    wfInputs.put("d83d9bb2-94d4-4fde-83c7-316e7ce54471", "Damian Arellanes"); // SEQ3.createRecord.name
    wfInputs.put("87c2c9b1-4cb4-4968-a110-8b89b933c60d", "Damian Arellanes"); // SEQ3.sendWelcStd.name
    wfInputs.put("160366d3-c23e-4321-91b8-fa41910f1eff", "Damian Arellanes"); // SEQ3.sendWelcFast.name
    
    wfInputs.put("02154329-d88f-4f48-97e1-71c28522a9cd", "Oxford St"); // SEQ3.createRecord.addr
    wfInputs.put("c641e8b7-f5f8-444b-b5a7-5af20341dca7", "Oxford St"); // SEQ3.sendWelcStd.addr
    wfInputs.put("e9bd5bb7-0e85-47cf-8834-98b9ce7510a7", "Oxford St"); // SEQ3.sendWelcFast.addr
    
    wfInputs.put("e41ac3b2-38b5-47cb-9151-3e91063478c2", "damian.arellanes@gmail.com"); // SEQ3.createRecord.email
    wfInputs.put("7b484d9e-4b87-441b-b8dd-238b781f1dc4", "damian.arellanes@gmail.com"); // SEQ3.sendWelcEmail.email
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    
    DXManWfOutputs wfOutputs = new DXManWfOutputs();    
    wfOutputs.add("0288c64a-996c-4a10-a086-71cab1857648"); // SEQ3.sendWelcStd.res
    wfOutputs.add("47fd3da3-47df-41d2-9bcc-ceb7a32be776"); // SEQ3.sendWelcFast.res
    wfOutputs.add("aebb89f6-d464-4c08-9e94-850f795cbe45"); // SEQ3.createRecord.id
    //wfOutputs.add("SEQ3.sendWelcEmail.res");
    
    return wfOutputs;
  }
    
}
