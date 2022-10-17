package org.example.patientrecords.adapters.graphql;

import org.example.patientrecords.data.Person;
import org.example.patientrecords.ports.PersonManagementPort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PersonController {

    private final PersonManagementPort personManagementPort;

    public PersonController(PersonManagementPort personManagementPort) {
        this.personManagementPort = personManagementPort;
    }

    // Here, we have more control over the queries at the cost of having to write more code.

    @QueryMapping("byPersonalIdentityCode")
    public Person personByPersonalIdentityCode(@Argument("pic") String pic) {
        return personManagementPort.findByPersonalIdentityCode(pic).orElse(null);
    }

    // We don't need a mapping for ID because the name in the data model is the same as in the GraphQL model.
  /*  @SchemaMapping(typeName = "Person", field = "id")
    public Long id(Person person) {
        return person.getId();
    }*/

    @SchemaMapping(typeName = "Person", field = "lastname")
    public String lastname(Person person) { // Using lastname instead of surname intentionally, to show how you can translate from the data model into the GraphQL model.
        return person.getSurname();
    }
}
