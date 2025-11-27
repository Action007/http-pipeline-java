package com.pipeline.middleware;

import com.pipeline.pipeline.HttpRequest;
import com.pipeline.pipeline.HttpResponse;
import com.pipeline.pipeline.RequestHandler;

public class TimeoutMiddleware implements RequestHandler {
  private final long timeoutMillis;

  public TimeoutMiddleware(long timeoutMillis) {
    this.timeoutMillis = timeoutMillis;
  }

  @Override
  public HttpResponse handle(HttpRequest request, HttpResponse response, RequestHandler next) {
    long start = System.currentTimeMillis();

    // Execute the rest of the chain
    HttpResponse result = next.handle(request, response, next);

    long duration = System.currentTimeMillis() - start;

    // Log if over threshold (don't alter response)
    if (duration > timeoutMillis) {
      System.out.println("⚠️  SLOW REQUEST: " + request.getMethod() + " " + request.getPath()
          + " took " + duration + "ms (limit: " + timeoutMillis + "ms)");
    }

    return result;
  }
}
