package com.peatroxd.congratsinator.congratsinator.mapper;

import com.peatroxd.congratsinator.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.congratsinator.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonDto toDto(Person person) {
        return new PersonDto(
                person.getId(),
                person.getName(),
                person.getBirthday(),
                person.getPhotoPath()
        );
    }
}
