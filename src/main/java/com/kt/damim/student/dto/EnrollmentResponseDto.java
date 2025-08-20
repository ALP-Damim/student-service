package com.kt.damim.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponseDto {
    private Integer studentId;
    private Integer classId;
    private String status;
    private OffsetDateTime createdAt;
}
