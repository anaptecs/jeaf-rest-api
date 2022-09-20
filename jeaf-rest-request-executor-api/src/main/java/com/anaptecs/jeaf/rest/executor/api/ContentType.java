/**
 * anaptecs GmbH, Ricarda-Huch-Str. 71, 72760 Reutlingen, Germany
 * 
 * Copyright 2004 - 2022. All rights reserved.
 */
package com.anaptecs.jeaf.rest.executor.api;

/**
 * Enumeration defines the supported content types for REST calls
 * 
 * @author JEAF Development Team
 */
public enum ContentType {
  JSON("application/json"), XML("application/xml");

  /**
   * Mime type that belongs to the content type-
   */
  private final String mimeType;

  /**
   * Initialize enumeration.
   * 
   * @param pMimeType Mime type the belong to the content type. The parameter must not be null.
   */
  private ContentType( String pMimeType ) {
    mimeType = pMimeType;
  }

  /**
   * Method returns the mime type that belongs to this content type.
   * 
   * @return String Mime type. The method never returns null.
   */
  public String getMimeType( ) {
    return mimeType;
  }
}
