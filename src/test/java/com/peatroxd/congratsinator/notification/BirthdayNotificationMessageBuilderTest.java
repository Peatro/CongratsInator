package com.peatroxd.congratsinator.notification;

import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.testdata.Persons;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BirthdayNotificationMessageBuilderTest {


    private final BirthdayNotificationMessageBuilder builder =
            new BirthdayNotificationMessageBuilder();

    @Test
    void build_includesHeaderWithDays() {
        Person person = Persons.aPerson()
                .withName("John")
                .withBirthday(LocalDate.of(1990, 1, 1))
                .build();

        String message = builder.build(List.of(person), 7);

        assertThat(message)
                .contains("Ближайшие дни рождения (7 дней):");
    }

    @Test
    void build_includesEachPersonNameAndBirthday() {
        Person alice = Persons.aPerson()
                .withName("Alice")
                .withBirthday(LocalDate.of(1990, 12, 31))
                .build();

        Person bob = Persons.aPerson()
                .withName("Bob")
                .withBirthday(LocalDate.of(1992, 1, 2))
                .build();

        String message = builder.build(List.of(alice, bob), 7);

        assertThat(message)
                .contains("Alice — 1990-12-31")
                .contains("Bob — 1992-01-02");
    }

    @Test
    void build_endsWithNewLineAfterEachPerson() {
        Person person = Persons.aPerson()
                .withName("John")
                .withBirthday(LocalDate.of(1990, 1, 1))
                .build();

        String message = builder.build(List.of(person), 1);

        assertThat(message).endsWith("\n");
    }
}
