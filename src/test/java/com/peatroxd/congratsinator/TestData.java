package com.peatroxd.congratsinator;

import com.peatroxd.congratsinator.model.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TestData {
    public static final UUID STATIC_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final LocalDate BIRTHDAY_DATE = LocalDate.of(1990, 1, 1);
    public static final String PHOTO_KEY = "photo_key_123";
    public static final String PERSON_NAME = "John Doe";
    public static final UUID RANDOM_UUID = UUID.randomUUID();

    public static Person createPerson() {
        return Person.builder()
                .id(RANDOM_UUID)
                .name(PERSON_NAME)
                .birthday(BIRTHDAY_DATE)
                .photoKey(PHOTO_KEY)
                .build();
    }

    public static Person createPersonWithoutId() {
        return Person.builder()
                .name(PERSON_NAME)
                .birthday(BIRTHDAY_DATE)
                .photoKey(PHOTO_KEY)
                .build();
    }

    public static Person createPersonUsingId(UUID id) {
        return Person.builder()
                .id(id)
                .name(PERSON_NAME)
                .birthday(BIRTHDAY_DATE)
                .photoKey(PHOTO_KEY)
                .build();
    }

    public static Person createPersonWithoutPhotoKey() {
        return Person.builder()
                .id(RANDOM_UUID)
                .name(PERSON_NAME)
                .birthday(BIRTHDAY_DATE)
                .build();
    }

    public static Person createRandomPerson() {
        return Person.builder()
                .id(RANDOM_UUID)
                .name(generateRandomName())
                .birthday(generateRandomBirthday())
                .photoKey(generateRandomPhotoKey())
                .build();
    }

    public static List<Person> createRandomPersons() {
        return List.of(
                createRandomPerson(),
                createRandomPerson(),
                createRandomPerson()
        );
    }

    public static LocalDate generateRandomBirthday() {
        int year = 1980 + (int)(Math.random() * 40);
        int month = 1 + (int)(Math.random() * 12);
        int day = 1 + (int)(Math.random() * 28);
        return LocalDate.of(year, month, day);
    }

    public static String generateRandomName() {
        return "User_" + UUID.randomUUID().toString().substring(0, 5);
    }

    public static String generateRandomPhotoKey() {
        return "photo_key_" + UUID.randomUUID().toString().substring(0, 5);
    }

    public static String generatePhotoPathUsingId(UUID id) {
        return "photos/" + id.toString() + ".jpg";
    }
}
