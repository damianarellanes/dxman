package com.dxman.dataspace.blockchain;

import com.dxman.dataspace.base.*;
import com.dxman.design.data.DXManParameter;
import com.dxman.utils.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import org.json.*;

/**
 * @author Damian Arellanes
 */
public class BlockChainManager implements DXManDataSpace {
  
  private final String blockChainEndpoint;
      
  public BlockChainManager(String blockChainEndpoint) {    
    this.blockChainEndpoint = blockChainEndpoint;
  }
  
  @Override
  public int registerThing(String id, String alias) {
    
    /*try {
      
      JSONObject node = new JSONObject();
      node.put("$class", THING_CLASS);
      node.put("thingId", id);
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
    } catch (JSONException ex) { System.err.println(ex.toString()); }*/
    
    return 200;
  }
  
  @Override
  public void createParameters(List<DXManDataParameter> parameters, String workflowId) {

    // Registers 20 parameters at a time to avoid any issue with the blockchain server
    int from = 0, to = 0;
    while(to < parameters.size()) {

      from = to;
      to = from + 20;

      if(parameters.size() < to) to = parameters.size();

      try {

        JSONObject transaction = new JSONObject();
        transaction.put("$class", BlockchainConfiguration.CREATE_PARAMS);
        JSONArray entitiesJSON = new JSONArray();
        for(DXManDataParameter param: parameters.subList(from, to)) {
          entitiesJSON.put(param.generateJSON());
        }

        transaction.put("parameters", entitiesJSON);

        //System.out.println(transaction.toString());

        String result = post(blockChainEndpoint + 
          "/api/" + BlockchainConfiguration.CREATE_PARAMS, transaction.toString());

        System.out.println((to-from) + " parameters created");

      } catch (JSONException ex) { System.err.println(ex.toString()); }
    }
  }  
  
  @Override
  public DXManReadResult readParameters(Collection<DXManParameter> parameters, String wfId, 
    String wfTimestamp) {
  
    List<String> inputNames = new ArrayList<>();
    JSONArray inputRefs = new JSONArray();
    for(DXManParameter input: parameters) {

      inputNames.add(input.getName());
      inputRefs.put("resource:" + BlockchainConfiguration.PARAM_CLASS + "#" 
        + DXManIDGenerator.generateParameterUUID(input.getId(), wfId));
    }
    
    // Blocks until all parameters are read
    JSONArray resultArray = new JSONArray();
    do {
      //System.out.println("Attempting reading...");
      resultArray = readParameters(inputRefs, wfTimestamp);
    } while(resultArray.length() == 0);
        
    return new DXManReadResult(resultArray, inputNames);
  }
  
  private JSONArray readParameters(JSONArray paramRefs, 
    String workflowTimestamp) {
    
    try {
      
      JSONObject transaction = new JSONObject();
      transaction.put("$class", BlockchainConfiguration.READ_PARAMS);
      transaction.put("parameters", paramRefs);
      transaction.put("workflowTimestamp", workflowTimestamp);
      
      System.out.println(transaction.toString());
            
      String result = post(blockChainEndpoint + 
        "/api/" + BlockchainConfiguration.READ_PARAMS, transaction.toString());
      
      /*if(result.responseCode() != 200) {
        System.err.println("Error reading parameters!");
      }*/
      
      //System.out.println("READ OK");
      return new JSONArray(result);
    } catch (JSONException ex) {}
    
    return new JSONArray();
  }
  
  @Override
  public String readParameter(String parameterId, String workflowId, 
    String workflowTimestamp) {    
    
    try {
      
      JSONObject transaction = new JSONObject();
      transaction.put("$class", BlockchainConfiguration.READ_PARAM);
      transaction.put("parameter", "resource:" + BlockchainConfiguration.PARAM_CLASS + "#" 
        + DXManIDGenerator.generateParameterUUID(parameterId, workflowId));
      transaction.put("workflowTimestamp", workflowTimestamp);

      //System.out.println(transaction.toString());      
              
      // Blocks until the parameter is read
      String result;
      do {
        //System.out.println("Attempting reading...");
        result = post(blockChainEndpoint + 
        "/api/" + BlockchainConfiguration.READ_PARAMS, transaction.toString()); 
      } while(result.equals("\"" + DXManErrors.PARAMETER_VALUE_NOT_FOUND.name() + "\""));
      
      //System.out.println(result);
      //System.out.println(DXManErrors.PARAMETER_VALUE_NOT_FOUND.name());
      //System.out.println(result.equals(DXManErrors.PARAMETER_VALUE_NOT_FOUND.name()));
            
      /*if(result.responseCode() != 200) {
        System.err.println("Error reading parameters!");
      }*/      
      
      return fixValueRead(result);
        
    } catch (JSONException ex) { 
      System.out.println(ex);
      return "NOOOO"; //DXManErrors.PARAMETER_VALUE_NOT_FOUND.name();
    }
  }
  
