package com.pipeline.middleware;

import java.util.HashMap;
import java.util.Map;
import com.pipeline.converter.ResponseConverter;
import com.pipeline.pipeline.HttpRequest;
import com.pipeline.pipeline.HttpResponse;
import com.pipeline.pipeline.RequestHandler;

public class FormatMiddleware implements RequestHandler {
  private final Map<String, ResponseConverter> converters;

  public FormatMiddleware(Map<String, ResponseConverter> converters) {
    this.converters = new HashMap<>(converters);
  }

  @Override
  public HttpResponse handle(HttpRequest request, HttpResponse response, RequestHandler next) {
    HttpResponse result = next.handle(request, response, next);

    String acceptHeader = request.getHeaders().get("Accept");
    if (acceptHeader == null || acceptHeader.isEmpty()) {
      acceptHeader = "application/json";
    }

    ResponseConverter converter = converters.get(acceptHeader);
    if (converter == null) {
      converter = converters.get("application/json");
    }

    if (result.getBody() != null && !(result.getBody() instanceof String)) {
      String formatted = converter.convert(result.getBody());
      result.setBody(formatted);
      result.addHeader("Content-Type", converter.getContentType());
    } else if (result.getBody() != null) {
      result.addHeader("Content-Type", converter.getContentType());
    }

    return result;
  }
}
