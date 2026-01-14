package com.peatroxd.congratsinator.congratsinator.controller.impl;

import com.peatroxd.congratsinator.congratsinator.controller.BirthdayController;
import com.peatroxd.congratsinator.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.congratsinator.mapper.PersonMapper;
import com.peatroxd.congratsinator.congratsinator.service.BirthdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/birthdays")
@RequiredArgsConstructor
public class BirthdayControllerImpl implements BirthdayController {

    private final BirthdayService birthdayService;
    private final PersonMapper personMapper;

    @GetMapping("/today")
    @Override
    public List<PersonDto> getTodayBirthdays() {
        return birthdayService.getTodayBirthdays().stream()
                .map(personMapper::toDto)
                .toList();
    }

    @GetMapping("/upcoming")
    @Override
    public List<PersonDto> getUpcomingBirthdays(
            @RequestParam(defaultValue = "7") int days
    ) {
        return birthdayService.getUpcomingBirthdays(days).stream()
                .map(personMapper::toDto)
                .toList();
    }
}