  private String fixValueWrite(String value) {
    return "##" + value + "##";
  }
  
  private String fixValueRead(String value) {
    return value.replace("\"##", "").replace("##\"", "");
  }
  
  @Override
  public void writeParameters(List<DXManDataParameter> parameters) {
    
    //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    //String instant = timestamp.toInstant().toString();
    //String instant = getDataspaceTimestamp(); // TODO this in the blockchain model
    
    // Writes 20 parameters at a time to avoid any issue with the blockchain server
    int from = 0, to = 0;
    while(to < parameters.size()) {
      
      from = to;
      to = from + 20;

      if(parameters.size() < to) to = parameters.size();
  
      try {

        JSONObject transaction = new JSONObject();
        transaction.put("$class", BlockchainConfiguration.UPDATE_PARAMS);
        JSONArray updatesJSON = new JSONArray();
        for(DXManDataParameter param: parameters.subList(from, to)) {

          JSONObject updateJSON = new JSONObject();
          updateJSON.put("$class", BlockchainConfiguration.UPDATE_PARAM_CON);
          updateJSON.put("parameter", param.toString());
          updateJSON.put("newValue", fixValueWrite(param.getValue()));
          updateJSON.put("updater", param.getUpdater());
          //updateJSON.put("timestamp", instant);
      
          updatesJSON.put(updateJSON);
        }

        transaction.put("updates", updatesJSON);
        
        //System.out.println(transaction.toString());       
        
        String result = post(blockChainEndpoint + 
          "/api/" + BlockchainConfiguration.UPDATE_PARAMS, transaction.toString());
        
        //System.out.println("Result written:" + result);
        //result.responseMessage();
        
        /*if(result.responseCode() == 200) {
          System.out.println((to-from) + " data has been updated!");
        }*/

      } catch (JSONException ex) { System.err.println(ex.toString()); }
    }
  }
  
  @Override
  public void writeParameter(String parameterId, String workflowId, 
    String newValue, String updater) {
    
    //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    //String instant = timestamp.toInstant().toString();
    //String instant = getDataspaceTimestamp(); // TODO this in the blockchain model
    
    try {
      
      JSONObject transaction = new JSONObject();
      transaction.put("$class", "com.dxman.blockchain.UpdateParameter");
      JSONObject updateJSON = new JSONObject();
      updateJSON.put("$class", "com.dxman.blockchain.UpdateParameterConcept");
      updateJSON.put("parameter", "com.dxman.blockchain.Parameter#" + 
        DXManIDGenerator.generateParameterUUID(parameterId, workflowId));
      updateJSON.put("newValue", fixValueWrite(newValue));
      updateJSON.put("updater", updater);
      //updateJSON.put("timestamp", instant);
      transaction.put("update", updateJSON);  
      
      //System.out.println(transaction.toString());

      String result = post("http://148.100.108.114:3000" + 
        "/api/com.dxman.blockchain.UpdateParameter", transaction.toString());
      
      //System.out.println("Result written:" + result);
      
      /*if(result.responseCode() == 200) {
        System.out.println("Parameter " + parameterId + " has been updated!");
      } else System.out.println("ERROR UPDATING");*/
        
    } catch (JSONException ex) { System.err.println(ex.toString()); }
  }
  
  @Override
  public String getDataspaceTimestamp() {    
        
    return post(blockChainEndpoint + 
      "/api/" + BlockchainConfiguration.TIMESTAMP_CLASS, 
      "{\"$class\":\"" + BlockchainConfiguration.TIMESTAMP_CLASS + "\"}")
      .replace("\"", "").replace("\n", "");
  }
      
  private String post(String url, String content) {
    
    HttpURLConnection con = null;        
    byte[] postData = content.getBytes();
    StringBuilder response = new StringBuilder();

    try {

        URL myurl = new URL(url);
        con = (HttpURLConnection) myurl.openConnection();

        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setConnectTimeout(Integer.MAX_VALUE);
        con.setRequestProperty("User-Agent", "Java client");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(postData);
        }
        
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        //System.out.println("RESP@ " + response.toString());

    } catch (MalformedURLException ex) { System.out.println(ex);} 
      catch (IOException ex) { response = new StringBuilder(post(url, content)); } 
    finally { con.disconnect(); }
    
