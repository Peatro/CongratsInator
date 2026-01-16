package com.peatroxd.congratsinator.congratsinator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDto {
    private UUID id;
    private String name;
    private LocalDate birthday;
    private String photoUrl;
}
