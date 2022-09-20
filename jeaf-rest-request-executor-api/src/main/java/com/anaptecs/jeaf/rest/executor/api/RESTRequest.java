/**
 * anaptecs GmbH, Ricarda-Huch-Str. 71, 72760 Reutlingen, Germany
 * 
 * Copyright 2004 - 2022. All rights reserved.
 */
package com.anaptecs.jeaf.rest.executor.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class defines a generic REST request that is used as an abstraction of for concrete implementations together with
 * {@link RESTRequestExecutor}.
 * 
 * @author JEAF Development Team
 */
public class RESTRequest {
  /**
   * Based on the service class {@link RESTRequestExecutor} is able to resolve the URL of the REST resource that should
   * be called as well as all required configuration parameters.
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
   * Query params that belong to the request.
   */
  private final Map<String, String> queryParams;

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

  public Class<?> getServiceClass( ) {
    return serviceClass;
  }

  public HttpMethod getHttpMethod( ) {
    return httpMethod;
  }

  public String getPath( ) {
    return path;
  }

  public Map<String, String> getHeaders( ) {
    return Collections.unmodifiableMap(headers);
  }

  public Map<String, String> getQueryParams( ) {
    return Collections.unmodifiableMap(queryParams);
  }

  public Map<String, String> getCookies( ) {
    return Collections.unmodifiableMap(cookies);
  }

  public Object getBody( ) {
    return body;
  }

  public ContentType getContentType( ) {
    return contentType;
  }

  public static class Builder {
    private final Class<?> serviceClass;

    private final HttpMethod httpMethod;

    private String path = "/";

    private final Map<String, String> headers = new HashMap<>();

    private final Map<String, String> queryParams = new HashMap<>();

    private final Map<String, String> cookies = new HashMap<>();

    private Object body;

    private final ContentType contentType;

    public static Builder newBuilder( Class<?> pServiceClass, HttpMethod pHttpMethod, ContentType pContentType ) {
      return new Builder(pServiceClass, pHttpMethod, pContentType);
    }

    private Builder( Class<?> pServiceClass, HttpMethod pHttpMethod, ContentType pContentType ) {
      if (pServiceClass != null && pHttpMethod != null && pContentType != null) {
        serviceClass = pServiceClass;
        httpMethod = pHttpMethod;
        contentType = pContentType;
      }
      else {
        throw new IllegalArgumentException("Parameters 'pServiceClass' and 'pHttpMethod' must not be null.");
      }
    }

    public Builder setPath( String pPath ) {
      if (pPath != null) {
        path = pPath;
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pPath' must not be null.");
      }
    }

    public Builder setHeader( String pHeaderName, String pHeaderValue ) {
      if (pHeaderName != null) {
        headers.put(pHeaderName, pHeaderValue);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pHeaderName' must not be null.");
      }
    }

    public Builder setQueryParam( String pQueryParamName, String pQueryParamValue ) {
      if (pQueryParamName != null && pQueryParamValue != null) {
        queryParams.put(pQueryParamName, pQueryParamValue);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pQueryParamName' and 'pQueryParamValue' must not be null.");
      }
    }

    public Builder setCookie( String pCookieName, String pCookieValue ) {
      if (pCookieName != null) {
        cookies.put(pCookieName, pCookieValue);
        return this;
      }
      else {
        throw new IllegalArgumentException("Parameter 'pCookieName' must not be null.");
      }
    }

    public Builder setBody( Object pBody ) {
      body = pBody;
      return this;
    }

    public RESTRequest build( ) {
      return new RESTRequest(this);
    }
  }
}
