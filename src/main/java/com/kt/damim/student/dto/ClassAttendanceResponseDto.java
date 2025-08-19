package com.kt.damim.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassAttendanceResponseDto {
	private Integer studentId;
	private String studentName;
	private Integer classId;
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

		// score 필드가 제거되었으므로 평균 점수 계산 로직 제거
		this.averageScore = 0.0;
	}
}
