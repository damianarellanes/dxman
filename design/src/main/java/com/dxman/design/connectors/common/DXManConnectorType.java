package com.dxman.design.connectors.common;

/**
 * @author Damian Arellanes
 */
public enum DXManConnectorType {
  // INVOCATION is not required since all atomic services must have one of them
  SEQUENCER, SELECTOR, PARALLEL, LOOPER, GUARD;
}
