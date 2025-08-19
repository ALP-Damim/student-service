package com.kt.damim.student.dto;

import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResultDto {
	private Integer sessionId;
	private String sessionTitle;
	private OffsetDateTime sessionOnDate;
	private Attendance.AttendanceStatus status;
	private String note;
	private OffsetDateTime recordedAt;

	public static AttendanceResultDto from(Attendance attendance, Session session) {
		return AttendanceResultDto.builder()
			.sessionId(attendance.getSessionId())
			.sessionTitle("Session") // Session 엔티티에는 title이 없으므로 기본값 사용
			.sessionOnDate(session != null ? session.getOnDate() : null)
			.status(attendance.getStatus())
			.note(attendance.getNote())
			.recordedAt(attendance.getCreatedAt())
			.build();
	}

	public static AttendanceResultDto from(Attendance attendance) {
		return AttendanceResultDto.builder()
			.sessionId(attendance.getSessionId())
			.sessionTitle("Session") // Session 엔티티에는 title이 없으므로 기본값 사용
			.sessionOnDate(null) // 세션 정보는 별도로 조회해야 함
			.status(attendance.getStatus())
			.note(attendance.getNote())
			.recordedAt(attendance.getCreatedAt())
			.build();
	}

	public static AttendanceResultDto createDefault(Integer sessionId, OffsetDateTime onDate) {
		return AttendanceResultDto.builder()
			.sessionId(sessionId)
			.sessionTitle("Session")
			.sessionOnDate(onDate)
			.status(Attendance.AttendanceStatus.ABSENT)
			.note("출석 기록 없음")
			.build();
	}
}
