package com.peatroxd.congratsinator.congratsinator.service.impl;

import com.peatroxd.congratsinator.congratsinator.model.Person;
import com.peatroxd.congratsinator.congratsinator.repository.PersonRepository;
import com.peatroxd.congratsinator.congratsinator.service.BirthdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BirthdayServiceImpl implements BirthdayService {

    private final PersonRepository repository;

    @Override
    public List<Person> getTodayBirthdays() {
        LocalDate today = LocalDate.now();

        return repository.findAll().stream()
                .filter(p ->
                        p.getBirthday().getDayOfMonth() == today.getDayOfMonth()
                                && p.getBirthday().getMonth() == today.getMonth()
                )
                .toList();
    }

    @Override
    public List<Person> getUpcomingBirthdays(int days) {
        LocalDate today = LocalDate.now();

        return repository.findAll().stream()
                .filter(p -> {
                    long diff = daysUntilNextBirthday(p.getBirthday(), today);
                    return diff >= 0 && diff <= days;
                })
                .sorted(Comparator.comparing(
                        p -> daysUntilNextBirthday(p.getBirthday(), today)
                ))
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
