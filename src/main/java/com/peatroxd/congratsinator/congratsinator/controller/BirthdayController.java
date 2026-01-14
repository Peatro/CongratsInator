package com.peatroxd.congratsinator.congratsinator.controller;

import com.peatroxd.congratsinator.congratsinator.dto.PersonDto;

import java.util.List;

public interface BirthdayController {

    List<PersonDto> getTodayBirthdays();

    List<PersonDto> getUpcomingBirthdays(int days);
}
