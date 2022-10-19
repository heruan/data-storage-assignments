package org.example.patientrecords.adapters.graphql;

import java.util.List;

import org.example.patientrecords.data.Person;
import org.example.patientrecords.ports.PersonManagementPort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PersonController {

    private final PersonManagementPort personManagementPort;

    public PersonController(PersonManagementPort personManagementPort) {
        this.personManagementPort = personManagementPort;
    }

    // Here, we have more control over the queries at the cost of having to
    // write more code.

    @QueryMapping("byPersonalIdentityCode")
    public Person personByPersonalIdentityCode(@Argument("pic") String pic) {
        return personManagementPort.findByPersonalIdentityCode(pic)
                .orElse(null);
    }

    @QueryMapping("byName")
    public List<Person> personByName(@Argument("name") String name) {
        return personManagementPort.findByName(name);
    }

    // Using lastname instead of surname intentionally, to show how you can
    // translate from the data model into the GraphQL model.
    @SchemaMapping(typeName = "Person", field = "lastname")
    public String lastname(Person person) {
        return person.getSurname();
    }

    @SchemaMapping(typeName = "Person", field = "pic")
    public String pic(Person person) {
        return person.getPersonalIdentityCode();
    }

    @MutationMapping("updatePhoneNumber")
    public Person updatePhoneNumber(@Argument("pic") String pic,
            @Argument("phoneNumber") String phoneNumber) {
        return personManagementPort.findByPersonalIdentityCode(pic)
                .map(person -> {
                    person.setPhoneNumber(phoneNumber);
                    return personManagementPort.updatePerson(person);
                }).orElse(null);
    }
}
