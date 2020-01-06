package com.aanrii.webflux.demo;

import com.aanrii.webflux.demo.proto.OrderServiceGrpc;
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.linecorp.armeria.spring.GrpcServiceRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArmeniaConfiguration {
  @Bean
  public ArmeriaServerConfigurator armeriaServerConfigurator() {
    int windowSize = 10; // 1個のMessageのサイズより小さい任意の値
    return builder -> builder.http2InitialStreamWindowSize(windowSize);
  }

  @Bean
  public GrpcServiceRegistrationBean grpcServiceRegistrationBean(
      OrderServiceGrpc.OrderServiceImplBase orderService) {
    return new GrpcServiceRegistrationBean()
        .setServiceName(OrderServiceGrpc.SERVICE_NAME)
        .setService(
            GrpcService.builder()
                .addService(orderService)
                .supportedSerializationFormats(GrpcSerializationFormats.values())
                .enableUnframedRequests(true)
                .build())
        .setDecorators(LoggingService.newDecorator());
  }
}
