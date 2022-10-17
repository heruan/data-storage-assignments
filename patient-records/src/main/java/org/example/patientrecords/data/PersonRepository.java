package org.example.patientrecords.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long>, QuerydslPredicateExecutor<Person> {

    Optional<Person> findByPersonalIdentityCode(String pic);

    List<Person> findByDateOfBirth(LocalDate dateOfBirth);

    @Query("select p from Person p where lower(p.surname) LIKE :name or lower(p.givenNames) LIKE :name")
    List<Person> findByName(String name);
}
