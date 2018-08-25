package test;

import com.dxman.deployment.cli.DXManWorkflowTreeEditor;
import com.dxman.design.data.DXManDataChannelPoint;
import com.dxman.execution.*;
import com.dxman.execution.selector.DXManWfConditionOperator;

/**
 * @author Damian Arellanes
 */
public class WfTreeTest extends DXManWorkflowTreeEditor {
  
  private final String CUSTOMER = "8809aa27-dab5-4f4d-9ef2-36d69a0f75c2";
  private final String CUSTOMER_CREATE_NAME = "0b9aaeae-7b63-444a-9c94-b1aead3309c4";
  private final String CUSTOMER_CREATE_ADDR = "0bf90e7a-223d-4bad-8777-41999c862ee4";
  private final String CUSTOMER_CREATE_EMAIL = "a007aa2d-75c7-4305-a17d-d38917e5a805";
  private final String CUSTOMER_CREATE_ID = "ef66ae5d-0fc2-4f75-89a0-5d9b377b059b";
  private final String CUSTOMER_WELCEMAIL_EMAIL = "df67dfcf-8009-4848-94e4-3debcf71aea8";
  private final String CUSTOMER_STD_NAME = "de4a66a1-a3a1-4459-ad6f-80dd64b6ae7c";
  private final String CUSTOMER_STD_ADDR = "345097da-7375-49d9-a358-6bf9ea0307e2";
  private final String CUSTOMER_STD_RES = "ae056288-c634-4869-ab75-1faddfc85ea7";
  private final String CUSTOMER_FAST_NAME = "d1a7cb71-3f40-4708-bd22-6006a8c500be";
  private final String CUSTOMER_FAST_ADDR = "f052f71f-1f9f-4fe2-90f1-f0427d6e2f7c";
  private final String CUSTOMER_FAST_RES = "9f36c132-73e7-4942-8a7d-9c9f88eff5c3";
  
  private final String LBP_CREATE = "7790c9d2-73f1-48a7-8dce-5f256930dd1f";
  
  private final String SENDER = "a4df49b2-f592-4c6b-b24c-d95b8edfb665";
  
  private final String EMAIL_WELCEMAIL = "2b98dccf-aa69-4b38-ba20-7da789c065b5";
  
  private final String POST = "eaf9c4c8-1e9f-458a-904c-c1c588036380";
  private final String SEL1_ADDR = "d04e6cf1-5080-40b6-8c53-9113bb1d8561";
  private final String POST_STD_ADDR = "8e62c9b0-7fe0-46cb-a2e2-da79e6e1790d";
  private final String POST_FAST_ADDR = "3ea775b1-8fdf-4506-b20f-d53b96d607ed";  
  
  private final String COURIER1_STD = "89514f03-c098-4a16-a495-d24d526134cd";
  private final String COURIER2_FAST = "4cda25b3-0340-418d-887e-6ea993e66856"; 
  

  public WfTreeTest(DXManWorkflowTree workflowTree, String wfId) {
    super(workflowTree, wfId);
  }

  @Override
  public void designControl() {

    // TODO the workflow tree can also include invocation nodes so the argument should be only the invocationNodeId
    
    // CustomerService, LPB.createRecord
    customiseOrder(CUSTOMER, LBP_CREATE, 0); 
    // CustomerService, SenderService
    customiseOrder(CUSTOMER, SENDER, 1);    
    
    // SenderService, PostService
    customiseOrder(SENDER, POST, 0);
    // SenderService, EmailService.sendWelcEmail
    customiseOrder(SENDER, EMAIL_WELCEMAIL, 1);
    
    // PostService, Courier1.sendWelcStd, SEL1.addr
    customiseSelector(POST, COURIER1_STD, 
      SEL1_ADDR, DXManWfConditionOperator.EQUAL, "Oxford St");
    // PostService, Courier2.sendWelcFast, SEL1.addr
    customiseSelector(POST, COURIER2_FAST, SEL1_ADDR, 
      DXManWfConditionOperator.NOT_EQUAL, "Oxford St");
  }
  
  @Override
  public void designData() {
    
    // Post.sendWelcStd.addr --> SEL1.addr 
    DXManDataChannelPoint origin = new DXManDataChannelPoint(POST_STD_ADDR);
    DXManDataChannelPoint destination = new DXManDataChannelPoint(SEL1_ADDR);
    addDataChannel(POST, origin, destination);//Post,origin,dest
    
    // Post.sendWelcFast.addr --> SEL1.addr 
    DXManDataChannelPoint origin2 = new DXManDataChannelPoint(POST_FAST_ADDR);
    DXManDataChannelPoint destination2 = new DXManDataChannelPoint(SEL1_ADDR);
    addDataChannel(POST, origin2, destination2);//Post,origin,dest
  }

  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
        
    wfInputs.put(CUSTOMER_CREATE_NAME, "Damian Arellanes"); // Customer.createRecord.name
    wfInputs.put(CUSTOMER_STD_NAME, "Damian Arellanes"); // Customer.sendWelcStd.name
    wfInputs.put(CUSTOMER_FAST_NAME, "Damian Arellanes"); // Customer.sendWelcFast.name
    
    wfInputs.put(CUSTOMER_CREATE_ADDR, "Oxford St"); // Customer.createRecord.addr
    wfInputs.put(CUSTOMER_STD_ADDR, "Oxford St"); // Customer.sendWelcStd.addr
    wfInputs.put(CUSTOMER_FAST_ADDR, "Oxford St"); // Customer.sendWelcFast.addr
    
    wfInputs.put(CUSTOMER_CREATE_EMAIL, "damian.arellanes@gmail.com"); // Customer.createRecord.email
    wfInputs.put(CUSTOMER_WELCEMAIL_EMAIL, "damian.arellanes@gmail.com"); // Customer.sendWelcEmail.email
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    
    DXManWfOutputs wfOutputs = new DXManWfOutputs();    
    wfOutputs.add(CUSTOMER_STD_RES); // Customer.sendWelcStd.res
    wfOutputs.add(CUSTOMER_FAST_RES); // Customer.sendWelcFast.res
    wfOutputs.add(CUSTOMER_CREATE_ID); // Customer.createRecord.id
    //wfOutputs.add("Customer.sendWelcEmail.res");
    
    return wfOutputs;
  }
    
}
