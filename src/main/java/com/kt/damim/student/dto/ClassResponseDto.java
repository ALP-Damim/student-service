package com.kt.damim.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassResponseDto {
	private Integer classId;
	private Integer teacherId;
	private String teacherName;
	private String className;
	private String semester;
	private String schoolYear;
	private String subject;
	private String zoomUrl;
	private Integer heldDay;
	private String heldDaysString;
	private LocalTime startsAt;
	private LocalTime endsAt;
}


