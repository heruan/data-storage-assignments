package org.example.patientrecords.adapters.graphql;

import org.example.patientrecords.data.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(PersonRepository personRepository) {
        return wiringBuilder -> wiringBuilder.type("Query", builder -> builder
                // We can use Spring Data and QueryDSL to do a lot of stuff automatically, provided that
                // our data models and the GraphQL models are the same.
                .dataFetcher("people", QuerydslDataFetcher.builder(personRepository).many()));
    }
}
