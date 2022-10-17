package org.example.portal;

import com.google.protobuf.StringValue;
import org.example.api.contact.EmailAddress;
import org.example.api.contact.PhoneNumber;
import org.example.api.contact.PostalAddress;
import org.example.api.patient.services.AddPersonCommand;
import org.example.api.patient.services.PersonQuery;
import org.example.api.person.data.Person;
import org.example.api.person.data.PersonalIdentityCode;
import org.example.api.person.data.Sex;
import org.example.api.time.Date;

public class PortalApp {
    public static void main(String[] args) {
        var patientRecordsClient = new PatientRecordsClient();
        var contactTracingClient = new ContactTracingClient();
        var messageSenderClient = new MessageSenderClient();

        var response = patientRecordsClient.getPersonalInformationService().addPerson(AddPersonCommand.newBuilder()
                .setPerson(Person.newBuilder()
                        .setSurname("Cool")
                        .addGivenNames("Joe")
                        .setDateOfBirth(Date.newBuilder().setYear(1981).setMonth(1).setDay(1).build())
                        .setSex(Sex.MALE)
                        .setPersonalIdentityCode(PersonalIdentityCode.newBuilder().setCode("010181-900C").build())
                        .setEmailAddress(EmailAddress.newBuilder().setEmailAddress("joecool@foo.bar").build())
                        .setPhoneNumber(PhoneNumber.newBuilder().setPhoneNumber("+358401234567").build())
                        .setPostalAddress(PostalAddress.newBuilder()
                                .setStreetAddress("Ruukinkatu")
                                .setNumber("2-4")
                                .setSpecifier(StringValue.of("The dark little cell in the basement"))
                                .setPostalCode("20540")
                                .setPostOffice("Turku")
                                .setCountry(StringValue.of("Finland"))
                                .build())
                        .build())
                .build());
        System.out.println(response);

        var queryResponse = patientRecordsClient.getPersonalInformationService().findPerson(PersonQuery.newBuilder().setPersonalIdentityCode(PersonalIdentityCode.newBuilder().setCode("010181-900C").build()).build());
        System.out.println(queryResponse);
    }
}