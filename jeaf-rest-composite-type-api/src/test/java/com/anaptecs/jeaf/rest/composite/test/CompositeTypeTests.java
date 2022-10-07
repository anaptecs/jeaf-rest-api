/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.rest.composite.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CompositeTypeTests {
  @Test
  void testSplit( ) {
    String lSerializedProperties = "\"ABC\"#\"YXZ-45vf\"#[\"ABC\",\"DEF\",null]";
    String[] lParts = lSerializedProperties.split("#");
    assertEquals(3, lParts.length);
    assertEquals("ABC", lParts[0].substring(1, lParts[0].length() - 1));

  }
}
