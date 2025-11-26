package com.pipeline.chain;

import java.util.List;
import com.pipeline.pipeline.HttpRequest;
import com.pipeline.pipeline.HttpResponse;
import com.pipeline.pipeline.RequestHandler;

public class HandlerChain {
  private List<RequestHandler> handlers;

  public HandlerChain(List<RequestHandler> handlers) {
    this.handlers = handlers;
  }

  public HttpResponse execute(HttpRequest request) {
    HttpResponse httpResponse = new HttpResponse();
    return proceed(0, request, httpResponse);
  }

  private HttpResponse proceed(int index, HttpRequest request, HttpResponse response) {
    if (index >= handlers.size()) {
      return response;
    }

    RequestHandler handler = handlers.get(index);
    RequestHandler next = (req, res, n) -> proceed(index + 1, req, res);
    return handler.handle(request, response, next);
  }
}
