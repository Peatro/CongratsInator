package com.peatroxd.congratsinator.congratsinator.mapper;

import com.peatroxd.congratsinator.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.congratsinator.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    public PersonDto toDto(Person person) {
        String photoUrl = null;

        if (person.getPhotoKey() != null) {
            // Вместо presigned URL возвращаем endpoint Spring
            photoUrl = "/api/persons/" + person.getId() + "/photo";
        }

        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .birthday(person.getBirthday())
                .photoUrl(photoUrl)
                .build();
    }

    public Person toEntity(PersonDto personDto) {
        return new Person(
                personDto.getId(),
                personDto.getName(),
                personDto.getBirthday(),
                null // photoKey оставляем null, обновляем только через загрузку фото
        );
    }
}

