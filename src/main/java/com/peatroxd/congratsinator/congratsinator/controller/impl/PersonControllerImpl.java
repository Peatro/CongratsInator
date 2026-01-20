package com.peatroxd.congratsinator.congratsinator.controller.impl;

import com.peatroxd.congratsinator.congratsinator.controller.PersonController;
import com.peatroxd.congratsinator.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.congratsinator.mapper.PersonMapper;
import com.peatroxd.congratsinator.congratsinator.model.Person;
import com.peatroxd.congratsinator.congratsinator.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonControllerImpl implements PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @Override
    @GetMapping
    public ResponseEntity<List<PersonDto>> getAll() {
        List<PersonDto> result = personService.getAll().stream()
                .map(personMapper::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }

    @Override
    @PostMapping
    public ResponseEntity<PersonDto> addPerson(@RequestBody PersonDto dto) {
        Person saved = personService.addPerson(personMapper.toEntity(dto));

        return ResponseEntity.ok(personMapper.toDto(saved));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> editPerson(
            @PathVariable UUID id,
            @RequestBody PersonDto dto
    ) {
        Person person = personMapper.toEntity(dto);
        person.setId(id);

        Person updated = personService.editPerson(person);

        return ResponseEntity.ok(personMapper.toDto(updated));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}

