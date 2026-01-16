package com.peatroxd.congratsinator.congratsinator.service.impl;

import com.peatroxd.congratsinator.congratsinator.model.Person;
import com.peatroxd.congratsinator.congratsinator.repository.PersonRepository;
import com.peatroxd.congratsinator.congratsinator.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    public Person addPerson(Person person) {
        return repository.save(person);
    }

    public void deletePerson(UUID id) {
        repository.deleteById(id);
    }

    public Person editPerson(Person person) {
        return repository.save(person);
    }

    public List<Person> getAll() {
        return repository.findAll();
    }

    public void updatePhotoPath(UUID id, String fileName) {
        Person person = repository.findById(id).orElseThrow();
        person.setPhotoKey(fileName);
        repository.save(person);
    }

    public Person getById(UUID id) {
        return repository.findById(id).orElseThrow();
    }
}
