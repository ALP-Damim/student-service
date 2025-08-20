package com.kt.damim.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "세션별 출석 응답 DTO")
public class SessionAttendanceResponseDto {
	@Schema(description = "학생 ID", example = "1")
	private Integer studentId;
	
	@Schema(description = "학생 이름", example = "kim@example.com")
	private String studentName;
	
	@Schema(description = "세션 ID", example = "1")
	private Integer sessionId;
	
	@Schema(description = "세션명", example = "session1")
	private String sessionName;
	
	@Schema(description = "클래스명", example = "2024-1")
	private String className;
	
	@Schema(description = "출석 결과")
	private AttendanceResultDto attendanceResult;
}
