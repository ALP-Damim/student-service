package com.kt.damim.student.dto;

import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Session;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "출석 결과 DTO")
public class AttendanceResultDto {
	@Schema(description = "세션 ID", example = "1")
	private Integer sessionId;
	
	@Schema(description = "세션명", example = "session1")
	private String sessionName;
	
	@Schema(description = "세션 날짜", example = "2024-01-15T10:00:00Z")
	private OffsetDateTime sessionOnDate;
	
	@Schema(description = "출석 상태", example = "PRESENT")
	private Attendance.AttendanceStatus status;
	
	@Schema(description = "비고", example = "좋은 수업이었습니다")
	private String note;
	
	@Schema(description = "기록일시", example = "2024-01-15T10:05:00Z")
	private OffsetDateTime recordedAt;

	public static AttendanceResultDto from(Attendance attendance, Session session) {
		return AttendanceResultDto.builder()
			.sessionId(attendance.getSessionId())
			.sessionName(session != null ? session.getSessionName() : null)
			.sessionOnDate(session != null ? session.getOnDate() : null)
			.status(attendance.getStatus())
			.note(attendance.getNote())
			.recordedAt(attendance.getCreatedAt())
			.build();
	}

	public static AttendanceResultDto from(Attendance attendance) {
		return AttendanceResultDto.builder()
			.sessionId(attendance.getSessionId())
			.sessionName(null)
			.sessionOnDate(null) // 세션 정보는 별도로 조회해야 함
			.status(attendance.getStatus())
			.note(attendance.getNote())
			.recordedAt(attendance.getCreatedAt())
			.build();
	}

	public static AttendanceResultDto createDefault(Integer sessionId, OffsetDateTime onDate) {
		return AttendanceResultDto.builder()
			.sessionId(sessionId)
			.sessionName("Session")
			.sessionOnDate(onDate)
			.status(Attendance.AttendanceStatus.ABSENT)
			.note("출석 기록 없음")
			.build();
	}
}
