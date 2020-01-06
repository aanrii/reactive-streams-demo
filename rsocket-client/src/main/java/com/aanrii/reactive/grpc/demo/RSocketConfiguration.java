package com.aanrii.reactive.grpc.demo;

import com.aanrii.webflux.demo.proto.OrderServiceClient;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import java.net.InetSocketAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RSocketConfiguration {

  @Bean
  public OrderServiceClient orderServiceClient() {
    RSocket rSocket = RSocketFactory
        .connect()
        .transport(TcpClientTransport.create(new InetSocketAddress("127.0.0.1", 7000)))
        .start()
        .log("server")
        .block();
    return new OrderServiceClient(rSocket);
  }
}
