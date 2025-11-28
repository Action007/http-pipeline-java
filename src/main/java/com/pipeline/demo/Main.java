package com.pipeline.demo;

import java.util.*;
import com.pipeline.chain.AsyncRequestProcessor;
import com.pipeline.chain.HandlerChain;
import com.pipeline.converter.JsonConverter;
import com.pipeline.converter.ResponseConverter;
import com.pipeline.converter.XmlConverter;
import com.pipeline.middleware.*;
import com.pipeline.pipeline.*;
import com.pipeline.routing.Router;

public class Main {
  public static void main(String[] args) {
    // === STEP 1: Endpoint Handlers ===
    RequestHandler getUsersHandler = (req, res, next) -> {
      String user = (String) req.getAttributes().get("user");
      if (user == null) {
        res.setStatus(401);
        res.setBody(Map.of("error", "Authentication required"));
        return res;
      }
      res.setStatus(200);
      res.setBody(List.of(Map.of("id", 1, "name", "Alice"), Map.of("id", 2, "name", "Bob")));
      return res;
    };

    RequestHandler postUsersHandler = (req, res, next) -> {
      res.setStatus(201);
      res.setBody(Map.of("id", 3, "name", "Charlie", "created", true));
      return res;
    };

    RequestHandler getProductsHandler = (req, res, next) -> {
      res.setStatus(200);
      res.setBody(List.of(Map.of("id", 101, "name", "Laptop", "price", 999),
          Map.of("id", 102, "name", "Mouse", "price", 25)));
      return res;
    };

    // === STEP 2: Router ===
    Router router = new Router();
    router.addRoute("GET", "/users", getUsersHandler);
    router.addRoute("POST", "/users", postUsersHandler);
    router.addRoute("GET", "/products", getProductsHandler);

    // === STEP 3: Converters ===
    Map<String, ResponseConverter> converters = new HashMap<>();
    converters.put("application/json", new JsonConverter());
    converters.put("application/xml", new XmlConverter());

    // === STEP 4: Build Chain (ORDER MATTERS) ===
    HandlerChain chain = new HandlerChain(List.of(new LoggingMiddleware(),
        new TimeoutMiddleware(500), new AuthenticationMiddleware(), new RateLimitMiddleware(3),
        new FormatMiddleware(converters), new RoutingMiddleware(router)));

    // === STEP 5: Test Requests ===
    List<HttpRequest> testRequests = Arrays.asList(
        // 1. Valid authenticated GET /users with JSON Accept
        new HttpRequest(HttpMethod.GET, "/users",
            Map.of("Authorization", "valid-token", "Accept", "application/json"), Map.of(), null),
        // 2. Unauthenticated GET /users (should fail with 401)
        new HttpRequest(HttpMethod.GET, "/users", Map.of("Accept", "application/json"), Map.of(),
            null),
        // 3. Valid POST /users with body
        new HttpRequest(HttpMethod.POST, "/users",
            Map.of("Authorization", "valid-token", "Accept", "application/json"), Map.of(),
            "{\"name\":\"Charlie\"}"),
        // 4. Valid GET /products with XML Accept
        new HttpRequest(HttpMethod.GET, "/products",
            Map.of("Authorization", "valid-token", "Accept", "application/xml"), Map.of(), null),
        // 5. Valid GET /unknown-path (should fail with 404)
        new HttpRequest(HttpMethod.GET, "/unknown-path",
            Map.of("Authorization", "valid-token", "Accept", "application/json"), Map.of(), null));

    // === STEP 6: Execute and Print ===
    System.out.println("=== SYNCHRONOUS TESTS ===\n");
    for (int i = 0; i < testRequests.size(); i++) {
      HttpRequest req = testRequests.get(i);
      System.out.println("REQUEST " + (i + 1) + ": " + req.getMethod() + " " + req.getPath());
      System.out.println("Headers: " + req.getHeaders());
      System.out.println("-----");

      HttpResponse res = chain.execute(req);
      System.out.println("STATUS: " + res.getStatusCode());
      System.out.println("HEADERS: " + res.getHeaders());
      System.out.println("BODY: " + res.getBody());
      System.out.println("==========================================\n");
    }

    // === STEP 7: Async Test (10 rapid requests) ===
    System.out.println("=== ASYNC RATE LIMIT TEST ===\n");
    List<HttpRequest> rapidRequests = new ArrayList<>();
    for (int i = 0; i < 5; i++) { // 5 requests (limit is 3)
      rapidRequests.add(new HttpRequest(HttpMethod.GET, "/users",
          Map.of("Authorization", "valid-token", "Accept", "application/json"), Map.of(), null));
    }

    AsyncRequestProcessor asyncProc = new AsyncRequestProcessor(chain);
    List<HttpResponse> asyncResponses = asyncProc.processAsync(rapidRequests);
    asyncProc.shutdown();

    for (int i = 0; i < asyncResponses.size(); i++) {
      HttpResponse res = asyncResponses.get(i);
      System.out.println("Async Response " + (i + 1) + ": " + res.getStatusCode());
    }
  }
}
