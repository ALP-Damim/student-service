package com.kt.damim.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateRequestDto {
    private String name;
    private String desiredCourse;
    private String desiredJob;
    private LocalDate birthDate;
    private String school;
    private String phone;
}
