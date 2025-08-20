package com.kt.damim.student.controller;

import com.kt.damim.student.dto.ClassAttendanceResponseDto;
import com.kt.damim.student.dto.SessionAttendanceResponseDto;
import com.kt.damim.student.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "출석 관리", description = "학생 출석 조회 API")
public class AttendanceController {
    
    private final AttendanceService attendanceService;
    
    @GetMapping("/class/{studentId}/{classId}")
    @Operation(summary = "클래스별 출석 통계 조회", description = "특정 학생의 특정 클래스에 대한 모든 세션 출석 현황과 통계를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (존재하지 않는 학생/클래스 ID)"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<ClassAttendanceResponseDto> getClassAttendance(
            @Parameter(description = "학생 ID", example = "11") @PathVariable Integer studentId,
            @Parameter(description = "클래스 ID", example = "1") @PathVariable Integer classId) {
        
        ClassAttendanceResponseDto response = attendanceService.getClassAttendance(studentId, classId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/session/{studentId}/{sessionId}")
    @Operation(summary = "세션별 출석 조회", description = "특정 학생의 특정 세션에 대한 출석 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (존재하지 않는 학생/세션 ID)"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<SessionAttendanceResponseDto> getSessionAttendance(
            @Parameter(description = "학생 ID", example = "11") @PathVariable Integer studentId,
            @Parameter(description = "세션 ID", example = "1") @PathVariable Integer sessionId) {
        
        SessionAttendanceResponseDto response = attendanceService.getSessionAttendance(studentId, sessionId);
        return ResponseEntity.ok(response);
    }
}
