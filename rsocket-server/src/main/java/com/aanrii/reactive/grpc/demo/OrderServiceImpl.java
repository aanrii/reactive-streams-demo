package com.aanrii.reactive.grpc.demo;

import com.aanrii.webflux.demo.proto.OrderProto.Order;
import com.aanrii.webflux.demo.proto.OrderProto.OrderResult;
import com.aanrii.webflux.demo.proto.OrderService;
import io.netty.buffer.ByteBuf;
import java.time.Clock;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {
  private final Clock clock = Clock.systemDefaultZone();

  @SneakyThrows
  private OrderResult process(Order order) {
    TimeUnit.SECONDS.sleep(1L);
    return OrderResult.newBuilder()
        .setOrderId(order.getId())
        .setStatus("ok")
        .setReceivedAt(clock.millis())
        .build();
  }

  @Override
  public Flux<OrderResult> placeOrder(Publisher<Order> messages, ByteBuf metadata) {
    return Flux.from(messages).limitRate(3).onBackpressureError().map(this::process).log("placeOrderStreamServer");
  }
}
