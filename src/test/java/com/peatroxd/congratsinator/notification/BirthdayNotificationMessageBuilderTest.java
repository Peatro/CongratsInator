package com.peatroxd.congratsinator.notification;

import com.peatroxd.congratsinator.TestData;
import com.peatroxd.congratsinator.model.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BirthdayNotificationMessageBuilderTest {

    private final BirthdayNotificationMessageBuilder builder = new BirthdayNotificationMessageBuilder();

    @Test
    void build_includesHeaderWithDays() {
        Person p = TestData.createPersonUsingDateOfBirth(LocalDate.of(1990, 1, 1));
        p.setName("John");

        String message = builder.build(List.of(p), 7);

        assertThat(message).contains("Ближайшие дни рождения (7 дней):");
    }

    @Test
    void build_includesEachPersonNameAndBirthday() {
        Person p1 = TestData.createPersonUsingDateOfBirth(LocalDate.of(1990, 12, 31));
        p1.setName("Alice");

        Person p2 = TestData.createPersonUsingDateOfBirth(LocalDate.of(1992, 1, 2));
        p2.setName("Bob");

        String message = builder.build(List.of(p1, p2), 7);

        assertThat(message)
                .contains("Alice — 1990-12-31")
                .contains("Bob — 1992-01-02");
    }

    @Test
    void build_endsWithNewLineAfterEachPerson() {
        Person p = TestData.createPersonUsingDateOfBirth(LocalDate.of(1990, 1, 1));
        p.setName("John");

        String message = builder.build(List.of(p), 1);

        assertThat(message).endsWith("\n");
    }
}
