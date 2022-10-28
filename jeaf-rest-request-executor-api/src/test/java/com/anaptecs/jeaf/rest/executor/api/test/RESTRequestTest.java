/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.rest.executor.api.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
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
    assertEquals("/", lRequest.getPath());
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

    // Test null handling
    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (String) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValue' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, "Hello");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValue' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParams("q", (String[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParams(null, "Hello", "World");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParams(null, lStrings);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParams("q",
          (Collection<String>) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }
  }

  @Test
  void testBody( ) {
    Builder lBuilder = RESTRequest.builder(Integer.class, HttpMethod.POST, ContentType.XML);
    assertNotNull(lBuilder.setPath("/this/is/my/path"));
    assertNotNull(lBuilder.setBody(Arrays.asList("Hello", "World", "!")));
    RESTRequest lRequest = lBuilder.build();
    assertEquals("/this/is/my/path", lRequest.getPath());
    assertEquals(Arrays.asList("Hello", "World", "!"), lRequest.getBody());

    // try to set null as path
    try {
      RESTRequest.builder(Integer.class, HttpMethod.POST, ContentType.XML).setPath(null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pPath' must not be null.", e.getMessage());
    }
  }

  @Test
  void testCookie( ) {
    Builder lBuilder = RESTRequest.builder(Integer.class, HttpMethod.POST, ContentType.XML);
    assertNotNull(lBuilder.setCookie("myCookie", "myCookieValue"));
    RESTRequest lRequest = lBuilder.build();
    assertEquals(1, lRequest.getCookies().size());
    assertEquals("myCookieValue", lRequest.getCookies().get("myCookie"));

    // Test null handling
    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.POST, ContentType.XML);
    assertNotNull(lBuilder.setCookie("myCookie", null));
    lRequest = lBuilder.build();
    assertEquals(1, lRequest.getCookies().size());
    assertEquals(null, lRequest.getCookies().get("myCookie"));

    try {
      RESTRequest.builder(Integer.class, HttpMethod.POST, ContentType.XML).setCookie(null, "Hello");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pCookieName' must not be null.", e.getMessage());
    }
  }

  @Test
  void testHeader( ) {
    Builder lBuilder = RESTRequest.builder(Integer.class, HttpMethod.POST, ContentType.XML);
    assertNotNull(lBuilder.setHeader("My-Header", "myHeaderValue"));
    RESTRequest lRequest = lBuilder.build();
    assertEquals(1, lRequest.getHeaders().size());
    assertEquals("myHeaderValue", lRequest.getHeaders().get("My-Header"));

    // Test null handling
    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.POST, ContentType.XML);
    assertNotNull(lBuilder.setHeader("My-Header", (String) null));
    lRequest = lBuilder.build();
    assertEquals(1, lRequest.getHeaders().size());
    assertEquals(null, lRequest.getHeaders().get("My-Header"));

    try {
      RESTRequest.builder(Integer.class, HttpMethod.POST, ContentType.XML).setHeader(null, "value");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }

    // Test general error handling
    try {
      RESTRequest.builder(null, HttpMethod.POST, ContentType.XML);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pServiceClass', 'pHttpMethod' and 'pContentType' must not be null.", e.getMessage());
    }
    try {
      RESTRequest.builder(Integer.class, null, ContentType.XML);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pServiceClass', 'pHttpMethod' and 'pContentType' must not be null.", e.getMessage());
    }
    try {
      RESTRequest.builder(Integer.class, HttpMethod.POST, null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pServiceClass', 'pHttpMethod' and 'pContentType' must not be null.", e.getMessage());
    }

    // Test other primitive header types
    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setHeader("boolean", Boolean.TRUE));
    assertEquals(lBuilder, lBuilder.setHeader("byte", Byte.valueOf((byte) 47)));
    assertEquals(lBuilder, lBuilder.setHeader("short", Short.valueOf((short) 241)));
    assertEquals(lBuilder, lBuilder.setHeader("integer", Integer.valueOf((int) 47110815)));
    assertEquals(lBuilder, lBuilder.setHeader("long", Long.valueOf((long) 123456789)));
    assertEquals(lBuilder, lBuilder.setHeader("double", Double.valueOf((double) 123456789.1234)));
    assertEquals(lBuilder, lBuilder.setHeader("float", Float.valueOf((float) 1239.1234)));
    assertEquals(lBuilder, lBuilder.setHeader("character", Character.valueOf('A')));

    lRequest = lBuilder.build();
    assertEquals(Integer.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.PATCH, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    Map<String, String> lHeaders = lRequest.getHeaders();
    assertEquals("true", lHeaders.get("boolean"));
    assertEquals("47", lHeaders.get("byte"));
    assertEquals("241", lHeaders.get("short"));
    assertEquals("47110815", lHeaders.get("integer"));
    assertEquals("123456789", lHeaders.get("long"));
    assertEquals("1.234567891234E8", lHeaders.get("double"));
    assertEquals("1239.1234", lHeaders.get("float"));
    assertEquals("A", lHeaders.get("character"));

    // Test null handling for header values
    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setHeader("boolean", (Boolean) null));
    assertEquals(lBuilder, lBuilder.setHeader("byte", (Byte) null));
    assertEquals(lBuilder, lBuilder.setHeader("short", (Short) null));
    assertEquals(lBuilder, lBuilder.setHeader("integer", (Integer) null));
    assertEquals(lBuilder, lBuilder.setHeader("long", (Long) null));
    assertEquals(lBuilder, lBuilder.setHeader("double", (Double) null));
    assertEquals(lBuilder, lBuilder.setHeader("float", (Float) null));
    assertEquals(lBuilder, lBuilder.setHeader("character", (Character) null));

    lRequest = lBuilder.build();
    assertEquals(Integer.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.PATCH, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    lHeaders = lRequest.getHeaders();
    assertEquals(null, lHeaders.get("boolean"));
    assertEquals(null, lHeaders.get("byte"));
    assertEquals(null, lHeaders.get("short"));
    assertEquals(null, lHeaders.get("integer"));
    assertEquals(null, lHeaders.get("long"));
    assertEquals(null, lHeaders.get("double"));
    assertEquals(null, lHeaders.get("float"));
    assertEquals(null, lHeaders.get("character"));

    // Test null handling
    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    try {
      lBuilder.setHeader(null, (Boolean) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }
    try {
      lBuilder.setHeader(null, (Byte) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }
    try {
      lBuilder.setHeader(null, (Short) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }
    try {
      lBuilder.setHeader(null, (Integer) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }
    try {
      lBuilder.setHeader(null, (Long) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }
    try {
      lBuilder.setHeader(null, (Double) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }
    try {
      lBuilder.setHeader(null, (Float) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }
    try {
      lBuilder.setHeader(null, (Character) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }
  }

  @Test
  void testContentType( ) {
    assertEquals("application/json", ContentType.JSON.getMimeType());
    assertEquals("application/xml", ContentType.XML.getMimeType());
  }

}
