package com.peatroxd.congratsinator.congratsinator.service.impl;

import com.peatroxd.congratsinator.congratsinator.model.Person;
import com.peatroxd.congratsinator.congratsinator.repository.PersonRepository;
import com.peatroxd.congratsinator.congratsinator.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    public Person addPerson(Person person) {
        return repository.save(person);
    }

    public void deletePerson(Long id) {
        repository.deleteById(id);
    }

    public Person editPerson(Person person) {
        return repository.save(person);
    }

    public List<Person> getAll() {
        return repository.findAll();
    }

    public List<Person> getTodayAndUpcoming(int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(daysAhead);
        return repository.findByBirthdayBetween(today, end);
    }
}
