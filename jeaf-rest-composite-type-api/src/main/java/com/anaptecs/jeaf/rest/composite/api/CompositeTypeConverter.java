/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.rest.composite.api;

import java.util.List;

/**
 * Interface defines operations of a composite type converter. Such a converter is responsible to provide a mechanism
 * where composite data types can be converted from an object into a string representation and vice versa.
 * 
 * Based on which mechanism the conversion is done is an implementation detail.
 * 
 * @author JEAF Development Team
 */
public interface CompositeTypeConverter {
  /**
   * Method serializes the passed composite data type object into a string representation.
   * 
   * @param pObject Object that should be converted. The parameter must not be null.
   * @param pSerializedClasses List of all classes that are involved in the serialization process of the passed object.
   * The list of involved classes must be complete and must not change its content or order between serialization and
   * deserialization.
   * @return {@link String} String representation of the passed object. The method must not return null. It is expected
   * that the returned string can be used as part of URLs.
   */
  String serializeObject( Object pObject, List<Class<?>> pSerializedClasses );

  /**
   * Method deserializes the passed string into an composite date type object of the passed type.
   * 
   * @param pSerializedObject String representation of the serialized object. The parameter must not be null.
   * @param pResultType Type of object that should be returned. The parameter must not be null.
   * @param pSerializedClasses List of all classes that were involved in the serialization process of the passed object.
   * The list of involved classes must be complete and must not change its content or order between serialization and
   * deserialization.
   * @return T Object representation of the passed string. The method never returns null.
   */
  <T> T deserializeObject( String pSerializedObject, Class<T> pResultType, List<Class<?>> pSerializedClasses );
}