    return response.toString();
  }
  
  @Override
  public DXManDataParameter createDataParameter(String parameterId, 
    String workflowId, String value, String updater) {
    return new BlockchainParameter(parameterId, workflowId, value, updater);
  }
  
  @Override
  public String getEndpoint() {
    return blockChainEndpoint;
  }
  
  public static void main(String[] args) {
        
    BlockChainManager blockchain = new BlockChainManager("http://148.100.108.114:3000");
    
    // Creation
//    DXManDataParameter i1 = blockchain.createDataParameter("i1", "MyWfId", "null");
//    DXManDataParameter i2 = blockchain.createDataParameter("i2", "MyWfId", "null");
//    DXManDataParameter o1 = blockchain.createDataParameter("o1", "MyWfId", "null");
//    DXManDataParameter o2 = blockchain.createDataParameter("o2", "MyWfId", "null");
//    DXManDataParameter o3 = blockchain.createDataParameter("o3", "MyWfId", "null");
//    DXManDataParameter o4 = blockchain.createDataParameter("o4", "MyWfId", "null");    
    //DXManDataMapper mapper1 = blockchain.createDataMapper("mapper1", "MyWfId", "null", "mapperx");
    //DXManDataReducer reducer1 = blockchain.createDataReducer("reducer1", "MyWfId", "null", "reducerx");
        
//    // Data channels
//    i2.addWriter(o1); i2.addWriter(i1);
//    o2.addWriter(o1); o2.addWriter(i1);
//    o3.addWriter(o1); o3.addWriter(i1);
    /*i1.addReader(mapper1);
    i2.addReader(mapper1);    
    mapper1.addReader(o3);mapper1.addReader(reducer1);mapper1.addWriter(i1);mapper1.addWriter(i2);    
    reducer1.addReader(o4);reducer1.addWriter(mapper1);
    o1.addReader(o3);
    o2.addReader(o4);*/
//    
//    long start, end;
//    
//    List<DXManDataParameter> parameters = new ArrayList<>();
//    parameters.add(i1); parameters.add(i2);
//    parameters.add(o1); parameters.add(o2); parameters.add(o3);
//    parameters.add(o4);
//    String currentWorkflow = "MyWfId";
//    JSONArray paramRefs = new JSONArray();
//    for(DXManDataParameter parameter: parameters) {
//      paramRefs.put(parameter.getType() + "#" 
//      + DXManIDGenerator.generateParameterUUID(
//        parameter.getParameterId(), currentWorkflow));
//    }    
//    start = System.currentTimeMillis();         
//    JSONArray result = blockchain.readParameters(paramRefs, "2017-09-19T20:41:53.491Z");    
//    end = System.currentTimeMillis();
//    System.out.println(end - start);
//    
//    System.out.println(result.toString());
    
    // Batch of entities
//    List<DXManDataParameter> parameters = new ArrayList<>();
//    parameters.add(i1); parameters.add(i2); parameters.add(o1); parameters.add(o2); parameters.add(o3);
//    parameters.add(o4); //parameters.add(mapper1); parameters.add(reducer1);
//        
    // Call the blockchain transaction
    //blockchain.createParameters(parameters, "MyWfId");
        
    //"2018-01-02T11:42Z";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Instant instant = timestamp.toInstant();
    String workflowTimestamp = blockchain.getDataspaceTimestamp();
    
    System.out.println("----------------UPDATING...----------");
    //blockchain.writeParameter("i1", "MyWfId", "Interfaces done 2");    
    //System.out.println(blockchain.readParameter("i1", "MyWfId", instant.toString()));
    
//    List<DXManDataParameter> updates = new ArrayList<>();
//    DXManDataParameter i1_up = blockchain.createDataParameter("i1", "MyWfId", "i1 value"); 
//    DXManDataParameter i2_up = blockchain.createDataParameter("o1", "MyWfId", "o1 value");
//    updates.add(i1_up); //updates.add(i2_up);
//        
//    blockchain.writeParameters(updates);
    
    System.out.println("----------------READING...----------");
    /*System.out.println("i1-->" + blockchain.readParameter("i1", "MyWfId", instant.toString()));
    System.out.println("i2-->" + blockchain.readParameter("i2", "MyWfId", instant.toString()));
    System.out.println("o1-->" + blockchain.readParameter("o1", "MyWfId", instant.toString()));
    System.out.println("o2-->" + blockchain.readParameter("o2", "MyWfId", instant.toString()));
    System.out.println("o3-->" + blockchain.readParameter("o3", "MyWfId", instant.toString()));
    System.out.println("o4-->" + blockchain.readParameter("o4", "MyWfId", instant.toString()));*/
        
//    List<DXManDataParameter> readList = new ArrayList<>();
//    readList.add(i2); readList.add(o2); readList.add(o3);
//    JSONArray paramRefs = new JSONArray();
//    for(DXManDataParameter parameter: readList) {
//      paramRefs.put("resource:" + parameter.toString());
//    }    
//    JSONArray result = blockchain.readParameters(paramRefs, workflowTimestamp);
//    
//    System.out.println(result.toString());
    
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
