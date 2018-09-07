package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.*;
import com.dxman.utils.DXManErrors;
import static com.dxman.utils.DXManIDGenerator.generateParameterUUID;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import org.javalite.http.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class DXManBlockChainManager implements DXManDataSpace {
  
  private final String blockChainEndpoint;
  private final String THING_CLASS = "com.dxman.blockchain.DXManNode";
  private final String PARAM_CLASS = "com.dxman.blockchain.DXManParameter";
  private final String UPDATE_PARAM_CON = "com.dxman.blockchain.UpdateParameterConcept";
  private final String CREATE_PARAMETERS = "com.dxman.blockchain.CreateParameters";
  private final String UPDATE_PARAM = "com.dxman.blockchain.UpdateParameter";
  private final String UPDATE_PARAMS = "com.dxman.blockchain.UpdateParameters";  
  
  public DXManBlockChainManager(String blockChainEndpoint) {
    this.blockChainEndpoint = blockChainEndpoint;
  }
  
  @Override
  public int registerThing(String id, String alias) {
    
    try {
      
      JSONObject node = new JSONObject();
      node.put("$class", THING_CLASS);
      node.put("nodeId", id);
      node.put("alias", alias);

      Post result = post(
        blockChainEndpoint + "/api/" + THING_CLASS, 
        node.toString()
      );

      switch(result.responseCode()) {
        case 500:
          System.err.println("Participant " + id +" already exists!");
          break;
        case 200:
          System.out.println("Participant " + id +" has been added!");
          break;
      }

      return result.responseCode();
    } catch (JSONException ex) { System.err.println(ex.toString()); }
    
    return 200;
  }
  
  @Override
  public void registerParameters(List<DXManDataParameter> parameters, String workflowId) {
    
    /*Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String instant = timestamp.toInstant().toString();*/
    
    // Registers 20 parameters at a time to avoid any issue with the blockchain server
    int from = 0, to = 0;
    while(to < parameters.size()) {
      
      from = to;
      to = from + 20;

      if(parameters.size() < to) to = parameters.size();
  
      try {

        JSONObject transaction = new JSONObject();
        transaction.put("$class", CREATE_PARAMETERS);
        JSONArray parametersJSON = new JSONArray();
        for(DXManDataParameter par: parameters.subList(from, to)) {

          JSONObject parameterJSON = new JSONObject();
          parameterJSON.put("$class", PARAM_CLASS);
          parameterJSON.put("id", par.getId());
          parameterJSON.put("parameterId", par.getParameterId());
          parameterJSON.put("workflowId", par.getWorkflowId());
          parameterJSON.put("value", par.getValue());
          //parameterJSON.put("timestamp", instant);
          JSONArray readersJSON = new JSONArray();
          for(String readerId: par.getReaders()) {
            readersJSON.put(PARAM_CLASS + "#" 
              + generateParameterUUID(readerId, workflowId));
          }
          parameterJSON.put("readers", readersJSON);

          parametersJSON.put(parameterJSON);
        }

        transaction.put("parameters", parametersJSON);

        Post result = post(blockChainEndpoint + 
          "/api/" + CREATE_PARAMETERS, transaction.toString());

        if(result.responseCode() == 200) {
          //System.out.println((to-from) + " parameters have been registered!");
        }

      } catch (JSONException ex) { System.err.println(ex.toString()); }
    }
  }
  
  @Override
  public String readParameter(String parameterId, String workflowId, 
    String workflowTimestamp) {
    
    try {
      
      JSONObject query = new JSONObject();
      query.put("$class", PARAM_CLASS);
      query.put("parameterId", parameterId);
      query.put("workflowId", workflowId);

      StringBuilder url = new StringBuilder();
      url.append(blockChainEndpoint)
        .append("/api/queries/selectWorkflowParameter")
        .append("?parameterId=").append(parameterId).append("&")
        .append("workflowId=").append(workflowId).append("&")
        .append("workflowTimestamp=").append(workflowTimestamp);
      
      JSONArray result = new JSONArray(get(url.toString()));
      return ((JSONObject)result.get(0)).getString("value");
        
    } catch (JSONException ex) {       
      return DXManErrors.PARAMETER_VALUE_NOT_FOUND.name();
    }
  }
  
  @Override
  public void writeParameters(List<DXManDataParameter> parameters, String workflowId) {
    
    /*Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String instant = timestamp.toInstant().toString();*/
    
    // Writes 20 parameters at a time to avoid any issue with the blockchain server
    int from = 0, to = 0;
    while(to < parameters.size()) {
      
      from = to;
      to = from + 20;

      if(parameters.size() < to) to = parameters.size();
  
      try {

        JSONObject transaction = new JSONObject();
        transaction.put("$class", UPDATE_PARAMS);
        JSONArray updatesJSON = new JSONArray();
        for(DXManDataParameter par: parameters.subList(from, to)) {

          JSONObject updateJSON = new JSONObject();
          updateJSON.put("$class", UPDATE_PARAM_CON);
          updateJSON.put("parameter", PARAM_CLASS +"#" + 
            par.getId());
          updateJSON.put("newValue", par.getValue());
          //updateJSON.put("timestamp", instant);
      
          updatesJSON.put(updateJSON);
        }

        transaction.put("updates", updatesJSON);

        Post result = post(blockChainEndpoint + 
          "/api/" + UPDATE_PARAMS, transaction.toString());

        if(result.responseCode() == 200) {
          System.out.println((to-from) + " parameters have been updated!");
        }

      } catch (JSONException ex) { System.err.println(ex.toString()); }
    }
  }
  
  @Override
  public void writeParameter(String parameterId, String workflowId, 
    String newValue) {
    
    /*Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String instant = timestamp.toInstant().toString();*/
    
    try {
      
      JSONObject transaction = new JSONObject();
      transaction.put("$class", UPDATE_PARAM);
      JSONObject updateJSON = new JSONObject();
      updateJSON.put("$class", UPDATE_PARAM_CON);
      updateJSON.put("parameter", PARAM_CLASS + "#" + 
        generateParameterUUID(parameterId, workflowId));
      updateJSON.put("newValue", newValue);
      //updateJSON.put("timestamp", instant);
      transaction.put("update", updateJSON);      

      Post result = post(blockChainEndpoint + 
        "/api/" + UPDATE_PARAM, transaction.toString());
      
      if(result.responseCode() == 200) {
        System.out.println("Parameter " + parameterId + " has been updated!");
      }
        
    } catch (JSONException ex) { System.err.println(ex.toString()); }
  }
  
  private Post post(String url, String content) {
    return Http.post(url, content)
      .header("Accept", "application/json")
      .header("Content-Type", "application/json");
  }
    
  private String get(String url) {
    return Http.get(url).text();
  }
  
  public static void main(String[] args) {
    
    DXManBlockChainManager blockchain = new DXManBlockChainManager("http://localhost:3000");    
    
    List<DXManDataParameter> parameters = new ArrayList<>();
    parameters.add(new DXManDataParameter("Par1", "MyWfId", "Hello world initial", Arrays.asList("Par2", "Par3")));
    parameters.add(new DXManDataParameter("Par2", "MyWfId", "null", new ArrayList<>()));
    parameters.add(new DXManDataParameter("Par3", "MyWfId", "null", new ArrayList<>()));
    
    //blockchain.registerParameters(parameters, "MyWfId");
        
    //"2018-01-02T11:42Z";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Instant instant = timestamp.toInstant();
    
    /*System.out.println("----------------UPDATING...----------");
    blockchain.writeParameter("Par1", "MyWfId", "AN UPDATE HAS BEEN DONE!!!");*/
    
    List<DXManDataParameter> updates = new ArrayList<>();
    updates.add(new DXManDataParameter("Par1", "MyWfId", "MULTIPLE UPDATES for everyone", new ArrayList<>()));
    updates.add(new DXManDataParameter("Par3", "MyWfId", "Only for Par3", new ArrayList<>()));
    updates.add(new DXManDataParameter("Par2", "MyWfId", "Only for Par2", new ArrayList<>()));
    blockchain.writeParameters(updates, "MyWfId");    
    
    System.out.println("Par1-->" + blockchain.readParameter("Par1", "MyWfId", instant.toString()));
    System.out.println("Par2-->" + blockchain.readParameter("Par2", "MyWfId", instant.toString()));
    System.out.println("Par3-->" + blockchain.readParameter("Par3", "MyWfId", instant.toString()));
    
    /*String par1UUID = generateParameterUUID("Par1", "MyWfId");System.out.println("PAR1:" + par1UUID);
    String par2UUID = generateParameterUUID("Par2", "MyWfId");System.out.println("PAR2:" + par2UUID);
    String par3UUID = generateParameterUUID("Par3", "MyWfId");System.out.println("PAR3:" + par3UUID);
    
    List<String> readers = Arrays.asList("Par2", "Par3");
    blockchain.registerParameter("Par1", "MyWfId", "Hello World Initial", readers);
    List<String> readers2 = new ArrayList<>();
    blockchain.registerParameter("Par2", "MyWfId", "null", readers2);
    List<String> readers3 = new ArrayList<>();
    blockchain.registerParameter("Par3", "MyWfId", "null", readers3);

    //"2018-01-02T11:42Z";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Instant instant = timestamp.toInstant();
    //System.out.println(instant);
    
    System.out.println("Par1-->" + blockchain.readParameter("Par1", "MyWfId", "2017-08-24T17:17:53.666Z"));
    System.out.println("Par2-->" + blockchain.readParameter("Par2", "MyWfId", "2017-08-24T17:17:53.666Z"));
    System.out.println("Par3-->" + blockchain.readParameter("Par3", "MyWfId", "2017-08-24T17:17:53.666Z"));
    
    System.out.println("----------------UPDATING...----------");
    blockchain.writeParameter("Par1", "MyWfId", "AN UPDATE HAS BEEN DONE!!!");
    
    System.out.println("Par1-->" + blockchain.readParameter("Par1", "MyWfId", instant.toString()));
    System.out.println("Par2-->" + blockchain.readParameter("Par2", "MyWfId", instant.toString()));
    System.out.println("Par3-->" + blockchain.readParameter("Par3", "MyWfId", instant.toString()));*/
  }
}
