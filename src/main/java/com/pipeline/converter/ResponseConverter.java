package com.pipeline.converter;

public interface ResponseConverter {
  String convert(Object body);

  String getContentType();
}
