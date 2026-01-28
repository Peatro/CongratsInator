package com.peatroxd.congratsinator.controller.impl;

import com.peatroxd.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.mapper.PersonMapper;
import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.service.BirthdayService;
import com.peatroxd.congratsinator.testdata.JsonPersonAsserts;
import com.peatroxd.congratsinator.testdata.PersonDtos;
import com.peatroxd.congratsinator.testdata.Persons;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.peatroxd.congratsinator.controller.ApiPathsTestData.BIRTHDAY_CONTROLLER_DAYS_PARAM;
import static com.peatroxd.congratsinator.controller.ApiPathsTestData.BIRTHDAY_CONTROLLER_GET_TODAY_BIRTHDAYS;
import static com.peatroxd.congratsinator.controller.ApiPathsTestData.BIRTHDAY_CONTROLLER_GET_UPCOMING_BIRTHDAYS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BirthdayControllerImpl.class)
class BirthdayControllerImplTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BirthdayService birthdayService;

    @MockitoBean
    PersonMapper personMapper;

    @Test
    void today_returnsDtos() throws Exception {
        Person p1 = Persons.persistedWithName("Alice");
        Person p2 = Persons.persistedWithName("Bob");

        PersonDto d1 = PersonDtos.from(p1);
        PersonDto d2 = PersonDtos.from(p2);

        when(birthdayService.getTodayBirthdays()).thenReturn(List.of(p1, p2));
        when(personMapper.toDto(p1)).thenReturn(d1);
        when(personMapper.toDto(p2)).thenReturn(d2);

        mockMvc.perform(get(BIRTHDAY_CONTROLLER_GET_TODAY_BIRTHDAYS).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(JsonPersonAsserts.listSize(2))
                .andExpect(JsonPersonAsserts.dtoAt(0, d1))
                .andExpect(JsonPersonAsserts.dtoAt(1, d2));

        verify(birthdayService).getTodayBirthdays();
        verify(personMapper).toDto(p1);
        verify(personMapper).toDto(p2);
        verifyNoMoreInteractions(birthdayService, personMapper);
    }

    @Test
    void upcoming_withoutDaysParam_usesDefault7() throws Exception {
        Person person = Persons.persistedWithName("John");
        PersonDto dto = PersonDtos.from(person);

        when(birthdayService.getUpcomingBirthdays(7)).thenReturn(List.of(person));
        when(personMapper.toDto(person)).thenReturn(dto);

        mockMvc.perform(get(BIRTHDAY_CONTROLLER_GET_UPCOMING_BIRTHDAYS).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(JsonPersonAsserts.listSize(1))
                .andExpect(JsonPersonAsserts.dtoAt(0, dto));

        verify(birthdayService).getUpcomingBirthdays(7);
        verify(personMapper).toDto(person);
        verifyNoMoreInteractions(birthdayService, personMapper);
    }

    @Test
    void upcoming_withDaysParam_passesItToService() throws Exception {
        when(birthdayService.getUpcomingBirthdays(3)).thenReturn(List.of());

        mockMvc.perform(get(BIRTHDAY_CONTROLLER_GET_UPCOMING_BIRTHDAYS)
                        .param(BIRTHDAY_CONTROLLER_DAYS_PARAM, "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(JsonPersonAsserts.listSize(0));

        verify(birthdayService).getUpcomingBirthdays(3);
        verifyNoInteractions(personMapper);
        verifyNoMoreInteractions(birthdayService);
    }

    @Test
    void upcoming_withInvalidDays_returns400() throws Exception {
        mockMvc.perform(get(BIRTHDAY_CONTROLLER_GET_UPCOMING_BIRTHDAYS)
                        .param(BIRTHDAY_CONTROLLER_DAYS_PARAM, "lol")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(birthdayService, personMapper);
    }
}
