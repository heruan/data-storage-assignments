package org.example.patientrecords.adapters.grpc;

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
    public Server server(PersonalInformationGrpcService personalInformationGrpcService) {
        LOGGER.info("Listening for gRPC requests on port {}", PortNumbers.PATIENT_RECORDS_PORT);
        return NettyServerBuilder
                .forPort(PortNumbers.PATIENT_RECORDS_PORT)
                .addService(personalInformationGrpcService)
                .build();
    }

}
