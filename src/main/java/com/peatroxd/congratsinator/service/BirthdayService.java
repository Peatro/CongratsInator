package com.peatroxd.congratsinator.service;

import com.peatroxd.congratsinator.model.Person;

import java.util.List;

public interface BirthdayService {

    List<Person> getTodayBirthdays();

    List<Person> getUpcomingBirthdays(int days);
}
