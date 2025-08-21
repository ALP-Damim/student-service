package com.kt.damim.student.dto;

import com.kt.damim.student.entity.Attendance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceCreateResponseDto {
    
    private Integer sessionId;
    private Integer studentId;
    private Attendance.AttendanceStatus status;
    private String note;
    private OffsetDateTime createdAt;
    
    public static AttendanceCreateResponseDto from(Attendance attendance) {
        return AttendanceCreateResponseDto.builder()
                .sessionId(attendance.getSessionId())
                .studentId(attendance.getStudentId())
                .status(attendance.getStatus())
                .note(attendance.getNote())
                .createdAt(attendance.getCreatedAt())
                .build();
    }
}
