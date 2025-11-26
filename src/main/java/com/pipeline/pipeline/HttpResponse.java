package com.pipeline.pipeline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
  private int statusCode;
  private final Map<String, String> headers;
  private Object body;
  private boolean completed;

  public HttpResponse(int statusCode, Map<String, String> headers, Object body, boolean completed) {
    this.statusCode = statusCode;
    this.headers = headers != null ? new HashMap<>(headers) : new HashMap<>();
    this.body = body;
    this.completed = completed;
  }

  public HttpResponse() {
    this.statusCode = 200;
    this.headers = new HashMap<>();
    this.body = null;
    this.completed = false;
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  public void markCompleted() {
    completed = true;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public void setStatus(int code) {
    this.statusCode = code;
  }

  public Map<String, String> getHeaders() {
    return Collections.unmodifiableMap(this.headers);
  }

  public Object getBody() {
    return this.body;
  }

  public void setBody(Object body) {
    this.body = body;
  }

  public boolean isCompleted() {
    return completed;
  }

  @Override
  public String toString() {
    return "{" + " statusCode='" + getStatusCode() + "'" + ", headers='" + getHeaders() + "'"
        + ", body='" + getBody() + "'" + "'" + "}";
  }

}
