package org.example.contacttracing.ports;

import java.util.List;

public interface ContactTracingPort {

    // These are just example methods to get you started. You can delete them after you have created the real ones.

    Long addActor(String name); // Returns the actor ID

    Long addMovie(String movie); // Returns the movie ID

    Long addRole(Long actorId, Long movieId, String role); // Returns the role ID

    List<String> findActorsWhoHaveWorkedWith(Long actorId); // Returns the actor names
}
