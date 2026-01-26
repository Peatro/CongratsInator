package com.peatroxd.congratsinator.controller;

import com.peatroxd.congratsinator.dto.PersonDto;

import java.util.List;

public interface BirthdayController {

    List<PersonDto> getTodayBirthdays();

    List<PersonDto> getUpcomingBirthdays(int days);
}
