package com.peatroxd.congratsinator.congratsinator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    private Long id;
    private String name;
    private LocalDate birthday;
    private String photoUrl;
}
