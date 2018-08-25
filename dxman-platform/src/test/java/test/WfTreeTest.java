package test;

import com.dxman.deployment.cli.DXManWorkflowTreeEditor;
import com.dxman.design.data.DXManDataChannelPoint;
import com.dxman.execution.*;
import com.dxman.execution.selector.DXManWfConditionOperator;

/**
 * @author Damian Arellanes
 */
public class WfTreeTest extends DXManWorkflowTreeEditor {
  
  private final String CUSTOMER = "f2acaa7f-ca5e-41a7-ae9c-06e113ae5f6e";
  private final String CUSTOMER_CREATE_NAME = "87aeacb4-7c34-4eb3-a9e5-8e7559f51514";
  private final String CUSTOMER_CREATE_ADDR = "9f09a40b-5aad-4a71-a3d9-e8caa36ba058";
  private final String CUSTOMER_CREATE_EMAIL = "998c5f9f-8508-49e8-8319-1f8ba78b165a";
  private final String CUSTOMER_CREATE_ID = "c0577610-3ca1-406d-a74f-e7567e39e5c7";
  private final String CUSTOMER_WELCEMAIL_EMAIL = "61b120f7-31d2-458a-ae3b-ef2999407247";
  private final String CUSTOMER_STD_NAME = "b427ca90-c708-4444-bc4e-7f6a99d8395c";
  private final String CUSTOMER_STD_ADDR = "1bb4b783-d1c8-44e3-8873-447d0ea5de93";
  private final String CUSTOMER_STD_RES = "63171f82-e07f-4d3f-b987-8f49157aa3af";
  private final String CUSTOMER_FAST_NAME = "8e79add5-6617-4359-81cf-28bad12beaa0";
  private final String CUSTOMER_FAST_ADDR = "044fc578-0eb9-4630-a88d-d379594ed539";
  private final String CUSTOMER_FAST_RES = "20ea62bd-e4a4-445b-959a-4a9037b300d8";
  
  private final String LBP_CREATE = "331d155a-9604-4ed2-90e2-9876ad8ef9aa";
  
  private final String SENDER = "e5232b39-832c-407f-82b8-ff3774c4024c";
  
  private final String EMAIL_WELCEMAIL = "6dd2f38f-4b34-4a53-992e-c94ae718007e";
  
  private final String POST = "6319f8be-ed78-457f-9edb-2e0c3633c547";
  private final String SEL1_ADDR = "15926c1d-1c58-4edb-aacc-4c0161586c7a";
  private final String POST_STD_ADDR = "a0565144-02fc-420a-bb06-48757fe0eb35";
  private final String POST_FAST_ADDR = "cf1a1019-974d-4fb9-9cec-f910146157d1";  
  
  private final String COURIER1_STD = "c140ad9c-3f9a-473f-8a75-9ac879ce18c7";
  private final String COURIER2_FAST = "4076f4f3-635b-4965-90ed-1fda0520a214"; 
  

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
      SEL1_ADDR, DXManWfConditionOperator.NOT_EQUAL, "Oxford St");
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
