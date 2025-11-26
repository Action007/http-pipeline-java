package com.pipeline.middleware;

import com.pipeline.pipeline.HttpRequest;
import com.pipeline.pipeline.HttpResponse;
import com.pipeline.pipeline.RequestHandler;

public class LoggingMiddleware implements RequestHandler {

  @Override
  public HttpResponse handle(HttpRequest request, HttpResponse response, RequestHandler next) {
    System.out.println("→ Request: " + request.getMethod() + " " + request.getPath());

    long startTime = System.currentTimeMillis();
    HttpResponse result = next.handle(request, response, next);
    long endTime = System.currentTimeMillis();

    System.out
        .println("← Response: " + result.getStatusCode() + " (" + (endTime - startTime) + "ms)");
    return result;
  }
}
