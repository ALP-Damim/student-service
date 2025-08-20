package com.kt.damim.student.controller;

import com.kt.damim.student.dto.EnrollmentRequestDto;
import com.kt.damim.student.dto.EnrollmentResponseDto;
import com.kt.damim.student.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // 강좌 신청
    @PostMapping
    public ResponseEntity<EnrollmentResponseDto> enrollInClass(@RequestBody EnrollmentRequestDto requestDto) {
        EnrollmentResponseDto enrollment = enrollmentService.enrollInClass(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    // 수강 신청 목록 조회 (다양한 조건으로 필터링 가능)
    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDto>> getEnrollments(
            @RequestParam(required = false) Integer studentId,
            @RequestParam(required = false) Integer classId,
            @RequestParam(required = false) String status) {
        
        List<EnrollmentResponseDto> enrollments = enrollmentService.getEnrollments(studentId, classId, status);
        return ResponseEntity.ok(enrollments);
    }




}
