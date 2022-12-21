/**
 * anaptecs GmbH, Ricarda-Huch-Str. 71, 72760 Reutlingen, Germany
 * 
 * Copyright 2004 - 2022. All rights reserved.
 */
package com.anaptecs.jeaf.rest.executor.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class defines a generic REST request that is used as an abstraction of for concrete implementations together with
 * {@link RESTRequestExecutor}.
 * 
 * @author JEAF Development Team
 */
public class RESTRequest {
  /**
   * Delimiter for header fields in case that there are multiple values for one header.
   */
  private static final String DELIMITER = ", ";

  /**
   * Based on the service class a REST executor ({@link RESTRequestExecutor}) is able to resolve the URL of the REST
   * resource that should be called as well as all other configuration parameters.
   */
  private final Class<?> serviceClass;

  /**
   * Http method that should be used to execute the REST call.
   */
  private final HttpMethod httpMethod;

  /**
   * Resource path that should be called.
   */
  private final String path;

  /**
   * Http request headers that belong to the request.
   */
  private final Map<String, List<String>> headerFields;

  /**
   * Query parameters that belong to the request. Please be aware that for query parameters it is supported to have more
   * than one value for it.
   */
  private final Map<String, List<String>> queryParameters;

  /**
   * Cookies that belong to the request
   */
  private final Map<String, String> cookies;

  /**
   * Object representing the body of the request. Please be aware that the body here needs to be transformed into the
   * matching content type according to {@link #contentType} before the http request can actually be sent.
   */
  private final Object body;

  /**
   * Content type that should be used for communication with the REST resource. Please be aware that we do not
   * distinguish content type between request and response. This means that based on this attribute body conversion has
   * to be done as well as setting the required standard header fields.
   */
  private final ContentType contentType;

  /**
   * Method creates new builder. All mandatory parameters already have to be passed here.
   * 
   * @param pServiceClass Based on the service class a REST executor ({@link RESTRequestExecutor}) is able to resolve
   * the URL of the REST resource that should be called as well as all other configuration parameters. The parameter
   * must not be null.
   * @param pHttpMethod Http method that should be used to execute the REST call. The parameter must not be null.
   * @param pContentType Content type that should be used for communication with the REST resource. Please be aware that
   * we do not distinguish content type between request and response. This means that based on this attribute body
   * conversion has to be done as well as setting the required standard header fields. The parameter must not be null.
   * @return {@link Builder} Created builder. The method never returns null.
   */
  public static Builder builder( Class<?> pServiceClass, HttpMethod pHttpMethod, ContentType pContentType ) {
    return new Builder(pServiceClass, pHttpMethod, pContentType);
  }

  /**
   * Initialize object.
   * 
   * @param pBuilder
   */
  private RESTRequest( Builder pBuilder ) {
    serviceClass = pBuilder.serviceClass;
    httpMethod = pBuilder.httpMethod;
    path = pBuilder.path;
    headerFields = new HashMap<>(pBuilder.headerFields);
    queryParameters = new HashMap<>(pBuilder.queryParameters);
    cookies = new HashMap<>(pBuilder.cookies);
    body = pBuilder.body;
    contentType = pBuilder.contentType;
  }

  /**
   * Method returns the class object representing the service that should be called. Based on the service class a REST
   * executor ({@link RESTRequestExecutor}) is able to resolve the URL of the REST resource that should be called as
   * well as all other configuration parameters.
   * 
   * @return {@link Class} Class object representing the REST service. The method never returns null.
   */
  public Class<?> getServiceClass( ) {
    return serviceClass;
  }

  /**
   * Method returns the http method that should be used for the REST call.
   * 
   * @return {@link HttpMethod} Http method that should be used. The method never returns null.
   */
  public HttpMethod getHttpMethod( ) {
    return httpMethod;
  }

  /**
   * Method returns the resource path for the request.
   * 
   * @return {@link String} Resource path that should be called. The method never returns null.
   */
  public String getPath( ) {
    return path;
  }

  /**
   * Method returns the http headers that should be sent as part of the request. In case that there is more then one
   * value for a header field the the values will be concatenated using ','.
   * 
   * @return {@link Map} All http headers that should be sent as part of the request. The method never returns null.
   * 
   * @see #getHeaderFields()
   */
  @Deprecated
  public Map<String, String> getHeaders( ) {
    Map<String, String> lHeaders = new HashMap<>();
    for (Entry<String, List<String>> lNext : headerFields.entrySet()) {
      StringBuilder lBuffer = new StringBuilder();
      List<String> lValue = lNext.getValue();
      if (lValue != null) {
        Iterator<String> lIterator = lValue.iterator();
        while (lIterator.hasNext()) {
          lBuffer.append(lIterator.next());
          if (lIterator.hasNext()) {
            lBuffer.append(DELIMITER);
          }
          lHeaders.put(lNext.getKey(), lBuffer.toString());
        }
      }
      else {
        lHeaders.put(lNext.getKey(), null);
      }
    }
    return lHeaders;
  }

