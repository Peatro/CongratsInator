package com.peatroxd.congratsinator.controller;

import com.peatroxd.congratsinator.dto.PersonDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PersonController {
    ResponseEntity<List<PersonDto>> getAll();

    ResponseEntity<PersonDto> addPerson(PersonDto person);

    ResponseEntity<PersonDto> editPerson(UUID id, PersonDto person);

    ResponseEntity<Void> deletePerson(UUID id);
}
