package com.kt.damim.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrolledClassResponseDto {
	private Integer classId;
	private Integer teacherId;
	private String teacherName;
	private String className;
	private String semester;
	private String zoomUrl;
	private Integer heldDay;
	private String heldDaysString;
	private LocalTime startsAt;
	private LocalTime endsAt;
	// 가까운 강좌 조회 시 사용 (없으면 null)
	private OffsetDateTime nextStartAt;
}
