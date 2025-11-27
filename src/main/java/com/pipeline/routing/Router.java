package com.pipeline.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.pipeline.pipeline.HttpMethod;
import com.pipeline.pipeline.HttpRequest;
import com.pipeline.pipeline.RequestHandler;

public class Router {
  private final List<Route> routes = new ArrayList<>();

  public void addRoute(String method, String path, RequestHandler handler) {
    HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());
    routes.add(new Route(httpMethod, path, handler));
  }

  public Optional<Route> findRoute(HttpRequest request) {
    return routes.stream().filter(route -> route.getMethod() == request.getMethod()
        && route.getPathPattern().equals(request.getPath())).findFirst();
  }
}
