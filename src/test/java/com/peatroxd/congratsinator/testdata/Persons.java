package com.peatroxd.congratsinator.testdata;

import com.peatroxd.congratsinator.model.Person;

import java.time.LocalDate;
import java.util.UUID;

public final class Persons {

    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }

    public static Person toSave() {
        return aPerson().withoutId().build();
    }

    public static Person toSaveWithName(String name) {
        return aPerson().withoutId().withName(name).build();
    }

    public static Person toSaveWithNameAndBirthday(String name, LocalDate birthday) {
        return aPerson().withoutId().withName(name).withBirthday(birthday).build();
    }

    public static Person persisted() {
        return aPerson().withId(UUID.randomUUID()).build();
    }

    public static Person persisted(UUID id) {
        return aPerson().withId(id).build();
    }

    public static Person persistedWithName(String name) {
        return aPerson().withId(UUID.randomUUID()).withName(name).build();
    }

    public static Person persistedWithIdAndName(UUID id, String name) {
        return aPerson().withId(id).withName(name).build();
    }

    public static Person persistedWithIdNameBirthday(UUID id, String name, LocalDate birthday) {
        return aPerson().withId(id).withName(name).withBirthday(birthday).build();
    }

    public static Person withName(String name) {
        return aPerson().withName(name).build();
    }

    public static Person withBirthday(LocalDate birthday) {
        return aPerson().withBirthday(birthday).build();
    }

    public static Person withDayMonth(int day, int month) {
        return aPerson().withDayMonth(day, month).build();
    }

    public static Person withoutId() {
        return aPerson().withoutId().build();
    }

    public static Person withoutBirthday() {
        return aPerson().withoutBirthday().build();
    }

    public static Person withoutPhotoKey() {
        return aPerson().withoutPhotoKey().build();
    }

    public static final class PersonBuilder {
        private UUID id = null;
        private String name = Values.PERSON_NAME;
        private LocalDate birthday = Values.BIRTHDAY;
        private String photoKey = Values.PHOTO_KEY;

        public PersonBuilder withId(UUID id) { this.id = id; return this; }
        public PersonBuilder withName(String name) { this.name = name; return this; }

        public PersonBuilder withBirthday(LocalDate birthday) { this.birthday = birthday; return this; }
        public PersonBuilder withoutBirthday() { this.birthday = null; return this; }

        public PersonBuilder withDayMonth(int day, int month) {
            this.birthday = LocalDate.of(Values.BIRTHDAY.getYear(), month, day);
            return this;
        }

        public PersonBuilder withPhotoKey(String photoKey) { this.photoKey = photoKey; return this; }
        public PersonBuilder withoutPhotoKey() { this.photoKey = null; return this; }

        public PersonBuilder withoutId() { this.id = null; return this; }

        public Person build() {
            return Person.builder()
                    .id(id)
                    .name(name)
                    .birthday(birthday)
                    .photoKey(photoKey)
                    .build();
        }
    }

    private Persons() {}
}
