package com.dxman.thing.deployment.connectors.atomic;

import com.dxman.dataspace.base.DXManDataEntity;
import com.dxman.dataspace.base.DXManDataEntityFactory;
import com.dxman.dataspace.base.DXManDataParameter;
import com.dxman.design.data.DXManOperation;
import com.dxman.design.data.DXManParameter;
import com.dxman.utils.DXManConfiguration;
import com.dxman.utils.DXManIDGenerator;
import com.dxman.utils.DXManMap;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Damian Arellanes
 */
public class DXManDataUtil {
  
  public class PrecompiledOperation {
    
    private final Pattern pattern;
    private final String[] outputIds;

    public PrecompiledOperation(Pattern pattern, String[] outputIds) {
      this.pattern = pattern;
      this.outputIds = outputIds;
    }
    
    public Pattern getPattern() { return pattern; } 
    public String[] getOutputIds() { return outputIds; }
  }
  
  private final DXManMap<String, PrecompiledOperation> precompiledOperations;
  private final DXManDataEntityFactory dataEntityFactory;

  public DXManDataUtil(DXManMap<String, DXManOperation> operations, 
    DXManDataEntityFactory dataEntityFactory) {
    
    this.precompiledOperations = new DXManMap<>();
    this.dataEntityFactory = dataEntityFactory;    
    preCompilePatterns(operations);
  }
  
  private void preCompilePatterns(DXManMap<String, DXManOperation> operations) {
    
    operations.forEach((opKey,op)->{
      
      // Escape taken from https://stackoverflow.com/questions/14134558/
      //list-of-all-special-characters-that-need-to-be-escaped-in-a-regex/
      //27454382#27454382
      String  basePattern = op.getBindingInfo().getResponseTemplate().replaceAll(
        "[\\<\\(\\[\\{\\\\\\^\\-\\=\\$\\!\\|\\]\\}\\)\\?\\*\\+\\.\\>]", "\\\\$0"
      );
            
      // Precompiles outputs for the operation
      // TODO Parameters should be lists not maps
      int i = 0;
      String[] outputIds = new String[op.getOutputs().size()];      
      for(DXManParameter output: op.getOutputs().values()) {
      
        basePattern = basePattern.replace(
          DXManConfiguration.WILDCARD_START + output.getName() + 
            DXManConfiguration.WILDCARD_END, "(.*)"
        );
        outputIds[i++] = output.getId();
      }
      
      precompiledOperations.put(opKey, new PrecompiledOperation(
        Pattern.compile(basePattern), outputIds)
      );
    });
  }

  public DXManMap<String, PrecompiledOperation> getPrecompiledOperations() {
    return precompiledOperations;
  }
}
