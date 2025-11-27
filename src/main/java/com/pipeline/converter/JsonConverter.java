package com.pipeline.converter;

import java.util.List;
import java.util.Map;

public class JsonConverter implements ResponseConverter {
  @Override
  public String convert(Object body) {
    if (body == null)
      return "null";
    if (body instanceof String)
      return (String) body;
    if (body instanceof Map)
      return convertMap((Map<?, ?>) body);
    if (body instanceof List)
      return convertList((List<?>) body);
    return "\"" + body.toString() + "\"";
  }

  private String convertMap(Map<?, ?> map) {
    StringBuilder sb = new StringBuilder("{");
    boolean first = true;
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      if (!first)
        sb.append(",");
      sb.append("\"").append(entry.getKey()).append("\":");
      sb.append(toJsonValue(entry.getValue()));
      first = false;
    }
    sb.append("}");
    return sb.toString();
  }

  private String convertList(List<?> list) {
    StringBuilder sb = new StringBuilder("[");
    boolean first = true;
    for (Object item : list) {
      if (!first)
        sb.append(",");
      sb.append(toJsonValue(item));
      first = false;
    }
    sb.append("]");
    return sb.toString();
  }

  private String toJsonValue(Object value) {
    if (value == null)
      return "null";
    if (value instanceof String)
      return "\"" + value + "\"";
    if (value instanceof Number || value instanceof Boolean)
      return value.toString();
    if (value instanceof Map)
      return convertMap((Map<?, ?>) value);
    if (value instanceof List)
      return convertList((List<?>) value);
    return "\"" + value.toString() + "\"";
  }

  @Override
  public String getContentType() {
    return "application/json";
  }
}
