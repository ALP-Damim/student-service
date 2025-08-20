package com.kt.damim.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "강좌 응답 DTO")
public class ClassResponseDto {
	@Schema(description = "클래스 ID", example = "1")
	private Integer classId;
	
	@Schema(description = "교사 ID", example = "1")
	private Integer teacherId;
	
	@Schema(description = "교사 이름", example = "김교수")
	private String teacherName;
	
	@Schema(description = "강좌명", example = "자바 프로그래밍")
	private String className;
	
	@Schema(description = "학기", example = "2024-2")
	private String semester;
	
	@Schema(description = "줌 URL", example = "https://zoom.us/j/123456789")
	private String zoomUrl;
	
	@Schema(description = "요일 비트마스크", example = "7")
	private Integer heldDay;
	
	@Schema(description = "요일 문자열", example = "월,화,수")
	private String heldDaysString;
	
	@Schema(description = "시작 시간", example = "10:00:00")
	private LocalTime startsAt;
	
	@Schema(description = "종료 시간", example = "12:00:00")
	private LocalTime endsAt;
}


