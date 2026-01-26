package com.peatroxd.congratsinator.mapper;

import com.peatroxd.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.util.PhotoUrlResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonMapper {

    private final PhotoUrlResolver photoUrlResolver;

    public PersonDto toDto(Person person) {
        String photoUrl = null;

        if(person.getPhotoKey() != null) {
            try {
                photoUrl = photoUrlResolver.resolvePhotoUrl(person.getId());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .birthday(person.getBirthday())
                .photoUrl(photoUrl)
                .build();
    }

    public Person toEntity(PersonDto dto) {
        return new Person(
                dto.getId(),
                dto.getName(),
                dto.getBirthday(),
                null
        );
    }
}

