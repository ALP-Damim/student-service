package com.kt.damim.student.dto;

import com.kt.damim.student.entity.Attendance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceCreateRequestDto {
    
    private Integer sessionId;
    private Integer studentId;
    private Attendance.AttendanceStatus status;
    private String note;
}
