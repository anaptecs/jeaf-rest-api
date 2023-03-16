package com.anaptecs.jeaf.rest.resource.api;

/**
 * Interface defines a very light-weight approach to request and response validation in RESt environments. Through this
 * interface generated REST Resource / Controllers can trigger request and response validation very easily.
 * 
 * @author JEAF Development Team
 */
public interface ValidationExecutor {
  /**
   * Method validates the passed request objects. It is expected that implementations throw a runtime exception that
   * describes all the validations that failed.
   * 
   * @param pRequestObject Request object that should be validated. The parameter may be null.
   * @param pService Class object representing the service to which the passed request object belongs to. The parameter
   * must not be null.
   */
  void validateRequest( Object pRequestObject, Class<?> pService );

  /**
   * Method validates the passed response object. It is expected that implementations throw a runtime exception that
   * describes all the validations that failed.
   * 
   * @param pResponseObject Response Request object that should be validated. The parameter may be null.
   * @param pService Class object representing the service to which the passed request object belongs to. The parameter
   * must not be null.
   */
  void validateResponse( Object pResponseObject, Class<?> pService );
}