  /**
   * Method returns the http headers that should be sent as part of the request. Please be aware there mights be more
   * then one value for a header field.
   * 
   * @return {@link Map} All http headers that should be sent as part of the request. The method never returns null.
   */
  public Map<String, List<String>> getHeaderFields( ) {
    return Collections.unmodifiableMap(headerFields);
  }

  /**
   * Method returns the query parameters that should be sent as part of the request. Please be aware that for query
   * params it is supported to have more than one value for it.
   * 
   * @return {@link Map} All query parameters that should be sent as part of the request. The method never returns null.
   * @deprecated Please use {@link #getQueryParameters()}
   */
  @Deprecated
  public Map<String, Set<String>> getQueryParams( ) {
    Map<String, Set<String>> lEntries = new HashMap<>();
    for (Entry<String, List<String>> lNext : queryParameters.entrySet()) {
      lEntries.put(lNext.getKey(), new HashSet<>(lNext.getValue()));
    }
    return Collections.unmodifiableMap(lEntries);
  }

  /**
   * Method returns the query parameters that should be sent as part of the request. Please be aware that for query
   * params it is supported to have more than one value for it.
   * 
   * @return {@link Map} All query parameters that should be sent as part of the request. The method never returns null.
   */
  public Map<String, List<String>> getQueryParameters( ) {
    return Collections.unmodifiableMap(queryParameters);
  }

  /**
   * Method returns the cookies that should be sent as part of the request.
   * 
   * @return {@link Map} All cookies that should be sent as part of the request.
   */
  public Map<String, String> getCookies( ) {
    return Collections.unmodifiableMap(cookies);
  }

  /**
   * Method returns the object that should be sent as body to the REST resource.
   * 
   * @return {@link Object} Object representing the body. The method may return null.
   */
  public Object getBody( ) {
    return body;
  }

  /**
   * Method returns the content type that should be used for request and response.
   * 
   * @return {@link ContentType} Content type that should be used for request and response. The method never returns
   * null.
   */
  public ContentType getContentType( ) {
    return contentType;
  }

  public static class Builder {
    /**
     * @see RESTRequest#serviceClass
     */
    private final Class<?> serviceClass;

    /**
     * @see RESTRequest#httpMethod
     */
    private final HttpMethod httpMethod;

    /**
     * @see RESTRequest#path
     */
    private String path = "/";

    /**
     * @see RESTRequest#headerFields
     */
    private final Map<String, List<String>> headerFields = new HashMap<>();

    /**
     * @see RESTRequest#queryParameters
     */
    private final Map<String, List<String>> queryParameters = new HashMap<>();

    /**
     * @see RESTRequest#cookies
     */
    private final Map<String, String> cookies = new HashMap<>();

    /**
     * @see RESTRequest#body
     */
    private Object body;

    /**
     * @see RESTRequest#contentType
     */
    private final ContentType contentType;

    /**
     * Initialize object.
     * 
     * @param pServiceClass Based on the service class a REST executor ({@link RESTRequestExecutor}) is able to resolve
     * the URL of the REST resource that should be called as well as all other configuration parameters. The parameter
     * must not be null.
     * @param pHttpMethod Http method that should be used to execute the REST call. The parameter must not be null.
     * @param pContentType Content type that should be used for communication with the REST resource. Please be aware
     * that we do not distinguish content type between request and response. This means that based on this attribute
     * body conversion has to be done as well as setting the required standard header fields. The parameter must not be
     * null.
     */
    private Builder( Class<?> pServiceClass, HttpMethod pHttpMethod, ContentType pContentType ) {
      // Check parameters for null.
      if (pServiceClass != null && pHttpMethod != null && pContentType != null) {
        serviceClass = pServiceClass;
        httpMethod = pHttpMethod;
        contentType = pContentType;
      }
      // Parameters must not be null.
      else {
        throw new IllegalArgumentException(
            "Parameters 'pServiceClass', 'pHttpMethod' and 'pContentType' must not be null.");
      }
    }

