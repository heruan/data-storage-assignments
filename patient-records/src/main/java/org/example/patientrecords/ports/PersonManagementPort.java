package org.example.patientrecords.ports;

import org.example.patientrecords.data.Person;
import org.example.patientrecords.data.PersonEncryptionKey;
import org.example.patientrecords.data.PersonEncryptionKeyRepository;
import org.example.patientrecords.data.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public Optional<Person> findByPersonalIdentityCode(String pic) {
        return personRepository.findByPersonalIdentityCode(pic);
    }

    public List<Person> findByDateOfBirth(LocalDate dateOfBirth) {
        // In the real world, this would be paginated or have a fixed upper limit.
        return personRepository.findByDateOfBirth(dateOfBirth);
    }

    public List<Person> findByName(String name) {
        // In the real world, this would be more advanced (maybe splitting the input parameter into multiple words separated by spaces). And it would be paginated.
        return personRepository.findByName('%' + name.toLowerCase() + '%');
    }
}
