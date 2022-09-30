/**
 * anaptecs GmbH, Ricarda-Huch-Str. 71, 72760 Reutlingen, Germany
 * 
 * Copyright 2004 - 2022. All rights reserved.
 */
package com.anaptecs.jeaf.rest.executor.api;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class defines a generic REST request that is used as an abstraction of for concrete implementations together with
 * {@link RESTRequestExecutor}.
 * 
 * @author JEAF Development Team
 */
public class RESTRequest {
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
  private final Map<String, String> headers;

  /**
   * Query params that belong to the request. Please be aware that for query params it is supported to have more than
   * one value for it.
   */
  private final Map<String, Set<String>> queryParams;

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
    headers = new HashMap<>(pBuilder.headers);
    queryParams = new HashMap<>(pBuilder.queryParams);
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
   * Method returns the http headers that should be sent as part of the request.
   * 
   * @return {@link Map} All http headers that should be sent as part of the request. The method never returns null.
   */
  public Map<String, String> getHeaders( ) {
    return Collections.unmodifiableMap(headers);
  }

  /**
   * Method returns the query parameters that should be sent as part of the request. Please be aware that for query
   * params it is supported to have more than one value for it.
   * 
   * @return {@link Map} All query parameters that should be sent as part of the request. The method never returns null.
   */
  public Map<String, Set<String>> getQueryParams( ) {
    return Collections.unmodifiableMap(queryParams);
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
     * @see RESTRequest#headers
     */
    private final Map<String, String> headers = new HashMap<>();

    /**
     * @see RESTRequest#queryParams
     */
    private final Map<String, Set<String>> queryParams = new HashMap<>();

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
     * be overwritten.
     * 
     * @param pHeaderName Name of the header. The parameter must not be null.
     * @param pHeaderValue Header value that should be set. The parameter may be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder setHeader( String pHeaderName, String pHeaderValue ) {
      if (pHeaderName != null) {
        headers.put(pHeaderName, pHeaderValue);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    /**
     * Method adds the request parameter with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query params it is supported to have more than one value for it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValue Value of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder addQueryParam( String pQueryParamName, String pQueryParamValue ) {
      if (pQueryParamName != null && pQueryParamValue != null) {
        Set<String> lValues = queryParams.get(pQueryParamName);
        if (lValues == null) {
          lValues = new HashSet<>();
          queryParams.put(pQueryParamName, lValues);
        }

        lValues.add(pQueryParamValue);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValue' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query params it is supported to have more than one value for it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder addQueryParams( String pQueryParamName, Collection<String> pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        for (String lNextValue : pQueryParamValues) {
          this.addQueryParam(pQueryParamName, lNextValue);
        }
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pQueryParamName' and 'pQueryParamValues' must not be null.");
      }
    }

    /**
     * Method adds the request parameters with the passed name. May be already existing query parameters with the same
     * name will be extended. Please be aware that for query params it is supported to have more than one value for it.
     * 
     * @param pQueryParamName Name of the query parameter. The parameter must not be null.
     * @param pQueryParamValues Values of the query parameter. The parameter must not be null.
     * @return {@link Builder} Builder object to concatenate calls to builder. The method never returns null.
     */
    public Builder addQueryParams( String pQueryParamName, String... pQueryParamValues ) {
      if (pQueryParamName != null && pQueryParamValues != null) {
        for (String lNextValue : pQueryParamValues) {
          this.addQueryParam(pQueryParamName, lNextValue);
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
  }
}
