package com.kt.damim.student.controller;

import com.kt.damim.student.dto.EnrollmentRequestDto;
import com.kt.damim.student.dto.EnrollmentResponseDto;
import com.kt.damim.student.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Tag(name = "수강 신청 관리", description = "수강 신청 및 조회 API")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "강좌 신청", description = "학생이 특정 강좌에 수강 신청을 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "수강 신청 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "이미 수강 신청된 강좌"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<EnrollmentResponseDto> enrollInClass(
            @Parameter(description = "수강 신청 요청 데이터", required = true)
            @RequestBody EnrollmentRequestDto requestDto) {
        EnrollmentResponseDto enrollment = enrollmentService.enrollInClass(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    @GetMapping
    @Operation(summary = "수강 신청 목록 조회", description = "다양한 조건으로 수강 신청 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<List<EnrollmentResponseDto>> getEnrollments(
            @Parameter(description = "학생 ID", example = "1") @RequestParam(required = false) Integer studentId,
            @Parameter(description = "클래스 ID", example = "1") @RequestParam(required = false) Integer classId,
            @Parameter(description = "수강 상태", example = "ENROLLED") @RequestParam(required = false) String status) {
        
        List<EnrollmentResponseDto> enrollments = enrollmentService.getEnrollments(studentId, classId, status);
        return ResponseEntity.ok(enrollments);
    }
}
