package com.pipeline.pipeline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
  private final HttpMethod method;
  private final String path;
  private final Map<String, String> headers;
  private final Map<String, String> queryParams;
  private final String body;
  private final Map<String, Object> attributes;

  public HttpRequest(HttpMethod method, String path, Map<String, String> headers,
      Map<String, String> queryParams, String body) {
    this.method = method;
    this.path = path;
    this.headers = headers != null ? new HashMap<>(headers) : new HashMap<>();
    this.queryParams = queryParams != null ? new HashMap<>(queryParams) : new HashMap<>();
    this.body = body;
    this.attributes = new HashMap<>();
  }

  public HttpRequest(HttpMethod method, String path) {
    this(method, path, null, null, null);
  }

  public HttpMethod getMethod() {
    return this.method;
  }

  public String getPath() {
    return this.path;
  }

  public Map<String, String> getHeaders() {
    return Collections.unmodifiableMap(this.headers);
  }

  public Map<String, String> getQueryParams() {
    return Collections.unmodifiableMap(this.queryParams);
  }

  public String getBody() {
    return this.body;
  }

  public Map<String, Object> getAttributes() {
    return new HashMap<>(this.attributes);
  }

  public void setAttribute(String key, Object value) {
    this.attributes.put(key, value);
  }

  @Override
  public String toString() {
    return "{" + " method='" + getMethod() + "'" + ", path='" + getPath() + "'" + ", headers='"
        + getHeaders() + "'" + ", queryParams='" + getQueryParams() + "'" + ", body='" + getBody()
        + "'" + ", attributes='" + getAttributes() + "'" + "}";
  }
}
