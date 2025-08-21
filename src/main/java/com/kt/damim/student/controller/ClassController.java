package com.kt.damim.student.controller;

import com.kt.damim.student.dto.ClassCreateRequestDto;
import com.kt.damim.student.dto.ClassCreateResponseDto;
import com.kt.damim.student.dto.ClassResponseDto;
import com.kt.damim.student.service.ClassQueryService;
import com.kt.damim.student.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

	private final ClassQueryService classQueryService;
	private final ClassService classService;

	@GetMapping
	public ResponseEntity<List<ClassResponseDto>> getClasses(
			@RequestParam(required = false) Integer limit,
			@RequestParam(required = false, name = "semesterOrder") String semesterOrder,
			@RequestParam(required = false, name = "day") Integer day,
			@RequestParam(required = false, name = "startId") Integer startId,
			@RequestParam(required = false, name = "teacherId") Integer teacherId
	) {
		if (semesterOrder != null || day != null || startId != null || teacherId != null) {
			return ResponseEntity.ok(classQueryService.getClassesWithFilter(limit, semesterOrder, day, startId, teacherId));
		}
		return ResponseEntity.ok(classQueryService.getClasses(limit, teacherId));
	}

	@PostMapping
	public ResponseEntity<ClassCreateResponseDto> createClass(@RequestBody ClassCreateRequestDto requestDto) {
		ClassCreateResponseDto createdClass = classService.createClass(requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdClass);
	}

	// 여러 강좌를 한번에 조회
	@PostMapping("/batch")
	public ResponseEntity<List<ClassResponseDto>> getClassesByIds(@RequestBody List<Integer> classIds) {
		List<ClassResponseDto> classes = classQueryService.getClassesByIds(classIds);
		return ResponseEntity.ok(classes);
	}
}


