/**
 * anaptecs GmbH, Ricarda-Huch-Str. 71, 72760 Reutlingen, Germany
 * 
 * Copyright 2004 - 2023. All rights reserved.
 */
package com.anaptecs.jeaf.rest.resource.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.rest.resource.api.CustomHeaderFilter;

public class CustomHeaderFilterTest {
  @Test
  void testCustomHeaderFilter( ) {
    CustomHeaderFilter lFilter = CustomHeaderFilter.from("Hello", "X-Abc_EFg");
    assertTrue(lFilter.test("Hello"));
    assertTrue(lFilter.test("hELLO"));
    assertTrue(lFilter.test("x-ABC_EFG"));
    assertFalse(lFilter.test("Hell"));

    lFilter = CustomHeaderFilter.from(Arrays.asList("Hello", "X-Abc_EFg"));
    assertTrue(lFilter.test("Hello"));
    assertTrue(lFilter.test("hELLO"));
    assertTrue(lFilter.test("x-ABC_EFG"));
    assertFalse(lFilter.test("Hell"));
  }
}
