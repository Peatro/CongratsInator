package com.peatroxd.congratsinator.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peatroxd.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.mapper.PersonMapper;
import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.service.PersonService;
import com.peatroxd.congratsinator.testdata.JsonPersonAsserts;
import com.peatroxd.congratsinator.testdata.PersonDtos;
import com.peatroxd.congratsinator.testdata.Persons;
import com.peatroxd.congratsinator.testdata.Values;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.peatroxd.congratsinator.controller.ApiPathsTestData.PERSON_CONTROLLER_BASE;
import static com.peatroxd.congratsinator.controller.ApiPathsTestData.PERSON_CONTROLLER_BY_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonControllerImpl.class)
class PersonControllerImplTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PersonService personService;

    @MockitoBean
    PersonMapper personMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAll_returnsListOfDtos() throws Exception {
        Person p1 = Persons.persistedWithName("Alice");
        Person p2 = Persons.persistedWithName("Bob");

        PersonDto d1 = PersonDtos.from(p1);
        PersonDto d2 = PersonDtos.from(p2);

        when(personService.getAll()).thenReturn(List.of(p1, p2));
        when(personMapper.toDto(p1)).thenReturn(d1);
        when(personMapper.toDto(p2)).thenReturn(d2);

        mockMvc.perform(get(PERSON_CONTROLLER_BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(JsonPersonAsserts.listSize(2))
                .andExpect(JsonPersonAsserts.dtoAt(0, d1))
                .andExpect(JsonPersonAsserts.dtoAt(1, d2));

        verify(personService).getAll();
        verify(personMapper).toDto(p1);
        verify(personMapper).toDto(p2);
        verifyNoMoreInteractions(personService, personMapper);
    }

    @Test
    void addPerson_mapsDtoToEntity_callsService_returnsSavedDto() throws Exception {
        PersonDto requestDto = PersonDtos.createRequest(Values.PERSON_NAME, Values.BIRTHDAY);

        Person entityToSave = Persons.toSaveWithNameAndBirthday(Values.PERSON_NAME, Values.BIRTHDAY);
        Person savedEntity = Persons.persistedWithIdNameBirthday(UUID.randomUUID(), Values.PERSON_NAME,
                Values.BIRTHDAY);
        PersonDto responseDto = PersonDtos.from(savedEntity);

        when(personMapper.toEntity(any(PersonDto.class))).thenReturn(entityToSave);
        when(personService.addPerson(entityToSave)).thenReturn(savedEntity);
        when(personMapper.toDto(savedEntity)).thenReturn(responseDto);

        mockMvc.perform(post(PERSON_CONTROLLER_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(JsonPersonAsserts.dto(responseDto));

        verify(personMapper).toEntity(any(PersonDto.class));
        verify(personService).addPerson(entityToSave);
        verify(personMapper).toDto(savedEntity);
        verifyNoMoreInteractions(personService, personMapper);
    }

    @Test
    void editPerson_overridesIdFromPath_evenIfBodyContainsAnother() throws Exception {
        UUID pathId = UUID.randomUUID();
        UUID bodyId = UUID.randomUUID();

        PersonDto requestDto = PersonDtos.updateRequest(bodyId, "Delta");

        Person mappedEntity = Persons.persistedWithIdAndName(bodyId, "Delta");
        Person updatedEntity = Persons.persistedWithIdAndName(pathId, "Delta");
        PersonDto responseDto = PersonDtos.from(updatedEntity);

        when(personMapper.toEntity(any(PersonDto.class))).thenReturn(mappedEntity);
        when(personService.editPerson(argThat(p -> pathId.equals(p.getId()) && "Delta".equals(p.getName()))))
                .thenReturn(updatedEntity);
        when(personMapper.toDto(updatedEntity)).thenReturn(responseDto);

        mockMvc.perform(put(PERSON_CONTROLLER_BY_ID, pathId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(JsonPersonAsserts.dto(responseDto));

        verify(personMapper).toEntity(any(PersonDto.class));
        verify(personService).editPerson(argThat(p -> pathId.equals(p.getId()) && "Delta".equals(p.getName())));
        verify(personMapper).toDto(updatedEntity);
        verifyNoMoreInteractions(personService, personMapper);
    }

    @Test
    void deletePerson_returns204_andCallsService() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete(PERSON_CONTROLLER_BY_ID, id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(personService).deletePerson(id);
        verifyNoInteractions(personMapper);
        verifyNoMoreInteractions(personService);
    }
}
