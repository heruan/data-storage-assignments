package org.example.patientrecords.ports;

import org.example.patientrecords.data.Person;
import org.example.patientrecords.data.PersonEncryptionKey;
import org.example.patientrecords.data.PersonEncryptionKeyRepository;
import org.example.patientrecords.data.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PersonManagementPort {

    private final PersonRepository personRepository;
    private final PersonEncryptionKeyRepository personEncryptionKeyRepository;

    public PersonManagementPort(PersonRepository personRepository,
                                PersonEncryptionKeyRepository personEncryptionKeyRepository) {
        this.personRepository = personRepository;
        this.personEncryptionKeyRepository = personEncryptionKeyRepository;
    }

    public Person addPerson(Person person) {
        var savedPerson = personRepository.saveAndFlush(person); // Flush to trigger any database constraints
        personEncryptionKeyRepository.save(new PersonEncryptionKey(savedPerson));
        return savedPerson;
    }
}
