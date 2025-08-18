package com.kt.damim.student.controller;

import com.kt.damim.student.dto.ClassAttendanceResponseDto;
import com.kt.damim.student.dto.SessionAttendanceResponseDto;
import com.kt.damim.student.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    
    private final AttendanceService attendanceService;
    
    @GetMapping("/class/{studentId}/{classId}")
    public ResponseEntity<ClassAttendanceResponseDto> getClassAttendance(
            @PathVariable Long studentId,
            @PathVariable Long classId) {
        
        ClassAttendanceResponseDto response = attendanceService.getClassAttendance(studentId, classId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/session/{studentId}/{sessionId}")
    public ResponseEntity<SessionAttendanceResponseDto> getSessionAttendance(
            @PathVariable Long studentId,
            @PathVariable Long sessionId) {
        
        SessionAttendanceResponseDto response = attendanceService.getSessionAttendance(studentId, sessionId);
        return ResponseEntity.ok(response);
    }
}
