package com.peatroxd.congratsinator;

import com.peatroxd.congratsinator.model.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestData {
    private static final Random RND = new Random(1);

    public static final UUID STATIC_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final LocalDate BIRTHDAY_DATE = LocalDate.of(1990, 1, 1);
    public static final String PHOTO_KEY = "photo_key_123";
    public static final String PERSON_NAME = "John Doe";

    public static Person createPerson() {
        return johnDoe(generateRandomUUID(), BIRTHDAY_DATE);
    }

    public static Person createPersonWithoutId() {
        return Person.builder()
                .name(PERSON_NAME)
                .birthday(BIRTHDAY_DATE)
                .photoKey(PHOTO_KEY)
                .build();
    }

    public static Person createPersonUsingId(UUID id) {
        return johnDoe(id, BIRTHDAY_DATE);
    }

    public static Person createPersonWithoutPhotoKey() {
        return Person.builder()
                .id(generateRandomUUID())
                .name(PERSON_NAME)
                .birthday(BIRTHDAY_DATE)
                .build();
    }

    public static Person createRandomPerson() {
        return Person.builder()
                .id(generateRandomUUID())
                .name(generateRandomName())
                .birthday(generateRandomBirthday())
                .photoKey(generateRandomPhotoKey())
                .build();
    }

    public static Person createPersonUsingDateOfBirth(LocalDate dateOfBirth) {
        return Person.builder()
                .id(generateRandomUUID())
                .name(generateRandomName())
                .birthday(dateOfBirth)
                .photoKey(generateRandomPhotoKey())
                .build();
    }

    public static Person createPersonUsingDayAndMonth(int day, int month) {
        return person(generateRandomName(), day, month);
    }

    public static Person person(String name, int day, int month) {
        return Person.builder()
                .id(generateRandomUUID())
                .name(name)
                .birthday(LocalDate.of(1990, month, day))
                .photoKey(generateRandomPhotoKey())
                .build();
    }

    public static Person johnDoe(UUID id, LocalDate birthday) {
        return Person.builder()
                .id(id)
                .name(PERSON_NAME)
                .birthday(birthday)
                .photoKey(PHOTO_KEY)
                .build();
    }

    public static List<String> createListOfEmails() {
        return List.of("a@ex.com", "b@ex.com");
    }

    public static List<Person> createRandomPersonsList() {
        return List.of(createRandomPerson(), createRandomPerson(), createRandomPerson());
    }

    public static LocalDate generateRandomBirthday() {
        int year = 1980 + RND.nextInt(40);
        int month = 1 + RND.nextInt(12);
        int day = 1 + RND.nextInt(28);
        return LocalDate.of(year, month, day);
    }

    public static String generateRandomName() {
        return "User_" + UUID.randomUUID().toString().substring(0, 5);
    }

    public static String generateRandomPhotoKey() {
        return "photo_key_" + UUID.randomUUID().toString().substring(0, 5);
    }

    public static String generatePhotoPathUsingId(UUID id) {
        return "photos/" + id + ".jpg";
    }

    public static UUID generateRandomUUID() {
        return UUID.randomUUID();
    }
}
