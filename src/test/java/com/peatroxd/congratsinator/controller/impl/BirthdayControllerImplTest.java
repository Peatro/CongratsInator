package com.peatroxd.congratsinator.controller.impl;

import com.peatroxd.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.mapper.PersonMapper;
import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.service.BirthdayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.peatroxd.congratsinator.TestData.createRandomPerson;
import static com.peatroxd.congratsinator.TestData.dtoFrom;
import static com.peatroxd.congratsinator.controller.ApiPathsTestData.BIRTHDAY_CONTROLLER_DAYS_PARAM;
import static com.peatroxd.congratsinator.controller.ApiPathsTestData.BIRTHDAY_CONTROLLER_GET_TODAY_BIRTHDAYS;
import static com.peatroxd.congratsinator.controller.ApiPathsTestData.BIRTHDAY_CONTROLLER_GET_UPCOMING_BIRTHDAYS;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        Person person1 = createRandomPerson();
        Person person2 = createRandomPerson();

        PersonDto d1 = dtoFrom(person1);
        PersonDto d2 = dtoFrom(person2);

        when(birthdayService.getTodayBirthdays()).thenReturn(List.of(person1, person2));
        when(personMapper.toDto(person1)).thenReturn(d1);
        when(personMapper.toDto(person2)).thenReturn(d2);

        mockMvc.perform(get(BIRTHDAY_CONTROLLER_GET_TODAY_BIRTHDAYS).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(person1.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(person1.getName()))
                .andExpect(jsonPath("$[0].birthday").value(person1.getBirthday().toString()))
                .andExpect(jsonPath("$[1].id").value(person2.getId().toString()))
                .andExpect(jsonPath("$[1].name").value(person2.getName()))
                .andExpect(jsonPath("$[1].birthday").value(person2.getBirthday().toString()));

        verify(birthdayService).getTodayBirthdays();
        verify(personMapper).toDto(person1);
        verify(personMapper).toDto(person2);
        verifyNoMoreInteractions(birthdayService, personMapper);
    }

    @Test
    void upcoming_withoutDaysParam_usesDefault7() throws Exception {
        Person person = createRandomPerson();
        PersonDto d = dtoFrom(person);

        when(birthdayService.getUpcomingBirthdays(7)).thenReturn(List.of(person));
        when(personMapper.toDto(person)).thenReturn(d);

        mockMvc.perform(get(BIRTHDAY_CONTROLLER_GET_UPCOMING_BIRTHDAYS).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(person.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(person.getName()))
                .andExpect(jsonPath("$[0].birthday").value(person.getBirthday().toString()));

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
                .andExpect(jsonPath("$.length()").value(0));

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
