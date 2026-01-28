package com.peatroxd.congratsinator.testdata;

import com.peatroxd.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.model.Person;

import java.time.LocalDate;
import java.util.UUID;

public final class PersonDtos {

    public static PersonDtoBuilder aPersonDto() {
        return new PersonDtoBuilder();
    }

    public static PersonDto from(Person person) {
        return aPersonDto()
                .withId(person.getId())
                .withName(person.getName())
                .withBirthday(person.getBirthday())
                .withPhotoUrl(null)
                .build();
    }

    public static PersonDto createRequest(String name) {
        return aPersonDto()
                .withId(null)
                .withName(name)
                .withBirthday(null)
                .withPhotoUrl(null)
                .build();
    }

    public static PersonDto createRequest(String name, LocalDate birthday) {
        return aPersonDto()
                .withId(null)
                .withName(name)
                .withBirthday(birthday)
                .withPhotoUrl(null)
                .build();
    }

    public static PersonDto updateRequest(UUID bodyId, String name) {
        return aPersonDto()
                .withId(bodyId)
                .withName(name)
                .withBirthday(null)
                .withPhotoUrl(null)
                .build();
    }

    public static PersonDto updateRequest(UUID bodyId, String name, LocalDate birthday) {
        return aPersonDto()
                .withId(bodyId)
                .withName(name)
                .withBirthday(birthday)
                .withPhotoUrl(null)
                .build();
    }

    public static final class PersonDtoBuilder {
        private UUID id = null;
        private String name = Values.PERSON_NAME;
        private LocalDate birthday = Values.BIRTHDAY;
        private String photoUrl = null;

        public PersonDtoBuilder withId(UUID id) { this.id = id; return this; }
        public PersonDtoBuilder withName(String name) { this.name = name; return this; }

        public PersonDtoBuilder withBirthday(LocalDate birthday) { this.birthday = birthday; return this; }
        public PersonDtoBuilder withoutBirthday() { this.birthday = null; return this; }

        public PersonDtoBuilder withPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; return this; }
        public PersonDtoBuilder withoutPhotoUrl() { this.photoUrl = null; return this; }

        public PersonDto build() {
            return PersonDto.builder()
                    .id(id)
                    .name(name)
                    .birthday(birthday)
                    .photoUrl(photoUrl)
                    .build();
        }
    }

    private PersonDtos() {}
}
