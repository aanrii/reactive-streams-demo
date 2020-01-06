package com.aanrii.webflux.demo;

import com.aanrii.webflux.demo.proto.OrderProto.Order;
import com.aanrii.webflux.demo.proto.OrderProto.OrderResult;
import com.aanrii.webflux.demo.proto.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.time.Clock;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {
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
  public StreamObserver<Order> placeOrder(StreamObserver<OrderResult> responseObserver) {
    return new StreamObserver<>() {
      @Override
      public void onNext(Order value) {
        OrderResult orderResult = process(value);
        responseObserver.onNext(orderResult);
      }

      @Override
      public void onError(Throwable t) {
        t.printStackTrace();
      }

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
      }
    };
  }
}
