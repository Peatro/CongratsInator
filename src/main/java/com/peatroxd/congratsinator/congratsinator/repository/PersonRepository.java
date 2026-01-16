package com.peatroxd.congratsinator.congratsinator.repository;

import com.peatroxd.congratsinator.congratsinator.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    List<Person> findByBirthdayBetween(LocalDate start, LocalDate end);
}
