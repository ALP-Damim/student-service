package com.kt.damim.student.dto;

import com.kt.damim.student.entity.Attendance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AttendanceResultDto {
	private Long sessionId;
	private String sessionTitle;
	private LocalDateTime sessionStartTime;
	private LocalDateTime sessionEndTime;
	private Attendance.AttendanceStatus status;
	private Integer score;
	private String comments;
	private LocalDateTime recordedAt;

	public static AttendanceResultDto from(Attendance attendance) {
		return AttendanceResultDto.builder()
			.sessionId(attendance.getSession().getId())
			.sessionTitle(attendance.getSession().getTitle())
			.sessionStartTime(attendance.getSession().getStartTime())
			.sessionEndTime(attendance.getSession().getEndTime())
			.status(attendance.getStatus())
			.score(attendance.getScore())
			.comments(attendance.getComments())
			.recordedAt(attendance.getRecordedAt())
			.build();
	}

	public static AttendanceResultDto createDefault(Long sessionId, String sessionTitle, LocalDateTime startTime, LocalDateTime endTime) {
		return AttendanceResultDto.builder()
			.sessionId(sessionId)
			.sessionTitle(sessionTitle)
			.sessionStartTime(startTime)
			.sessionEndTime(endTime)
			.status(Attendance.AttendanceStatus.ABSENT)
			.score(0)
			.comments("출석 기록 없음")
			.build();
	}
}