    /**
     * Method sets the resource path that should be called.
     * 
     * @param pPath Resource path that should be called. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setPath( String pPath ) {
      if (pPath != null) {
        path = pPath;
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pPath' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, boolean... pHeaderValues ) {
      if (pHeaderName != null) {
        List<String> lValues;
        if (pHeaderValues != null && pHeaderValues.length > 0) {
          lValues = new ArrayList<>(pHeaderValues.length);
          for (boolean lNextValue : pHeaderValues) {
            lValues.add(String.valueOf(lNextValue));
          }
        }
        else {
          lValues = null;
        }
        headerFields.put(pHeaderName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, byte... pHeaderValues ) {
      if (pHeaderName != null) {
        List<String> lValues;
        if (pHeaderValues != null && pHeaderValues.length > 0) {
          lValues = new ArrayList<>(pHeaderValues.length);
          for (byte lNextValue : pHeaderValues) {
            lValues.add(String.valueOf(lNextValue));
          }
        }
        else {
          lValues = null;
        }
        headerFields.put(pHeaderName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, short... pHeaderValues ) {
      if (pHeaderName != null) {
        List<String> lValues;
        if (pHeaderValues != null && pHeaderValues.length > 0) {
          lValues = new ArrayList<>(pHeaderValues.length);
          for (short lNextValue : pHeaderValues) {
            lValues.add(String.valueOf(lNextValue));
          }
        }
        else {
          lValues = null;
        }
        headerFields.put(pHeaderName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, int... pHeaderValues ) {
      if (pHeaderName != null) {
        List<String> lValues;
        if (pHeaderValues != null && pHeaderValues.length > 0) {
          lValues = new ArrayList<>(pHeaderValues.length);
          for (int lNextValue : pHeaderValues) {
            lValues.add(String.valueOf(lNextValue));
          }
        }
        else {
          lValues = null;
        }
        headerFields.put(pHeaderName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, long... pHeaderValues ) {
      if (pHeaderName != null) {
        List<String> lValues;
        if (pHeaderValues != null && pHeaderValues.length > 0) {
          lValues = new ArrayList<>(pHeaderValues.length);
          for (long lNextValue : pHeaderValues) {
            lValues.add(String.valueOf(lNextValue));
          }
        }
        else {
          lValues = null;
        }
        headerFields.put(pHeaderName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, float... pHeaderValues ) {
      if (pHeaderName != null) {
        List<String> lValues;
        if (pHeaderValues != null && pHeaderValues.length > 0) {
          lValues = new ArrayList<>(pHeaderValues.length);
          for (float lNextValue : pHeaderValues) {
            lValues.add(String.valueOf(lNextValue));
          }
        }
        else {
          lValues = null;
        }
        headerFields.put(pHeaderName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, double... pHeaderValues ) {
      if (pHeaderName != null) {
        List<String> lValues;
        if (pHeaderValues != null && pHeaderValues.length > 0) {
          lValues = new ArrayList<>(pHeaderValues.length);
          for (double lNextValue : pHeaderValues) {
            lValues.add(String.valueOf(lNextValue));
          }
        }
        else {
          lValues = null;
        }
        headerFields.put(pHeaderName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, char... pHeaderValues ) {
      if (pHeaderName != null) {
        List<String> lValues;
        if (pHeaderValues != null && pHeaderValues.length > 0) {
          lValues = new ArrayList<>(pHeaderValues.length);
          for (char lNextValue : pHeaderValues) {
            lValues.add(String.valueOf(lNextValue));
          }
        }
        else {
          lValues = null;
        }
        headerFields.put(pHeaderName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String} using {@link String#toString()}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, String... pHeaderValues ) {
      List<String> lHeaderValueList;
      if (pHeaderValues != null) {
        lHeaderValueList = Arrays.asList(pHeaderValues);
      }
      else {
        lHeaderValueList = null;
      }
      return this.setHeader(pHeaderName, lHeaderValueList);
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValues Header values that should be set. The parameter may be null. All passed values will be
     * converted into a {@link String} using {@link String#toString()}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, Collection<?> pHeaderValues ) {
      if (pHeaderName != null) {
        List<String> lValues;
        if (pHeaderValues != null) {
          lValues = new ArrayList<>(pHeaderValues.size());
          for (Object lNext : pHeaderValues) {
            lValues.add(lNext.toString());
          }
        }
        else {
          lValues = null;
        }
        headerFields.put(pHeaderName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method sets the http request header with the passed name. May be already existing header with the same name will
     * be overwritten. Please be aware that for header fields it is supported to have more than one value for it.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValue Header value that should be set. The parameter may be null. The parameter will be converted
     * into a {@link String} using {@link String#toString()}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, Object pHeaderValue ) {
      String[] lValue;
      if (pHeaderValue != null) {
        lValue = new String[] { pHeaderValue.toString() };
      }
      else {
        lValue = null;
      }
      return this.setHeader(pHeaderName, lValue);
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, boolean... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null && pQueryParamValues.length > 0) {
        List<String> lValues = new ArrayList<>(pQueryParamValues.length);
        for (boolean lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        queryParameters.put(pQueryParamName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, byte... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null && pQueryParamValues.length > 0) {
        List<String> lValues = new ArrayList<>(pQueryParamValues.length);
        for (byte lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        queryParameters.put(pQueryParamName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, short... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null && pQueryParamValues.length > 0) {
        List<String> lValues = new ArrayList<>(pQueryParamValues.length);
        for (short lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        queryParameters.put(pQueryParamName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, int... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null && pQueryParamValues.length > 0) {
        List<String> lValues = new ArrayList<>(pQueryParamValues.length);
        for (int lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        queryParameters.put(pQueryParamName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, long... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null && pQueryParamValues.length > 0) {
        List<String> lValues = new ArrayList<>(pQueryParamValues.length);
        for (long lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        queryParameters.put(pQueryParamName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, float... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null && pQueryParamValues.length > 0) {
        List<String> lValues = new ArrayList<>(pQueryParamValues.length);
        for (float lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        queryParameters.put(pQueryParamName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, double... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null && pQueryParamValues.length > 0) {
        List<String> lValues = new ArrayList<>(pQueryParamValues.length);
        for (double lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        queryParameters.put(pQueryParamName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, char... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null && pQueryParamValues.length > 0) {
        List<String> lValues = new ArrayList<>(pQueryParamValues.length);
        for (char lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        queryParameters.put(pQueryParamName, lValues);
        return this;
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, String... pQueryParamValues ) {
      if (pQueryParamValues != null) {
        return this.setQueryParameter(pQueryParamName, Arrays.asList(pQueryParamValues));
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null and must contain at least
     * one real value.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, Collection<?> pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null && pQueryParamValues.isEmpty() == false) {
        List<String> lQueryParamValueList;
        lQueryParamValueList = new ArrayList<>(pQueryParamValues.size());
        for (Object lNext : pQueryParamValues) {
          lQueryParamValueList.add(lNext != null ? lNext.toString() : null);
        }
        queryParameters.put(pQueryParamName, lQueryParamValueList);
        return this;
      }
      else {
        throw new IllegalArgumentException(
            "Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null and at least one value for a query parameter must be provided");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValue Value of the query parameter. The parameter must not be null. The parameter will be
     * converted into a {@link String} using {@link String#toString()}.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setQueryParameter( String pQueryParamName, Object pQueryParamValue ) {
      if (pQueryParamValue != null) {
        return this.setQueryParameter(pQueryParamName, new String[] { pQueryParamValue.toString() });
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValue' must not be null.");
      }
    }

    /**
     * Method adds the request parameter with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValue Value of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, Object pQueryParamValue ) {
      if (pQueryParamName != null && pQueryParamValue != null) {
        List<String> lValues = getQueryParamSet(pQueryParamName);
        lValues.add(pQueryParamValue.toString());
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValue' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, Collection<?> pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (Object lNextValue : pQueryParamValues) {
          if (lNextValue != null) {
            lValues.add(lNextValue.toString());
          }
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, String... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (Object lNextValue : pQueryParamValues) {
          lValues.add(lNextValue.toString());
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, boolean... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (boolean lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, byte... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (byte lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, short... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (short lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, int... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (int lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, long... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (long lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, float... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (float lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, double... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (double lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query parameters it is supported to have more than one value for
     * it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    @Deprecated
    public Builder addQueryParam( String pQueryParamName, char... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        List<String> lValues = this.getQueryParamSet(pQueryParamName);
        for (char lNextValue : pQueryParamValues) {
          lValues.add(String.valueOf(lNextValue));
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method sets the cookie with the passed name. May be already existing cookies with the same name will be
     * overwritten.
     * 
     * @param pCookieName Name of the cookie. The parameter must not be null.
     * @param pCookieValue Value of the cookie. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setCookie( String pCookieName, String pCookieValue ) {
      if (pCookieName != null) {
        cookies.put(pCookieName, pCookieValue);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pCookieName' must not be null.");
      }
    }

    /**
     * Method sets the body of the request. Depending of the defined {@link #contentType} this body object will be
     * serialized by the REST request executor {@link RESTRequestExecutor} to the matching format.
     * 
     * @param pBody Object that should be sent as body. The parameter may be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setBody( Object pBody ) {
      body = pBody;
      return this;
    }

    /**
     * Method build the {@link RESTRequest} object based on the set values.
     * 
     * @return {@link RESTRequest} Created object. The method never returns null.
     */
    public RESTRequest build( ) {
      return new RESTRequest(this);
    }

    private List<String> getQueryParamSet( String pQueryParamName ) {
      return queryParameters.computeIfAbsent(pQueryParamName, k -> new ArrayList<>(1));
    }
  }
}
