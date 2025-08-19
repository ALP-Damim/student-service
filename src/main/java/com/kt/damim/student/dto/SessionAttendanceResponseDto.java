package com.kt.damim.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionAttendanceResponseDto {
	private Integer studentId;
	private String studentName;
	private Integer sessionId;
	private String sessionName;
	private String className;
	private AttendanceResultDto attendanceResult;
}
