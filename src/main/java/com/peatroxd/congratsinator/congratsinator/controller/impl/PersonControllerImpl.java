package com.peatroxd.congratsinator.congratsinator.controller.impl;

import com.peatroxd.congratsinator.congratsinator.controller.PersonController;
import com.peatroxd.congratsinator.congratsinator.model.Person;
import com.peatroxd.congratsinator.congratsinator.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonControllerImpl implements PersonController {

    private final PersonService service;

    @Override
    @GetMapping
    public ResponseEntity<List<Person>> getAll() {
        List<Person> persons = service.getAll();
        return ResponseEntity.ok(persons);
    }

    @Override
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person saved = service.addPerson(person);
        return ResponseEntity.ok(saved);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Person> editPerson(@PathVariable Long id, @RequestBody Person person) {
        person.setId(id);
        Person updated = service.editPerson(person);
        return ResponseEntity.ok(updated);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        service.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
