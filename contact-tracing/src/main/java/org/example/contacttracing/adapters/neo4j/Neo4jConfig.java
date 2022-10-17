package org.example.contacttracing.adapters.neo4j;

import org.example.contacttracing.ports.ContactTracingPort;
import org.neo4j.configuration.connectors.BoltConnector;
import org.neo4j.configuration.helpers.SocketAddress;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.nio.file.Path;

@Configuration
public class Neo4jConfig {

    @Bean(destroyMethod = "shutdown")
    public DatabaseManagementService databaseManagementService(
            @Value("${neo4j.embedded.directory}") Path databaseDirectory,
            @Value("${neo4j.embedded.port:7687}") int port) {
        var databaseService = new DatabaseManagementServiceBuilder(databaseDirectory)
                .setConfig(BoltConnector.enabled, true)
                .setConfig(BoltConnector.listen_address, new SocketAddress(port))
                .build();

        return databaseService;
    }

    @Bean
    @DependsOn("databaseManagementService")
    public ContactTracingPort contactTracingPort(Driver driver) {
        return new ContactTracingPortImpl(driver);
    }
}
