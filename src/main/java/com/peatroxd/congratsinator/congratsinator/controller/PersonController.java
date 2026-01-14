package com.peatroxd.congratsinator.congratsinator.controller;

import com.peatroxd.congratsinator.congratsinator.model.Person;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PersonController {
    ResponseEntity<List<Person>> getAll();

    ResponseEntity<List<Person>> getUpcoming(int daysAhead);

    ResponseEntity<Person> addPerson(Person person);

    ResponseEntity<Person> editPerson(Long id, Person person);

    ResponseEntity<Void> deletePerson(Long id);
}
