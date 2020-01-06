package com.aanrii.reactive.grpc.demo;

import com.aanrii.webflux.demo.proto.OrderProto.Order;
import com.aanrii.webflux.demo.proto.OrderServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class OrderExecutor implements CommandLineRunner {

  private final OrderServiceClient orderServiceClient;

  @Override
  public void run(String... args) throws Exception {
    Flux<Order> orderFlux = Flux.range(0, 20).map(i -> String.format("order-%d", i))
        .map(id -> Order.newBuilder().setId(id).build()).log();
    orderServiceClient.placeOrder(orderFlux).log().subscribe();

    Thread.currentThread().join();
  }
}
