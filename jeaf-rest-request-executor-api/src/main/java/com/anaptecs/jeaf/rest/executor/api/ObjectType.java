/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.rest.executor.api;

/**
 * Class can be used to define the expected result type when deserializing JSON into objects.
 * 
 * @author JEAF Development Team
 */
public abstract class ObjectType {
  /**
   * Method can be used to define the expected return type when reading a single object.
   * 
   * @param pObjectType Class representing the expected type that should be returned. The parameter must not be null.
   * @return {@link ObjectType} Object representing the type of the created object. The method never returns null.
   */
  public static ObjectType createObjectType( Class<?> pObjectType ) {
    return new SingleObjectType(pObjectType);
  }

  /**
   * Method can be used to define the expected return type when reading a generic type from JSON.
   * 
   * Please be aware that this type is intended to be used for business classes working with generics. If you want to
   * deserialize collection classes then please use {@link #createObjectType(Class, Class)} instead.
   * 
   * @param pGenericType Class object representing the generic type that should be read. The parameter must not be null.
   * @param pParameterType Class object representing the parameter type for the generic class. The parameter must not be
   * null.
   * @return {@link ObjectType} Object representing the type of the created object. The method never returns null.
   */
  public static ObjectType createGenericsObjectType( Class<?> pGenericType, Class<?> pParameterType ) {
    return new GenericsObjectType(pGenericType, pParameterType);
  }

  /**
   * Method can be used to create an object type based on a type reference. This is a way to create very flexible
   * ObjectTypes. However creation of a type reference requires to create an anonymous class.
   * 
   * @param pTypeReference {@link TypeReference} representing the type of objects deserialized from JSON.
   * @return {@link ObjectType} Object representing the type of the created object. The method never returns null.
   */
  public static ObjectType createTypeReferenceObjectType( TypeReference<?> pTypeReference ) {
    return new TypeReferenceObjectType(pTypeReference);
  }

  /**
   * Constructor is private to block unexpected subclasses.
   */
  private ObjectType( ) {
  }

  /**
   * Class implements an object type that can be used to represent single object.
   */
  public static class SingleObjectType extends ObjectType {
    private final Class<?> objectType;

    private SingleObjectType( Class<?> pObjectType ) {
      super();
      objectType = pObjectType;
    }

    public Class<?> getObjectType( ) {
      return objectType;
    }
  }

  /**
   * Class implements an object type that can be used for generic types.
   */
  public static class GenericsObjectType extends ObjectType {
    private final Class<?> genericType;

    private final Class<?> parameterType;

    public GenericsObjectType( Class<?> pGenericType, Class<?> pParameterType ) {
      genericType = pGenericType;
      parameterType = pParameterType;
    }

    public Class<?> getGenericType( ) {
      return genericType;
    }

    public Class<?> getParameterType( ) {
      return parameterType;
    }
  }

  /**
   * Class implements an object type that can be used for any kind of type including nested generics.
   */
  public static class TypeReferenceObjectType extends ObjectType {
    private final TypeReference<?> typeReference;

    public TypeReferenceObjectType( TypeReference<?> pTypeReference ) {
      typeReference = pTypeReference;
    }

    public TypeReference<?> getTypeReference( ) {
      return typeReference;
    }
  }
}
