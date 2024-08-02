package com.anaptecs.jeaf.rest.executor.api.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.anaptecs.jeaf.rest.executor.api.ObjectType;
import com.anaptecs.jeaf.rest.executor.api.ObjectType.TypeReferenceObjectType;
import com.anaptecs.jeaf.rest.executor.api.TypeReference;

public class ObjectTypeTest {

  @Test
  void testTypeReferenceObjectType( ) {
    TypeReference<Response<Pageable<BusinessObject>>> lTypeReference =
        new TypeReference<Response<Pageable<BusinessObject>>>() {
        };
    assertEquals(
        "com.anaptecs.jeaf.rest.executor.api.test.Response<com.anaptecs.jeaf.rest.executor.api.test.Pageable<com.anaptecs.jeaf.rest.executor.api.test.BusinessObject>>",
        lTypeReference.getType().getTypeName());

    TypeReferenceObjectType lTypeReferenceObjectType =
        (TypeReferenceObjectType) ObjectType.createTypeReferenceObjectType(lTypeReference);
    assertEquals(
        "com.anaptecs.jeaf.rest.executor.api.test.Response<com.anaptecs.jeaf.rest.executor.api.test.Pageable<com.anaptecs.jeaf.rest.executor.api.test.BusinessObject>>",
        lTypeReferenceObjectType.getTypeReference().toString());

    TypeReference<List<Response<Pageable<BusinessObject>>>> lType2 =
        new TypeReference<List<Response<Pageable<BusinessObject>>>>() {
        };
    assertEquals(
        "java.util.List<com.anaptecs.jeaf.rest.executor.api.test.Response<com.anaptecs.jeaf.rest.executor.api.test.Pageable<com.anaptecs.jeaf.rest.executor.api.test.BusinessObject>>>",
        lType2.getType().getTypeName());
    lTypeReferenceObjectType = (TypeReferenceObjectType) ObjectType.createTypeReferenceObjectType(lType2);
    assertEquals(
        "java.util.List<com.anaptecs.jeaf.rest.executor.api.test.Response<com.anaptecs.jeaf.rest.executor.api.test.Pageable<com.anaptecs.jeaf.rest.executor.api.test.BusinessObject>>>",
        lTypeReferenceObjectType.getTypeReference().getType().getTypeName());
  }

}

class Response<T> {
  T data;
}

class Pageable<T> {
  int size;

  List<T> objects;
}

class BusinessObject {
}
