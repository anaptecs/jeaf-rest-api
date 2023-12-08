package com.anaptecs.jeaf.rest.executor.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class can be used to get information about generics at runtime.
 * 
 * For background info please see link bellow.
 * 
 * @see <a href=
 * "http://gafter.blogspot.com/2006/12/super-type-tokens.html">http://gafter.blogspot.com/2006/12/super-type-tokens.html</a>
 * 
 * @author JEAF Development Team
 */
public abstract class TypeReference<T> {
  private final Type type;

  protected TypeReference( ) {
    // Ensure that object was only created with actual types and never without.
    Type superClass = getClass().getGenericSuperclass();
    if (superClass instanceof Class<?>) {
      throw new IllegalArgumentException("TypeReference must not be created without actual type information");
    }
    else {
      type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
  }

  public Type getType( ) {
    return type;
  }

  @Override
  public String toString( ) {
    return type.toString();
  }
}