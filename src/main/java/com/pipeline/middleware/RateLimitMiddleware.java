package com.pipeline.middleware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.pipeline.pipeline.HttpRequest;
import com.pipeline.pipeline.HttpResponse;
import com.pipeline.pipeline.RequestHandler;

public class RateLimitMiddleware implements RequestHandler {
  private Map<String, Integer> requestCounts;
  private int maxRequests;

  public RateLimitMiddleware(int maxRequests) {
    this.requestCounts = new ConcurrentHashMap<>();
    this.maxRequests = maxRequests;
  }

  @Override
  public HttpResponse handle(HttpRequest request, HttpResponse response, RequestHandler next) {
    String clientId = (String) request.getAttributes().get("user");
    if (clientId == null) {
      clientId = "anonymous"; // or return 401 if auth is required(not sure yet)
    }

    int count = requestCounts.getOrDefault(clientId, 0);
    if (count >= maxRequests) {
      response.setStatus(429);
      response.setBody(Map.of("error", "Rate limit exceeded"));
      response.markCompleted();
      return response;
    } else {
      requestCounts.put(clientId, count + 1);
      return next.handle(request, response, next);
    }
  }

}
