package org.example.patientrecords.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonEncryptionKeyRepository extends JpaRepository<PersonEncryptionKey, Long> {

    Optional<PersonEncryptionKey> findByPerson(Person person);
}
