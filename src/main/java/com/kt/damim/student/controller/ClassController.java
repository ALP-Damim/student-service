package com.kt.damim.student.controller;

import com.kt.damim.student.dto.ClassResponseDto;
import com.kt.damim.student.service.ClassQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

	private final ClassQueryService classQueryService;

	@GetMapping
	public ResponseEntity<List<ClassResponseDto>> getClasses(
			@RequestParam(required = false) Integer limit,
			@RequestParam(required = false, name = "semesterOrder") String semesterOrder,
			@RequestParam(required = false, name = "day") Integer day
	) {
		if (semesterOrder != null || day != null) {
			return ResponseEntity.ok(classQueryService.getClassesWithFilter(limit, semesterOrder, day));
		}
		return ResponseEntity.ok(classQueryService.getClasses(limit));
	}
}


