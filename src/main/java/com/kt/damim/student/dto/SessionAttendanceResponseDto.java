package com.kt.damim.student.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionAttendanceResponseDto {
	private Long studentId;
	private String studentName;
	private Long sessionId;
	private String sessionTitle;
	private String className;
	private AttendanceResultDto attendanceResult;
}
