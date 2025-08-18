package com.kt.damim.student.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ClassAttendanceResponseDto {
	private Long studentId;
	private String studentName;
	private Long classId;
	private String className;
	private List<AttendanceResultDto> attendanceResults;
	private Double averageScore;
	private Long totalSessions;
	private Long attendedSessions;
	private Double attendanceRate;

	public void calculateStatistics() {
		if (attendanceResults == null || attendanceResults.isEmpty()) {
			this.averageScore = 0.0;
			this.totalSessions = 0L;
			this.attendedSessions = 0L;
			this.attendanceRate = 0.0;
			return;
		}

		this.totalSessions = (long) attendanceResults.size();
		this.attendedSessions = attendanceResults.stream()
			.filter(result -> result.getStatus() == com.kt.damim.student.entity.Attendance.AttendanceStatus.PRESENT)
			.count();

		this.attendanceRate = totalSessions > 0 ? (double) attendedSessions / totalSessions * 100 : 0.0;

		this.averageScore = attendanceResults.stream()
			.filter(result -> result.getScore() != null)
			.mapToInt(AttendanceResultDto::getScore)
			.average()
			.orElse(0.0);
	}
}
