package com.kt.damim.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "클래스별 출석 통계 응답 DTO")
public class ClassAttendanceResponseDto {
	@Schema(description = "학생 ID", example = "1")
	private Integer studentId;
	
	@Schema(description = "학생 이름", example = "kim@example.com")
	private String studentName;
	
	@Schema(description = "클래스 ID", example = "1")
	private Integer classId;
	
	@Schema(description = "클래스명", example = "2024-1")
	private String className;
	
	@Schema(description = "출석 결과 목록")
	private List<AttendanceResultDto> attendanceResults;
	
	@Schema(description = "평균 점수", example = "0.0")
	private Double averageScore;
	
	@Schema(description = "총 세션 수", example = "2")
	private Long totalSessions;
	
	@Schema(description = "출석한 세션 수", example = "1")
	private Long attendedSessions;
	
	@Schema(description = "출석률", example = "50.0")
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
