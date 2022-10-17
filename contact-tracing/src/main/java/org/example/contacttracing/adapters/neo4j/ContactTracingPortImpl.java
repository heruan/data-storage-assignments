package org.example.contacttracing.adapters.neo4j;

import org.example.contacttracing.ports.ContactTracingPort;
import org.neo4j.driver.Driver;

import java.util.List;
import java.util.stream.Collectors;

import static org.neo4j.driver.Values.parameters;

class ContactTracingPortImpl implements ContactTracingPort {

    private final Driver driver;

    public ContactTracingPortImpl(Driver driver) {
        this.driver = driver;
    }

    // Here are some example methods illustrating how to interact with Neo4j on the low level.
    // The query language is called Cypher. You can find the manual here: https://neo4j.com/docs/cypher-manual/current/

    @Override
    public Long addActor(String actor) {
        try (var session = driver.session()) {
            return session.writeTransaction(tx -> {
                var result = tx.run("CREATE (a:Actor {name:$name}) RETURN ID(a)", parameters("name", actor));
                return result.single().get(0).asLong();
            });
        }
    }

    @Override
    public Long addMovie(String movie) {
        try (var session = driver.session()) {
            return session.writeTransaction(tx -> {
                var result = tx.run("CREATE (m:Movie {name:$name}) RETURN ID(m)", parameters("name", movie));
                return result.single().get(0).asLong();
            });
        }
    }

    @Override
    public Long addRole(Long actorId, Long movieId, String role) {
        try (var session = driver.session()) {
            return session.writeTransaction(tx -> {
                var result = tx.run("MATCH (a:Actor),(m:Movie) WHERE ID(a) = $actorId AND ID(m) = $movieId CREATE (a)-[r:ACTED_IN {role: $role}]->(m) RETURN ID(r)",
                        parameters("actorId", actorId, "movieId", movieId, "role", role));
                return result.single().get(0).asLong();
            });
        }
    }

    @Override
    public List<String> findActorsWhoHaveWorkedWith(Long actorId) {
        try (var session = driver.session()) {
            return session.readTransaction(tx -> {
                var result = tx.run("MATCH (a:Actor)-[:ACTED_IN]->(movie)<-[:ACTED_IN]-(b:Actor) WHERE ID(a) = $actorId RETURN b.name",
                        parameters("actorId", actorId));
                return result.stream().map(r -> r.get(0).asString()).collect(Collectors.toList());
            });
        }
    }
}
