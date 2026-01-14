package com.peatroxd.congratsinator.congratsinator.service;

import com.peatroxd.congratsinator.congratsinator.model.Person;

import java.util.List;

public interface BirthdayService {

    List<Person> getTodayBirthdays();

    List<Person> getUpcomingBirthdays(int days);
}
