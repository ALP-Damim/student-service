package com.kt.damim.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCreateRequestDto {
    private Integer teacherId;
    private String teacherName;
    private String className;
    private String semester;
    private String zoomUrl;
    private Integer heldDay;
    private LocalTime startsAt;
    private LocalTime endsAt;
    private Integer capacity;
}
