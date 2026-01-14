package com.peatroxd.congratsinator.congratsinator.service;

import com.peatroxd.congratsinator.congratsinator.model.Person;

import java.util.List;

public interface PersonService {

    Person addPerson(Person person);

    void deletePerson(Long id);

    Person editPerson(Person person);

    List<Person> getAll();

    List<Person> getTodayAndUpcoming(int daysAhead);
}
