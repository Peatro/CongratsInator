package com.peatroxd.congratsinator.congratsinator.service.impl;

import com.peatroxd.congratsinator.congratsinator.model.Person;
import com.peatroxd.congratsinator.congratsinator.repository.PersonRepository;
import com.peatroxd.congratsinator.congratsinator.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        repository.findById(id).ifPresent(repository::delete);
    }

    public Person editPerson(Person person) {
        return repository.save(person);
    }

    public List<Person> getAll() {
        return repository.findAll();
    }

    @Transactional
    public void updatePhotoPath(UUID id, String fileName) {
        Person person = findByIdOrThrow(id);

        person.setPhotoKey(fileName);
    }

    public Person getById(UUID id) {
        return findByIdOrThrow(id);
    }

    private Person findByIdOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found: " + id));
    }
}
