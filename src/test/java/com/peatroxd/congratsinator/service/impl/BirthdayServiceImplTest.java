package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.TestData;
import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.peatroxd.congratsinator.TestData.createPersonUsingDateOfBirth;
import static com.peatroxd.congratsinator.TestData.createPersonUsingDayAndMonth;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BirthdayServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private BirthdayServiceImpl birthdayServiceImpl;

    @Test
    void getTodayBirthdays_callsRepositoryWithTodayMonthAndDay() {
        LocalDate fixedToday = LocalDate.of(2026, 1, 21);
        List<Person> repoResult = TestData.createRandomPersonsList();

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(fixedToday);

            when(personRepository.findByBirthdayMonthAndBirthdayDay(1, 21))
                    .thenReturn(repoResult);

            List<Person> result = birthdayServiceImpl.getTodayBirthdays();

            assertThat(result).isSameAs(repoResult);
            verify(personRepository).findByBirthdayMonthAndBirthdayDay(1, 21);
        }
    }

    @Test
    void getTodayBirthdays_returnsEmpty_whenRepositoryReturnsEmpty() {
        LocalDate fixedToday = LocalDate.of(2026, 1, 21);

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(fixedToday);

            when(personRepository.findByBirthdayMonthAndBirthdayDay(1, 21))
                    .thenReturn(List.of());

            List<Person> result = birthdayServiceImpl.getTodayBirthdays();

            assertThat(result).isEmpty();
            verify(personRepository).findByBirthdayMonthAndBirthdayDay(1, 21);
        }
    }

    @Test
    void getUpcomingBirthdays_filtersNullBirthdays() {
        LocalDate fixedToday = LocalDate.of(2026, 1, 21);

        Person nullBirthday = createPersonUsingDateOfBirth(null);
        Person valid = createPersonUsingDayAndMonth(23, 1);

        when(personRepository.findAll()).thenReturn(List.of(nullBirthday, valid));

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(fixedToday);

            List<Person> result = birthdayServiceImpl.getUpcomingBirthdays(7);

            assertThat(result).containsExactly(valid);
            verify(personRepository).findAll();
        }
    }

    @Test
    void getUpcomingBirthdays_filtersByDays() {
        LocalDate fixedToday = LocalDate.of(2026, 1, 21);

        Person in2DaysPerson = createPersonUsingDayAndMonth(23, 1);
        Person in10DaysPerson = createPersonUsingDayAndMonth(31, 1);

        when(personRepository.findAll()).thenReturn(List.of(in10DaysPerson, in2DaysPerson));

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(fixedToday);

            List<Person> result = birthdayServiceImpl.getUpcomingBirthdays(7);

            assertThat(result).containsExactly(in2DaysPerson);
            verify(personRepository).findAll();
        }
    }

    @Test
    void getUpcomingBirthdays_sortsByNearest() {
        LocalDate fixedToday = LocalDate.of(2026, 1, 21);

        Person in5DaysPerson = createPersonUsingDayAndMonth(26, 1);
        Person in2DaysPerson = createPersonUsingDayAndMonth(23, 1);

        when(personRepository.findAll()).thenReturn(List.of(in5DaysPerson, in2DaysPerson));

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(fixedToday);

            List<Person> result = birthdayServiceImpl.getUpcomingBirthdays(7);

            assertThat(result).containsExactly(in2DaysPerson, in5DaysPerson);
            verify(personRepository).findAll();
        }
    }

    @Test
    void getUpcomingBirthdays_acrossYearBoundary() {
        LocalDate fixedToday = LocalDate.of(2026, 12, 30);

        Person dec31Person = createPersonUsingDayAndMonth(31, 12);
        Person jan2Person = createPersonUsingDayAndMonth(2, 1);
        Person jan10Person = createPersonUsingDayAndMonth(10, 1);

        when(personRepository.findAll()).thenReturn(List.of(jan10Person, jan2Person, dec31Person));

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(fixedToday);

            List<Person> result = birthdayServiceImpl.getUpcomingBirthdays(5);

            assertThat(result).containsExactly(dec31Person, jan2Person);
            verify(personRepository).findAll();
        }
    }
}
