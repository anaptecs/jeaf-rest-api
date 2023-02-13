/**
 * anaptecs GmbH, Ricarda-Huch-Str. 71, 72760 Reutlingen, Germany
 * 
 * Copyright 2004 - 2023. All rights reserved.
 */
package com.anaptecs.jeaf.rest.resource.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Custom header filter is intended to be used to filter the HTTP headers that are supported by a resource. This filter
 * is used in case of so called custom headers for generated REST resources.
 * <p/>
 * Custom HTTP headers are additional headers that a resource accepts, but that are not part of the API specification
 * and might be changed without prior notice.
 * <p/>
 * When implementing this interface please be aware that http headers are not case sensitive.
 * <p/>
 * Example: {@code x-conversation-id}
 * 
 * @author JEAF Development Team
 */
@FunctionalInterface
public interface CustomHeaderFilter extends Predicate<String> {

  /**
   * Checks if the passed header name is supported by a resource.
   * 
   * @param pHeaderName The HTTP header name
   * @return {@code true} if this is a custom header that is supported by this resource.
   */
  @Override
  boolean test( String pHeaderName );

  /**
   * Method can be used to create a Custom header filter directly from a list of strings.
   * 
   * @param pSupportedHeaderNames A of positive list of HTTP header names. The parameter must not be null.
   * @return {@link CustomHeaderFilter} CustomHeaderFilter that was build based on the passe collections. It uses
   * {@code collection::contains} to recognize supported headers.
   */
  static CustomHeaderFilter from( Collection<String> pSupportedHeaderNames ) {
    return new CustomHeaderFilterImpl(pSupportedHeaderNames);
  }

  static CustomHeaderFilter from( String... pSupportedHeaderNames ) {
    return new CustomHeaderFilterImpl(Arrays.asList(pSupportedHeaderNames));
  }

  /**
   * Class provides a default implementation of a custom header filter.
   */
  static class CustomHeaderFilterImpl implements CustomHeaderFilter {
    /**
     * List contains all supported header field names in lower case format.
     */
    private final List<String> supportedHeaders;

    private CustomHeaderFilterImpl( Collection<String> pSupportedHeaders ) {
      supportedHeaders = new ArrayList<>(pSupportedHeaders.size());
      for (String lNext : pSupportedHeaders) {
        supportedHeaders.add(lNext.toLowerCase());
      }
    }

    @Override
    public boolean test( String pHeaderName ) {
      return supportedHeaders.contains(pHeaderName.toLowerCase());
    }
  }
}