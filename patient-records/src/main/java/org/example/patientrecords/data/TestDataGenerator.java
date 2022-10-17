package org.example.patientrecords.data;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class TestDataGenerator {

    private final PersonRepository personRepository;

    public TestDataGenerator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostConstruct
    public void init() {
        var person = new Person();
        person.setSurname("Cool");
        person.setGivenNames("Joe");
        person.setDateOfBirth(LocalDate.of(1981, 1, 1));
        person.setSex(Sex.MALE);
        person.setPersonalIdentityCode("010181-900C");
        person.setEmailAddress("joecool@foo.bar");
        person.setPhoneNumber("+358401234567");

        var address = new PostalAddress();
        address.setStreet("Ruukinkatu");
        address.setNumber("2-4");
        address.setSpecifier("The dark little cell in the basement");
        address.setPostalCode("20540");
        address.setPostOffice("Turku");
        address.setCountry("Finland");

        person.setPostalAddress(address);

        personRepository.saveAndFlush(person);
    }
}
