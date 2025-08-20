package com.kt.damim.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDto {
    private Integer userId;
    private String name;
    private String desiredCourse;
    private String desiredJob;
    private LocalDate birthDate;
    private String school;
    private String phone;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
