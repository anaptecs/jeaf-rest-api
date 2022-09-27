/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.rest.executor.api.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.rest.executor.api.ContentType;
import com.anaptecs.jeaf.rest.executor.api.HttpMethod;
import com.anaptecs.jeaf.rest.executor.api.RESTRequest;
import com.anaptecs.jeaf.rest.executor.api.RESTRequest.Builder;

public class RESTRequestTest {

  @Test
  void testQueryParams( ) {
    Builder lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON);
    lBuilder.addQueryParam("query1", "Hello");
    RESTRequest lRequest = lBuilder.build();
    Map<String, Set<String>> lQueryParams = lRequest.getQueryParams();
    assertEquals(1, lQueryParams.size());
    Set<String> lQueryParam = lQueryParams.get("query1");
    assertEquals(1, lQueryParam.size());
    assertEquals("Hello", lQueryParam.iterator().next());

    // Test handling of multiple value for one query param
    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON);
    lBuilder.addQueryParam("query1", "Hello");
    lBuilder.addQueryParams("query1", "World", "!");

    lRequest = lBuilder.build();
    assertEquals(String.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.GET, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    assertEquals(0, lRequest.getCookies().size());
    assertEquals(0, lRequest.getHeaders().size());
    assertEquals(null, lRequest.getBody());

    lQueryParams = lRequest.getQueryParams();
    assertEquals(1, lQueryParams.size());
    lQueryParam = lQueryParams.get("query1");
    assertEquals(3, lQueryParam.size());
    assertTrue(lQueryParam.contains("Hello"));
    assertTrue(lQueryParam.contains("World"));
    assertTrue(lQueryParam.contains("!"));

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON);
    lBuilder.addQueryParam("query1", "Hello");
    lBuilder.addQueryParams("query1", "World", "!");
    lBuilder.addQueryParam("query1", "Hello");
    lBuilder.addQueryParam("query2", "???");

    lRequest = lBuilder.build();

    lQueryParams = lRequest.getQueryParams();
    assertEquals(2, lQueryParams.size());
    lQueryParam = lQueryParams.get("query1");
    assertEquals(3, lQueryParam.size());
    assertTrue(lQueryParam.contains("Hello"));
    assertTrue(lQueryParam.contains("World"));
    assertTrue(lQueryParam.contains("!"));

    lQueryParam = lQueryParams.get("query2");
    assertEquals(1, lQueryParam.size());
    assertTrue(lQueryParam.contains("???"));

    // Test handling of multiple ways to add data
    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON);
    lBuilder.addQueryParam("query1", "Hello");
    lBuilder.addQueryParams("query1", "World", "!");
    lBuilder.addQueryParam("query1", "Hello");
    Set<String> lStrings = new HashSet<>();
    lStrings.add("Wonderful");
    lStrings.add("world");
    lBuilder.addQueryParams("query1", lStrings);
    lBuilder.addQueryParam("query2", "???");

    lRequest = lBuilder.build();

    lQueryParams = lRequest.getQueryParams();
    assertEquals(2, lQueryParams.size());
    lQueryParam = lQueryParams.get("query1");
    assertEquals(5, lQueryParam.size());
    assertTrue(lQueryParam.contains("Hello"));
    assertTrue(lQueryParam.contains("World"));
    assertTrue(lQueryParam.contains("!"));
    assertTrue(lQueryParam.contains("world"));
    assertTrue(lQueryParam.contains("Wonderful"));

    lQueryParam = lQueryParams.get("query2");
    assertEquals(1, lQueryParam.size());
    assertTrue(lQueryParam.contains("???"));

  }
}
