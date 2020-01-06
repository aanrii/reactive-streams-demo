package com.aanrii.reactive.grpc.demo;

import com.aanrii.webflux.demo.proto.OrderService;
import com.aanrii.webflux.demo.proto.OrderServiceServer;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.rpc.rsocket.RequestHandlingRSocket;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import java.net.InetSocketAddress;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RSocketConfiguration {
  @Bean
  public CloseableChannel closeableChannel(OrderService orderService) {
    OrderServiceServer orderServiceServer =
        new OrderServiceServer(orderService, Optional.empty(), Optional.empty());

    return RSocketFactory.receive()
        .acceptor(
            (setup, sendingSocket) -> Mono.just(new RequestHandlingRSocket(orderServiceServer)))
        .transport(TcpServerTransport.create(new InetSocketAddress("127.0.0.1", 7000)))
        .start()
        .block();
  }
}
