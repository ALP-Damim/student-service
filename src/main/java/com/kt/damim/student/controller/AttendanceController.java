package com.kt.damim.student.controller;

import com.kt.damim.student.dto.AttendanceCreateRequestDto;
import com.kt.damim.student.dto.AttendanceCreateResponseDto;
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
            @PathVariable Integer studentId,
            @PathVariable Integer classId) {
        
        ClassAttendanceResponseDto response = attendanceService.getClassAttendance(studentId, classId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/session/{studentId}/{sessionId}")
    public ResponseEntity<SessionAttendanceResponseDto> getSessionAttendance(
            @PathVariable Integer studentId,
            @PathVariable Integer sessionId) {
        
        SessionAttendanceResponseDto response = attendanceService.getSessionAttendance(studentId, sessionId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<AttendanceCreateResponseDto> createAttendance(
            @RequestBody AttendanceCreateRequestDto request) {
        
        AttendanceCreateResponseDto response = attendanceService.createAttendance(request);
        return ResponseEntity.status(201).body(response);
    }
}
