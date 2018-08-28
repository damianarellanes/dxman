package test;

import com.dxman.deployment.cli.DXManWorkflowTreeEditor;
import com.dxman.design.data.DXManDataChannelPoint;
import com.dxman.execution.*;
import com.dxman.execution.selector.DXManWfConditionOperator;

/**
 * @author Damian Arellanes
 */
public class WfTreeTest extends DXManWorkflowTreeEditor {
  
  private final String CUSTOMER = "21b63524-ffd0-438f-adc5-6d0fb428e058";
  private final String CUSTOMER_CREATE_NAME = "7ff109d4-fa4f-4ee7-9b3e-98f224a84acd";
  private final String CUSTOMER_CREATE_ADDR = "5d8d4f90-d440-4d9d-b675-4e31a9ec655b";
  private final String CUSTOMER_CREATE_EMAIL = "63e50512-863a-477f-849a-529d4fbb95e3";
  private final String CUSTOMER_CREATE_ID = "e09d33d9-db0e-4ed0-92d6-a0b6f098aec3";
  private final String CUSTOMER_WELCEMAIL_EMAIL = "b2e3b70b-81c6-42e8-a78c-4f80e7418400";
  private final String CUSTOMER_STD_NAME = "dedbdd92-89e2-4a88-bf27-f3b72a2f8ec5";
  private final String CUSTOMER_STD_ADDR = "64bf954b-d300-4f3c-8cf0-d1c9493042aa";
  private final String CUSTOMER_STD_RES = "fce29fd1-272c-41e3-b5d0-c5baf9d353a0";
  private final String CUSTOMER_FAST_NAME = "174f7dff-38ef-43fa-9763-cb0bde566469";
  private final String CUSTOMER_FAST_ADDR = "361192cb-6420-4b12-9ca1-218fb2f925b4";
  private final String CUSTOMER_FAST_RES = "6ff4b0d4-afd8-4854-b2d1-2b5e1c5480e5";
  
  private final String LBP_CREATE = "083eb853-b3d8-4fc9-969c-913168922b47";
  
  private final String SENDER = "105c788e-9fd6-4bf7-9116-9ab29fac3e2a";
  
  private final String EMAIL_WELCEMAIL = "2a05a277-5288-4159-8ff9-5f6057ee4563";
  
  private final String POST = "79c18f61-0d1a-4377-a5b8-a2998d17a941";
  private final String SEL1_ADDR = "106bd436-291b-4e13-8444-6b55c6d86590";
  private final String POST_STD_ADDR = "0de712a5-c7a2-4bc3-bdf4-080175e24fae";
  private final String POST_FAST_ADDR = "3ec2f520-5b1c-401f-a9f7-44dc0eded070";  
  
  private final String COURIER1_STD = "99580878-323b-4bf2-8301-558d7d3b084b";
  private final String COURIER2_FAST = "fd23b20a-c32a-404e-88d4-c336a638f79e"; 
  

  public WfTreeTest(DXManWorkflowTree workflowTree, String wfId) {
    super(workflowTree, wfId);
  }

  @Override
  public void designControl() {

    // TODO the workflow tree can also include invocation nodes so the argument should be only the invocationNodeId
    
    /*// CustomerService, LPB.createRecord
    customiseOrder(CUSTOMER, LBP_CREATE, 0); 
    // CustomerService, SenderService
    customiseOrder(CUSTOMER, SENDER, 1);    
    
    // SenderService, PostService
    customiseOrder(SENDER, POST, 0);
    // SenderService, EmailService.sendWelcEmail
    customiseOrder(SENDER, EMAIL_WELCEMAIL, 1);*/
    
    /*// PostService, Courier1.sendWelcStd, SEL1.addr
    customiseSelector(POST, COURIER1_STD, 
      SEL1_ADDR, DXManWfConditionOperator.EQUAL, "Oxford St");
    // PostService, Courier2.sendWelcFast, SEL1.addr
    customiseSelector(POST, COURIER2_FAST, SEL1_ADDR, 
      DXManWfConditionOperator.EQUAL, "Oxford St");*/
    
    // PostService, Courier1.sendWelcStd
    customiseParallel(POST, COURIER1_STD, 1);
    // PostService, Courier2.sendWelcFast
    customiseParallel(POST, COURIER2_FAST, 1);
  }
  
  @Override
  public void designData() {
    
    /*// Post.sendWelcStd.addr --> SEL1.addr 
    DXManDataChannelPoint origin = new DXManDataChannelPoint(POST_STD_ADDR);
    DXManDataChannelPoint destination = new DXManDataChannelPoint(SEL1_ADDR);
    addDataChannel(POST, origin, destination);//Post,origin,dest
    
    // Post.sendWelcFast.addr --> SEL1.addr 
    DXManDataChannelPoint origin2 = new DXManDataChannelPoint(POST_FAST_ADDR);
    DXManDataChannelPoint destination2 = new DXManDataChannelPoint(SEL1_ADDR);
    addDataChannel(POST, origin2, destination2);//Post,origin,dest*/
  }

  @Override
  public DXManWfInputs getInputs() {
    DXManWfInputs wfInputs = new DXManWfInputs();
        
    //wfInputs.put(CUSTOMER_CREATE_NAME, "Damian Arellanes"); // Customer.createRecord.name
    wfInputs.put(CUSTOMER_STD_NAME, "Damian Arellanes"); // Customer.sendWelcStd.name
    wfInputs.put(CUSTOMER_FAST_NAME, "Damian Arellanes"); // Customer.sendWelcFast.name
    
    //wfInputs.put(CUSTOMER_CREATE_ADDR, "Oxford St"); // Customer.createRecord.addr
    wfInputs.put(CUSTOMER_STD_ADDR, "Oxford St"); // Customer.sendWelcStd.addr
    wfInputs.put(CUSTOMER_FAST_ADDR, "Oxford St"); // Customer.sendWelcFast.addr
    
    /*wfInputs.put(CUSTOMER_CREATE_EMAIL, "damian.arellanes@gmail.com"); // Customer.createRecord.email
    wfInputs.put(CUSTOMER_WELCEMAIL_EMAIL, "damian.arellanes@gmail.com"); // Customer.sendWelcEmail.email*/
    return wfInputs;
  }

  @Override
  public DXManWfOutputs getOutputs() {
    
    DXManWfOutputs wfOutputs = new DXManWfOutputs();    
    wfOutputs.add(CUSTOMER_STD_RES); // Customer.sendWelcStd.res
    wfOutputs.add(CUSTOMER_FAST_RES); // Customer.sendWelcFast.res
    //wfOutputs.add(CUSTOMER_CREATE_ID); // Customer.createRecord.id
    //wfOutputs.add("Customer.sendWelcEmail.res");
    
    return wfOutputs;
  }
    
}
