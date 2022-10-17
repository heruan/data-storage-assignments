package org.example.contacttracing.adapters.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.example.api.PortNumbers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcServerConfig.class);

    @Bean(destroyMethod = "shutdown", initMethod = "start")
    public Server server(ContactTracingGrpcService contactTracingGrpcService) {
        LOGGER.info("Listening for gRPC requests on port {}", PortNumbers.CONTACT_TRACING_PORT);
        return NettyServerBuilder
                .forPort(PortNumbers.CONTACT_TRACING_PORT)
                .addService(contactTracingGrpcService)
                .build();
    }
}
