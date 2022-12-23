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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
    lBuilder.setQueryParameter("query1", "Hello");
    RESTRequest lRequest = lBuilder.build();
    Map<String, List<String>> lQueryParams = lRequest.getQueryParameters();
    assertEquals(1, lQueryParams.size());
    List<String> lQueryParam = lQueryParams.get("query1");
    assertEquals(1, lQueryParam.size());
    assertEquals("Hello", lQueryParam.iterator().next());

    // Test handling of multiple value for one query param
    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON);
    lBuilder.setQueryParameter("query1", "Hello", "World", "!");

    lRequest = lBuilder.build();
    assertEquals(String.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.GET, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    assertEquals(0, lRequest.getCookies().size());
    assertEquals(0, lRequest.getHeaderFields().size());
    assertEquals(null, lRequest.getBody());

    lQueryParams = lRequest.getQueryParameters();
    assertEquals(1, lQueryParams.size());
    lQueryParam = lQueryParams.get("query1");
    assertEquals(3, lQueryParam.size());
    assertTrue(lQueryParam.contains("Hello"));
    assertTrue(lQueryParam.contains("World"));
    assertTrue(lQueryParam.contains("!"));

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON);
    lBuilder.setQueryParameter("query1", "Hello");
    lBuilder.setQueryParameter("query1", "World", "!");
    lBuilder.setQueryParameter("query2", "???");

    lRequest = lBuilder.build();

    lQueryParams = lRequest.getQueryParameters();
    assertEquals(2, lQueryParams.size());
    lQueryParam = lQueryParams.get("query1");
    assertEquals(2, lQueryParam.size());
    assertTrue(lQueryParam.contains("World"));
    assertTrue(lQueryParam.contains("!"));

    lQueryParam = lQueryParams.get("query2");
    assertEquals(1, lQueryParam.size());
    assertTrue(lQueryParam.contains("???"));

    // Test handling of multiple ways to add data
    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON);
    Set<String> lStrings = new HashSet<>();
    lStrings.add("Wonderful");
    lStrings.add("world");
    lBuilder.setQueryParameter("query1", lStrings);
    lBuilder.setQueryParameter("query2", "???");

    lRequest = lBuilder.build();
    assertEquals("/", lRequest.getPath());
    lQueryParams = lRequest.getQueryParameters();
    assertEquals(2, lQueryParams.size());
    lQueryParam = lQueryParams.get("query1");
    assertEquals(2, lQueryParam.size());
    assertTrue(lQueryParam.contains("world"));
    assertTrue(lQueryParam.contains("Wonderful"));

    lQueryParam = lQueryParams.get("query2");
    assertEquals(1, lQueryParam.size());
    assertTrue(lQueryParam.contains("???"));

    // Test null handling
    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (String) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, "Hello");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (String[]) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, "Hello", "World");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, lStrings);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (Collection<String>) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (boolean[]) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, new boolean[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (byte[]) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, new byte[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (short[]) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, new short[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q", (int[]) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, new int[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (long[]) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, new long[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (float[]) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, new float[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (double[]) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, new double[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        (char[]) null);
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter(null, new char[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pQueryParamName' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setQueryParameter("q",
        new String[] {});
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setQueryParameter("boolean", true, false));
    assertEquals(lBuilder, lBuilder.setQueryParameter("byte", (byte) 47, (byte) -2));
    assertEquals(lBuilder, lBuilder.setQueryParameter("short", (short) 241, (short) 17));
    assertEquals(lBuilder, lBuilder.setQueryParameter("integer", 47110815, 1234));
    assertEquals(lBuilder, lBuilder.setQueryParameter("int", 123));
    assertEquals(lBuilder, lBuilder.setQueryParameter("long", (long) 123456789, (long) 12));
    assertEquals(lBuilder, lBuilder.setQueryParameter("double", (double) 123456789.1234, (double) 47.11));
    assertEquals(lBuilder, lBuilder.setQueryParameter("float", (float) 1239.1234, (float) 88.99));
    assertEquals(lBuilder, lBuilder.setQueryParameter("character", 'A', 'B'));

    lRequest = lBuilder.build();
    assertEquals(Integer.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.PATCH, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    Map<String, List<String>> lQueryParameters = lRequest.getQueryParameters();
    assertEquals("true", lQueryParameters.get("boolean").get(0));
    assertEquals("false", lQueryParameters.get("boolean").get(1));
    assertEquals("47", lQueryParameters.get("byte").get(0));
    assertEquals("-2", lQueryParameters.get("byte").get(1));
    assertEquals("241", lQueryParameters.get("short").get(0));
    assertEquals("17", lQueryParameters.get("short").get(1));
    assertEquals("47110815", lQueryParameters.get("integer").get(0));
    assertEquals("1234", lQueryParameters.get("integer").get(1));
    assertEquals("123", lQueryParameters.get("int").get(0));
    assertEquals("123456789", lQueryParameters.get("long").get(0));
    assertEquals("12", lQueryParameters.get("long").get(1));
    assertEquals("1.234567891234E8", lQueryParameters.get("double").get(0));
    assertEquals("47.11", lQueryParameters.get("double").get(1));
    assertEquals("1239.1234", lQueryParameters.get("float").get(0));
    assertEquals("88.99", lQueryParameters.get("float").get(1));
    assertEquals("A", lQueryParameters.get("character").get(0));
    assertEquals("B", lQueryParameters.get("character").get(1));

    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setQueryParameter("boolean", (boolean[]) null));
    assertEquals(lBuilder, lBuilder.setQueryParameter("byte", (byte[]) null));
    assertEquals(lBuilder, lBuilder.setQueryParameter("short", (short[]) null));
    assertEquals(lBuilder, lBuilder.setQueryParameter("integer", (int[]) null));
    assertEquals(lBuilder, lBuilder.setQueryParameter("long", (long[]) null));
    assertEquals(lBuilder, lBuilder.setQueryParameter("double", (double[]) null));
    assertEquals(lBuilder, lBuilder.setQueryParameter("float", (float[]) null));
    assertEquals(lBuilder, lBuilder.setQueryParameter("character", (char[]) null));
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setQueryParameter("boolean", new boolean[] {}));
    assertEquals(lBuilder, lBuilder.setQueryParameter("byte", new byte[] {}));
    assertEquals(lBuilder, lBuilder.setQueryParameter("short", new short[] {}));
    assertEquals(lBuilder, lBuilder.setQueryParameter("integer", new int[] {}));
    assertEquals(lBuilder, lBuilder.setQueryParameter("long", new long[] {}));
    assertEquals(lBuilder, lBuilder.setQueryParameter("double", new double[] {}));
    assertEquals(lBuilder, lBuilder.setQueryParameter("float", new float[] {}));
    assertEquals(lBuilder, lBuilder.setQueryParameter("character", new char[] {}));
    assertEquals(lBuilder, lBuilder.setQueryParameter("strings", new ArrayList<String>()));
    assertEquals(0, lBuilder.build().getQueryParameters().size());

    List<String> lStringLists = new ArrayList<>();
    lStringLists.add("Hello");
    lStringLists.add(null);
    lStringLists.add("World");
    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setQueryParameter("strings", lStringLists));
    assertEquals("[Hello, World]", lBuilder.build().getQueryParameters().get("strings").toString());

    lStringLists = new ArrayList<>();
    lStringLists.add(null);
    lStringLists.add(null);
    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setQueryParameter("strings", lStringLists));
    assertEquals(0, lBuilder.build().getQueryParameters().size());
  }

  @Deprecated
  @Test
  void testDeprecatedQueryParams( ) {
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
    lBuilder.addQueryParam("query1", "World", "!");

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
    lBuilder.addQueryParam("query1", "World", "!");
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
    lBuilder.addQueryParam("query1", "World", "!");
    lBuilder.addQueryParam("query1", "Hello");
    Set<String> lStrings = new HashSet<>();
    lStrings.add("Wonderful");
    lStrings.add("world");
    lBuilder.addQueryParam("query1", lStrings);
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
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (String[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, "Hello", "World");
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, lStrings);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (Collection<String>) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (boolean[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, new boolean[] { false });
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (byte[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, new byte[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (short[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, new short[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (int[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, new int[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (long[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, new long[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (float[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, new float[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (double[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, new double[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam("q", (char[]) null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).addQueryParam(null, new char[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.", e.getMessage());
    }

    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.addQueryParam("boolean", true, false));
    assertEquals(lBuilder, lBuilder.addQueryParam("byte", (byte) 47, (byte) -2));
    assertEquals(lBuilder, lBuilder.addQueryParam("short", (short) 241, (short) 17));
    assertEquals(lBuilder, lBuilder.addQueryParam("integer", 47110815, 1234));
    assertEquals(lBuilder, lBuilder.addQueryParam("int", 123));
    assertEquals(lBuilder, lBuilder.addQueryParam("long", (long) 123456789, (long) 12));
    assertEquals(lBuilder, lBuilder.addQueryParam("double", (double) 123456789.1234, (double) 47.11));
    assertEquals(lBuilder, lBuilder.addQueryParam("float", (float) 1239.1234, (float) 88.99));
    assertEquals(lBuilder, lBuilder.addQueryParam("character", 'A', 'B'));

    lRequest = lBuilder.build();
    assertEquals(Integer.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.PATCH, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    Map<String, List<String>> lQueryParameters = lRequest.getQueryParameters();
    assertEquals("true", lQueryParameters.get("boolean").get(0));
    assertEquals("false", lQueryParameters.get("boolean").get(1));
    assertEquals("47", lQueryParameters.get("byte").get(0));
    assertEquals("-2", lQueryParameters.get("byte").get(1));
    assertEquals("241", lQueryParameters.get("short").get(0));
    assertEquals("17", lQueryParameters.get("short").get(1));
    assertEquals("47110815", lQueryParameters.get("integer").get(0));
    assertEquals("1234", lQueryParameters.get("integer").get(1));
    assertEquals("123", lQueryParameters.get("int").get(0));
    assertEquals("123456789", lQueryParameters.get("long").get(0));
    assertEquals("12", lQueryParameters.get("long").get(1));
    assertEquals("1.234567891234E8", lQueryParameters.get("double").get(0));
    assertEquals("47.11", lQueryParameters.get("double").get(1));
    assertEquals("1239.1234", lQueryParameters.get("float").get(0));
    assertEquals("88.99", lQueryParameters.get("float").get(1));
    assertEquals("A", lQueryParameters.get("character").get(0));
    assertEquals("B", lQueryParameters.get("character").get(1));
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

    assertEquals(1, lRequest.getHeaderFields().size());
    assertEquals("myHeaderValue", lRequest.getHeaderFields().get("My-Header").get(0));

    // Test null handling
    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.POST, ContentType.XML);
    assertNotNull(lBuilder.setHeader("My-Header", (String) null));
    lRequest = lBuilder.build();
    assertEquals(1, lRequest.getHeaders().size());
    assertEquals(null, lRequest.getHeaders().get("My-Header"));
    assertEquals(null, lRequest.getHeaderFields().get("My-Header"));

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

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setHeader(null, new boolean[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setHeader(null, new byte[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setHeader(null, new short[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setHeader(null, new int[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setHeader(null, new long[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setHeader(null, new float[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setHeader(null, new double[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }

    try {
      RESTRequest.builder(String.class, HttpMethod.GET, ContentType.JSON).setHeader(null, new char[] {});
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Parameter 'pHeaderName' must not be null.", e.getMessage());
    }

    // Test other primitive header types
    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setHeader("boolean", Boolean.TRUE));
    assertEquals(lBuilder, lBuilder.setHeader("byte", Byte.valueOf((byte) 47)));
    assertEquals(lBuilder, lBuilder.setHeader("short", Short.valueOf((short) 241)));
    assertEquals(lBuilder, lBuilder.setHeader("integer", Integer.valueOf((int) 47110815)));
    assertEquals(lBuilder, lBuilder.setHeader("int", 123));
    assertEquals(lBuilder, lBuilder.setHeader("long", Long.valueOf((long) 123456789)));
    assertEquals(lBuilder, lBuilder.setHeader("double", Double.valueOf((double) 123456789.1234)));
    assertEquals(lBuilder, lBuilder.setHeader("float", Float.valueOf((float) 1239.1234)));
    assertEquals(lBuilder, lBuilder.setHeader("character", Character.valueOf('A')));

    lRequest = lBuilder.build();
    assertEquals(Integer.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.PATCH, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    Map<String, List<String>> lHeaders = lRequest.getHeaderFields();
    assertEquals("true", lHeaders.get("boolean").get(0));
    assertEquals("47", lHeaders.get("byte").get(0));
    assertEquals("241", lHeaders.get("short").get(0));
    assertEquals("47110815", lHeaders.get("integer").get(0));
    assertEquals("123", lHeaders.get("int").get(0));
    assertEquals("123456789", lHeaders.get("long").get(0));
    assertEquals("1.234567891234E8", lHeaders.get("double").get(0));
    assertEquals("1239.1234", lHeaders.get("float").get(0));
    assertEquals("A", lHeaders.get("character").get(0));

    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setHeader("boolean", true, false));
    assertEquals(lBuilder, lBuilder.setHeader("byte", (byte) 47, (byte) -2));
    assertEquals(lBuilder, lBuilder.setHeader("short", (short) 241, (short) 17));
    assertEquals(lBuilder, lBuilder.setHeader("integer", 47110815, 1234));
    assertEquals(lBuilder, lBuilder.setHeader("int", 123));
    assertEquals(lBuilder, lBuilder.setHeader("long", (long) 123456789, (long) 12));
    assertEquals(lBuilder, lBuilder.setHeader("double", (double) 123456789.1234, (double) 47.11));
    assertEquals(lBuilder, lBuilder.setHeader("float", (float) 1239.1234, (float) 88.99));
    assertEquals(lBuilder, lBuilder.setHeader("character", 'A', 'B'));

    lRequest = lBuilder.build();
    assertEquals(Integer.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.PATCH, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    lHeaders = lRequest.getHeaderFields();
    assertEquals("true", lHeaders.get("boolean").get(0));
    assertEquals("false", lHeaders.get("boolean").get(1));
    assertEquals("47", lHeaders.get("byte").get(0));
    assertEquals("-2", lHeaders.get("byte").get(1));
    assertEquals("241", lHeaders.get("short").get(0));
    assertEquals("17", lHeaders.get("short").get(1));
    assertEquals("47110815", lHeaders.get("integer").get(0));
    assertEquals("1234", lHeaders.get("integer").get(1));
    assertEquals("123", lHeaders.get("int").get(0));
    assertEquals("123456789", lHeaders.get("long").get(0));
    assertEquals("12", lHeaders.get("long").get(1));
    assertEquals("1.234567891234E8", lHeaders.get("double").get(0));
    assertEquals("47.11", lHeaders.get("double").get(1));
    assertEquals("1239.1234", lHeaders.get("float").get(0));
    assertEquals("88.99", lHeaders.get("float").get(1));
    assertEquals("A", lHeaders.get("character").get(0));
    assertEquals("B", lHeaders.get("character").get(1));

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
    assertEquals(8, lBuilder.build().getHeaderFields().size());

    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setHeader("boolean", (boolean[]) null));
    assertEquals(lBuilder, lBuilder.setHeader("byte", (byte[]) null));
    assertEquals(lBuilder, lBuilder.setHeader("short", (short[]) null));
    assertEquals(lBuilder, lBuilder.setHeader("integer", (int[]) null));
    assertEquals(lBuilder, lBuilder.setHeader("long", (long[]) null));
    assertEquals(lBuilder, lBuilder.setHeader("double", (double[]) null));
    assertEquals(lBuilder, lBuilder.setHeader("float", (float[]) null));
    assertEquals(lBuilder, lBuilder.setHeader("character", (char[]) null));
    assertEquals(8, lBuilder.build().getHeaderFields().size());

    lRequest = lBuilder.build();
    assertEquals(Integer.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.PATCH, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    lHeaders = lRequest.getHeaderFields();
    assertEquals(null, lHeaders.get("boolean"));
    assertEquals(null, lHeaders.get("byte"));
    assertEquals(null, lHeaders.get("short"));
    assertEquals(null, lHeaders.get("integer"));
    assertEquals(null, lHeaders.get("long"));
    assertEquals(null, lHeaders.get("double"));
    assertEquals(null, lHeaders.get("float"));
    assertEquals(null, lHeaders.get("character"));

    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setHeader("boolean", new boolean[] {}));
    assertEquals(lBuilder, lBuilder.setHeader("byte", new byte[] {}));
    assertEquals(lBuilder, lBuilder.setHeader("short", new short[] {}));
    assertEquals(lBuilder, lBuilder.setHeader("integer", new int[] {}));
    assertEquals(lBuilder, lBuilder.setHeader("long", new long[] {}));
    assertEquals(lBuilder, lBuilder.setHeader("double", new double[] {}));
    assertEquals(lBuilder, lBuilder.setHeader("float", new float[] {}));
    assertEquals(lBuilder, lBuilder.setHeader("character", new char[] {}));
    assertEquals(8, lBuilder.build().getHeaderFields().size());

    lRequest = lBuilder.build();
    assertEquals(Integer.class, lRequest.getServiceClass());
    assertEquals(HttpMethod.PATCH, lRequest.getHttpMethod());
    assertEquals(ContentType.JSON, lRequest.getContentType());
    lHeaders = lRequest.getHeaderFields();
    assertEquals(null, lHeaders.get("boolean"));
    assertEquals(null, lHeaders.get("byte"));
    assertEquals(null, lHeaders.get("short"));
    assertEquals(null, lHeaders.get("integer"));
    assertEquals(null, lHeaders.get("long"));
    assertEquals(null, lHeaders.get("double"));
    assertEquals(null, lHeaders.get("float"));
    assertEquals(null, lHeaders.get("character"));

    lBuilder = RESTRequest.builder(Integer.class, HttpMethod.PATCH, ContentType.JSON);
    assertEquals(lBuilder, lBuilder.setHeader("byte", (byte) 47, (byte) -2));
    lRequest = lBuilder.build();
    Map<String, String> lDeprecatedHeaders = lRequest.getHeaders();
    assertEquals("47, -2", lDeprecatedHeaders.get("byte"));

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
