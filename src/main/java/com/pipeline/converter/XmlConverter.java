package com.pipeline.converter;

import java.util.List;
import java.util.Map;

public class XmlConverter implements ResponseConverter {

  @Override
  public String convert(Object body) {
    if (body == null) {
      return "<response>null</response>";
    }
    if (body instanceof String) {
      return "<response>" + body + "</response>";
    }
    if (body instanceof Map) {
      return "<response>" + convertMap((Map<?, ?>) body) + "</response>";
    }
    if (body instanceof List) {
      return "<response>" + convertList((List<?>) body) + "</response>";
    }
    return "<response>" + body.toString() + "</response>";
  }

  private String convertMap(Map<?, ?> map) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      String key = entry.getKey().toString();
      String value = entry.getValue() == null ? "null" : entry.getValue().toString();
      sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
    }
    return sb.toString();
  }

  private String convertList(List<?> list) {
    StringBuilder sb = new StringBuilder();
    for (Object item : list) {
      if (item instanceof Map) {
        sb.append("<item>").append(convertMap((Map<?, ?>) item)).append("</item>");
      } else {
        String value = item == null ? "null" : item.toString();
        sb.append("<item>").append(value).append("</item>");
      }
    }
    return sb.toString();
  }

  @Override
  public String getContentType() {
    return "application/xml";
  }
}
