package com.pipeline.middleware;

import java.util.Map;
import java.util.Optional;
import com.pipeline.pipeline.HttpRequest;
import com.pipeline.pipeline.HttpResponse;
import com.pipeline.pipeline.RequestHandler;
import com.pipeline.routing.Route;
import com.pipeline.routing.Router;

public class RoutingMiddleware implements RequestHandler {
  private Router router;

  public RoutingMiddleware(Router router) {
    this.router = router;
  }

  @Override
  public HttpResponse handle(HttpRequest request, HttpResponse response, RequestHandler next) {
    Optional<Route> maybeRoute = router.findRoute(request);
    if (maybeRoute.isEmpty()) {
      response.setStatus(404);
      response.setBody(Map.of("error", "Not Found"));
      return response;
    }

    RequestHandler endpointHandler = maybeRoute.get().getHandler();
    return endpointHandler.handle(request, response, next);
  }
}
