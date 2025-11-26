package com.pipeline.pipeline;

public interface RequestHandler {
  HttpResponse handle(HttpRequest request, HttpResponse response, RequestHandler next);
}
