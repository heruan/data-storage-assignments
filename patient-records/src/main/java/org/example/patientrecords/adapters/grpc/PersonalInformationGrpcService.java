package org.example.patientrecords.adapters.grpc;

import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;
import org.example.api.contact.EmailAddress;
import org.example.api.contact.PhoneNumber;
import org.example.api.patient.services.*;
import org.example.api.person.data.PersonalIdentityCode;
import org.example.api.person.data.Status;
import org.example.api.time.TimeUtils;
import org.example.patientrecords.data.Person;
import org.example.patientrecords.data.PostalAddress;
import org.example.patientrecords.data.Sex;
import org.example.patientrecords.ports.PersonManagementPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonalInformationGrpcService extends PersonalInformationServiceGrpc.PersonalInformationServiceImplBase {

    private final PersonManagementPort personManagementPort;

    public PersonalInformationGrpcService(PersonManagementPort personManagementPort) {
        this.personManagementPort = personManagementPort;
    }

    @Override
    public void addPerson(AddPersonCommand request, StreamObserver<AddPersonCommandResponse> responseObserver) {
        try {
            var addedPerson = personManagementPort.addPerson(createPerson(request.getPerson()));
            responseObserver.onNext(AddPersonCommandResponse.newBuilder()
                    .setTimestamp(TimeUtils.wrapInstant(Instant.now()))
                    .setSuccess(createPersonEntityDto(addedPerson))
                    .build());
        } catch (Exception ex) {
            responseObserver.onNext(AddPersonCommandResponse.newBuilder()
                    .setTimestamp(TimeUtils.wrapInstant(Instant.now()))
                    .setFailure(ex.getMessage())
                    .build());
        }
        responseObserver.onCompleted();
    }

    private Person createPerson(org.example.api.person.data.Person dto) {
        var entity = new Person();
        entity.setSurname(dto.getSurname());
        entity.setGivenNames(String.join(" ", dto.getGivenNamesList()));
        if (dto.hasDateOfBirth()) {
            entity.setDateOfBirth(TimeUtils.unwrapDate(dto.getDateOfBirth()));
        }
        if (dto.hasDateOfDeath()) {
            entity.setDateOfDeath(TimeUtils.unwrapDate(dto.getDateOfDeath()));
        }
        if (dto.getSex() != org.example.api.person.data.Sex.UNRECOGNIZED) {
            entity.setSex(Sex.valueOf(dto.getSex().name()));
        }
        entity.setPersonalIdentityCode(dto.getPersonalIdentityCode().getCode());
        if (dto.hasEmailAddress()) {
            entity.setEmailAddress(dto.getEmailAddress().getEmailAddress());
        }
        if (dto.hasPhoneNumber()) {
            entity.setPhoneNumber(dto.getPhoneNumber().getPhoneNumber());
        }
        if (dto.hasPostalAddress()) {
            entity.setPostalAddress(createPostalAddress(dto.getPostalAddress()));
        }
        return entity;
    }

    private PostalAddress createPostalAddress(org.example.api.contact.PostalAddress dto) {
        var address = new PostalAddress();
        address.setStreet(dto.getStreetAddress());
        address.setNumber(dto.getNumber());
        if (dto.hasSpecifier()) {
            address.setSpecifier(dto.getSpecifier().getValue());
        }
        address.setPostalCode(dto.getPostalCode());
        address.setPostOffice(dto.getPostOffice());
        if (dto.hasCountry()) {
            address.setCountry(dto.getCountry().getValue());
        }
        return address;
    }

    private org.example.api.contact.PostalAddress createPostalAddressDto(PostalAddress postalAddress) {
        var builder = org.example.api.contact.PostalAddress.newBuilder();
        if (postalAddress.getStreet() != null) {
            builder.setStreetAddress(postalAddress.getStreet());
        }
        if (postalAddress.getNumber() != null) {
            builder.setNumber(postalAddress.getNumber());
        }
        if (postalAddress.getSpecifier() != null) {
            builder.setSpecifier(StringValue.newBuilder().setValue(postalAddress.getSpecifier()).build());
        }
        if (postalAddress.getPostalCode() != null) {
            builder.setSpecifier(StringValue.newBuilder().setValue(postalAddress.getPostalCode()).build());
        }
        if (postalAddress.getPostOffice() != null) {
            builder.setPostOffice(postalAddress.getPostOffice());
        }
        if (postalAddress.getCountry() != null) {
            builder.setCountry(StringValue.newBuilder().setValue(postalAddress.getCountry()).build());
        }
        return builder.build();
    }

    private PersonEntity createPersonEntityDto(Person entity) {
        var builder = PersonEntity.newBuilder();
        if (entity.getId() != null) {
            builder.setId(entity.getId());
        }
        if (entity.getVersion() != null) {
            builder.setVersion(entity.getVersion());
        }
        if (entity.getCreatedOn() != null) {
            builder.setCreatedOn(TimeUtils.wrapInstant(entity.getCreatedOn()));
        }
        if (entity.getLastModifiedOn() != null) {
            builder.setLastModifiedOn(TimeUtils.wrapInstant(entity.getLastModifiedOn()));
        }
        builder.setData(createPersonDto(entity));
        return builder.build();
    }

    private org.example.api.person.data.Person createPersonDto(Person entity) {
        var builder = org.example.api.person.data.Person.newBuilder();
        if (entity.getSurname() != null) {
            builder.setSurname(entity.getSurname());
        }
        if (entity.getGivenNames() != null) {
            builder.addAllGivenNames(List.of(entity.getGivenNames().split(" ")));
        }
        if (entity.getDateOfBirth() != null) {
            builder.setDateOfBirth(TimeUtils.wrapLocalDate(entity.getDateOfBirth()));
        }
        if (entity.getDateOfDeath() != null) {
            builder.setDateOfDeath(TimeUtils.wrapLocalDate(entity.getDateOfDeath()));
        }
        if (entity.isAlive()) {
            builder.setStatus(Status.ALIVE);
        } else if (entity.isDeceased()) {
            builder.setStatus(Status.DECEASED);
        }
        if (entity.getSex() != null) {
            builder.setSex(org.example.api.person.data.Sex.valueOf(entity.getSex().name()));
        }
        if (entity.getPersonalIdentityCode() != null) {
            builder.setPersonalIdentityCode(PersonalIdentityCode.newBuilder().setCode(entity.getPersonalIdentityCode()).build());
        }
        if (entity.getEmailAddress() != null) {
            builder.setEmailAddress(EmailAddress.newBuilder().setEmailAddress(entity.getEmailAddress()).build());
        }
        if (entity.getPhoneNumber() != null) {
            builder.setPhoneNumber(PhoneNumber.newBuilder().setPhoneNumber(entity.getPhoneNumber()).build());
        }
        if (entity.getPostalAddress() != null) {
            builder.setPostalAddress(createPostalAddressDto(entity.getPostalAddress()));
        }
        return builder.build();
    }

    @Override
    public void findPerson(PersonQuery request, StreamObserver<PersonQueryResponse> responseObserver) {
        if (request.hasPersonalIdentityCode()) {
            responseObserver.onNext(createResponse(request, personManagementPort.findByPersonalIdentityCode(request.getPersonalIdentityCode().getCode()).stream().collect(Collectors.toList())));
        } else if (request.hasDateOfBirth()) {
            responseObserver.onNext(createResponse(request, personManagementPort.findByDateOfBirth(TimeUtils.unwrapDate(request.getDateOfBirth()))));
        } else if (request.hasName()) {
            responseObserver.onNext(createResponse(request, personManagementPort.findByName(request.getName())));
        }
        responseObserver.onCompleted();
    }

    private PersonQueryResponse createResponse(PersonQuery query, List<Person> result) {
        var builder = PersonQueryResponse.newBuilder();
        builder.setQuery(query).setTimestamp(TimeUtils.wrapInstant(Instant.now())).addAllResult(result.stream().map(this::createPersonEntityDto).collect(Collectors.toList()));
        return builder.build();
    }
}
