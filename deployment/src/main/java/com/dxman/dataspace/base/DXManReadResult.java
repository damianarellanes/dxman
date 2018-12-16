package com.dxman.dataspace.base;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Damian Arellanes
 */
public class DXManReadResult {
    private final JSONArray values;
    private final List<String> inputNames;

  public DXManReadResult(JSONArray values, List<String> inputNames) {
    this.values = values;
    this.inputNames = inputNames;
  }
  
  public String getValue(int i) {
    try {
      return values.getString(i);
    } catch (JSONException ex) { System.err.println(ex); }
    return "NOT FOUND";
  }

  public JSONArray getValues() { return values; }
  public List<String> getInputNames() { return inputNames; }
}
