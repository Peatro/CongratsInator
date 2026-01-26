package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.repository.PersonRepository;
import com.peatroxd.congratsinator.service.BirthdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BirthdayServiceImpl implements BirthdayService {

    private final PersonRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Person> getTodayBirthdays() {
        MonthDay today = MonthDay.from(LocalDate.now());

        return repository.findByBirthdayMonthAndBirthdayDay(today.getMonthValue(), today.getDayOfMonth());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getUpcomingBirthdays(int days) {
        LocalDate today = LocalDate.now();

        return repository.findAll().stream()
                .filter(p -> p.getBirthday() != null)
                .sorted(Comparator.comparingLong(p -> daysUntilNextBirthday(p.getBirthday(), today)))
                .filter(p -> daysUntilNextBirthday(p.getBirthday(), today) <= days)
                .toList();
    }

    private long daysUntilNextBirthday(LocalDate birthday, LocalDate today) {
        LocalDate next = birthday.withYear(today.getYear());
        if (next.isBefore(today)) {
            next = next.plusYears(1);
        }
        return ChronoUnit.DAYS.between(today, next);
    }
}
