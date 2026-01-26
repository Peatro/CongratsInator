package com.peatroxd.congratsinator.service;

import com.peatroxd.congratsinator.model.Person;

import java.util.List;
import java.util.UUID;

public interface PersonService {

    Person addPerson(Person person);

    void deletePerson(UUID id);

    Person editPerson(Person person);

    List<Person> getAll();

    void updatePhotoPath(UUID id, String fileName);

    Person getById(UUID id);
}
