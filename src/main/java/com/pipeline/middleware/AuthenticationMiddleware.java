package com.pipeline.middleware;

import java.util.Map;
import com.pipeline.pipeline.HttpRequest;
import com.pipeline.pipeline.HttpResponse;
import com.pipeline.pipeline.RequestHandler;

public class AuthenticationMiddleware implements RequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request, HttpResponse response, RequestHandler next) {
    String authHeader = request.getHeaders().get("Authorization");
    if (authHeader != null && authHeader.equals("valid-token")) {
      request.setAttribute("user", "john_doe");
      return next.handle(request, response, next);
    } else {
      response.setStatus(401);
      response.setBody(Map.of("error", "Unauthorized"));
      response.markCompleted();
      return response;
    }
  }
}
