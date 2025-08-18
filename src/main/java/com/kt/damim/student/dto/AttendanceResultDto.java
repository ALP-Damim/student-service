package com.kt.damim.student.dto;

import com.kt.damim.student.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
        AttendanceResultDto dto = new AttendanceResultDto();
        dto.setSessionId(attendance.getSession().getId());
        dto.setSessionTitle(attendance.getSession().getTitle());
        dto.setSessionStartTime(attendance.getSession().getStartTime());
        dto.setSessionEndTime(attendance.getSession().getEndTime());
        dto.setStatus(attendance.getStatus());
        dto.setScore(attendance.getScore());
        dto.setComments(attendance.getComments());
        dto.setRecordedAt(attendance.getRecordedAt());
        return dto;
    }
}
