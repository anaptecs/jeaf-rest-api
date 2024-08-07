/**
 * anaptecs GmbH, Ricarda-Huch-Str. 71, 72760 Reutlingen, Germany
 * 
 * Copyright 2004 - 2022. All rights reserved.
 */
package com.anaptecs.jeaf.rest.executor.api;

import java.util.Collection;

/**
 * Interface defines an abstraction for synchronous calls to an REST resource. Idea of this interface is to provide a
 * generic interface for REST calls. Providers of this interface can use any kind of http client to execute the REST
 * requests that are received via this interface.
 * 
 * Responsibilities of implementations:
 * <ul>
 * <li>Ensure http compatibility of query parameters (they need to be URL encoded)</li>
 * <li>Conversion of body to defined content-type. Support for JSON is mandatory, XML is optional.</li>
 * <li>Proper handling of passed cookies</li>
 * <li>Problem-Handling: For technical problems as well as in case that the REST resource returns a problem. It is
 * expected that a runtime exception will be thrown that represents the occurred problem e.g. using Zalando Problem
 * classes</li>
 * <li>It is expected that implementations make use of circuit breakers and http connection pooling if this is
 * meaningful for the concrete scenarios.</li>
 * <li>It is expected that implementations provide some kind of configuration mechanism that is able to use the matching
 * configuration based on the passed service class (@see {@link RESTRequest#getServiceClass()}).</li>
 * </ul>
 * 
 * @author JEAF Development Team
 */
public interface RESTRequestExecutor {
  /**
   * Method executes a HTTP REST request that is expected to return no response (aka return type void). The REST
   * resource that should be called can be resolved using the service class that is defined in the passed request.
   * {@link RESTRequest#getServiceClass()}.
   * 
   * @param pRequest HTTP request that should be executed. The parameter must not be null.
   * @param pSuccessfulStatusCode HTTP status code that represents a successful call. This status code is required in
   * order to be able to distinguish between successful and failed requests. In case of failed requests an runtime
   * exception is expected to be thrown.
   */
  void executeNoResultRequest( RESTRequest pRequest, int pSuccessfulStatusCode );

  /**
   * Method executes a HTTP REST request that is expected to return a single non collection object as result.
   * 
   * @param pRequest HTTP request that should be executed. The parameter must not be null.
   * @param pSuccessfulStatusCode HTTP status code that represents a successful call. This status code is required in
   * order to be able to distinguish between successful and failed requests. In case of failed requests an runtime
   * exception is expected to be thrown.
   * @param pObjectType Type of the object that will be returned by the call. The parameter must not be null.
   * @return T Single object as it was defined by <code>pTypeClass</code>
   */
  <T> T executeSingleObjectResultRequest( RESTRequest pRequest, int pSuccessfulStatusCode,
      ObjectType pObjectType );

  /**
   * Method executes a HTTP request that is expected to return a collection of objects as result.
   * 
   * @param pRequest HTTP request that should be executed. The parameter must not be null.
   * @param pSuccessfulStatusCode HTTP status code that represents a successful call. This status code is required in
   * order to be able to distinguish between successful and failed requests. In case of failed requests an runtime
   * exception is expected to be thrown.
   * @param pCollectionClass Class object of collection class that should be returned e.g. List. The parameter must not
   * be null.
   * @param pObjectType Type of the objects that will be inside the collection. The parameter must not be null.
   * @return {@link Collection} of objects as it was defined by <code>pCollectionClass</code> and
   * <code>pObjectType</code>
   */
  <T> T executeCollectionResultRequest( RESTRequest pRequest, int pSuccessfulStatusCode,
      @SuppressWarnings("rawtypes")
      Class<? extends Collection> pCollectionClass, ObjectType pObjectType );
}
