package com.pipeline.routing;

import com.pipeline.pipeline.HttpMethod;
import com.pipeline.pipeline.RequestHandler;

public class Route {
  private final HttpMethod method;
  private final String pathPattern;
  private final RequestHandler handler;

  public Route(HttpMethod method, String pathPattern, RequestHandler handler) {
    this.method = method;
    this.pathPattern = pathPattern;
    this.handler = handler;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getPathPattern() {
    return pathPattern;
  }

  public RequestHandler getHandler() {
    return handler;
  }

  @Override
  public String toString() {
    return "{" + " method='" + getMethod() + "'" + ", pathPattern='" + getPathPattern() + "'"
        + ", handler='" + getHandler() + "'" + "}";
  }
}
