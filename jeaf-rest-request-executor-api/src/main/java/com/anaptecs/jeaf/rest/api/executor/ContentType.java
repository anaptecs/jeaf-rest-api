/**
 * anaptecs GmbH, Ricarda-Huch-Str. 71, 72760 Reutlingen, Germany
 * 
 * Copyright 2004 - 2022. All rights reserved.
 */
package com.anaptecs.jeaf.rest.api.executor;

public enum ContentType {
  JSON("application/json"), XML("application/xml");

  private final String mimeType;

  private ContentType( String pMimeType ) {
    mimeType = pMimeType;
  }

  public String getMimeType( ) {
    return mimeType;
  }
}
