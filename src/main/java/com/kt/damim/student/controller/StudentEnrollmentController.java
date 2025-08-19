package com.kt.damim.student.controller;

import com.kt.damim.student.dto.EnrolledClassResponseDto;
import com.kt.damim.student.service.EnrollmentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students/{studentId}/classes")
@RequiredArgsConstructor
public class StudentEnrollmentController {

	private final EnrollmentQueryService enrollmentQueryService;

	@GetMapping
	public ResponseEntity<List<EnrolledClassResponseDto>> getEnrolledClasses(
			@PathVariable Integer studentId,
			@RequestParam(required = false) String semester,
			@RequestParam(required = false) Boolean active,
			@RequestParam(required = false, defaultValue = "false") boolean nearestOnly) {
		if (nearestOnly) {
			return ResponseEntity.ok(enrollmentQueryService.getNearestThree(studentId));
		}
		return ResponseEntity.ok(enrollmentQueryService.getEnrolledClasses(studentId, semester, active));
	}
}
