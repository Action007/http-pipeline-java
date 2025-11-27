package com.pipeline.chain;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.pipeline.pipeline.HttpRequest;
import com.pipeline.pipeline.HttpResponse;

public class AsyncRequestProcessor {
  private final ExecutorService executor;
  private final HandlerChain chain;

  public AsyncRequestProcessor(HandlerChain chain) {
    this.chain = chain;
    this.executor = Executors.newFixedThreadPool(4);
  }

  public List<HttpResponse> processAsync(List<HttpRequest> requests) {
    List<CompletableFuture<HttpResponse>> futures = requests.stream()
        .map(request -> CompletableFuture.supplyAsync(() -> chain.execute(request), executor))
        .toList();

    CompletableFuture<Void> allDone =
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    allDone.join();

    return futures.stream().map(CompletableFuture::join).toList();
  }

  public void shutdown() {
    executor.shutdown();
  }
}
