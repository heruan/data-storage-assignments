package org.example.patientrecords.data;

import javax.annotation.PostConstruct;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

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

        var other = new Person();
        other.setSurname("Cool");
        other.setGivenNames("Joella");
        other.setDateOfBirth(LocalDate.of(1991, 11, 11));
        other.setSex(Sex.OTHER);
        other.setPersonalIdentityCode("11191-800B");
        other.setEmailAddress("joella@foo.bar");
        other.setPhoneNumber("+358407654321");

        var otherAddress = new PostalAddress();
        otherAddress.setStreet("Ruukinkatu");
        otherAddress.setNumber("2-4");
        otherAddress.setSpecifier("The dark little cell in the basement");
        otherAddress.setPostalCode("20540");
        otherAddress.setPostOffice("Turku");
        otherAddress.setCountry("Finland");

        other.setPostalAddress(otherAddress);

        personRepository.saveAndFlush(other);
    }
}
