package com.kt.damim.student.controller;

import com.kt.damim.student.dto.ClassCreateRequestDto;
import com.kt.damim.student.dto.ClassResponseDto;
import com.kt.damim.student.service.ClassQueryService;
import com.kt.damim.student.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@Tag(name = "강좌 관리", description = "강좌 등록 및 조회 API")
public class ClassController {

	private final ClassQueryService classQueryService;
	private final ClassService classService;

	@GetMapping
	@Operation(summary = "강좌 목록 조회", description = "모든 강좌를 조회합니다. 다양한 필터링 옵션을 지원합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	public ResponseEntity<List<ClassResponseDto>> getClasses(
			@Parameter(description = "최대 반환 개수", example = "10") @RequestParam(required = false) Integer limit,
			@Parameter(description = "학기 정렬 (asc/desc)", example = "desc") @RequestParam(required = false, name = "semesterOrder") String semesterOrder,
			@Parameter(description = "요일 비트마스크 (1~127)", example = "7") @RequestParam(required = false, name = "day") Integer day,
			@Parameter(description = "시작 ID", example = "1") @RequestParam(required = false, name = "startId") Integer startId,
			@Parameter(description = "교사 ID", example = "1") @RequestParam(required = false, name = "teacherId") Integer teacherId
	) {
		if (semesterOrder != null || day != null || startId != null || teacherId != null) {
			return ResponseEntity.ok(classQueryService.getClassesWithFilter(limit, semesterOrder, day, startId, teacherId));
		}
		return ResponseEntity.ok(classQueryService.getClasses(limit, teacherId));
	}

	@PostMapping
	@Operation(summary = "강좌 등록", description = "새로운 강좌를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "강좌 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	public ResponseEntity<ClassResponseDto> createClass(
			@Parameter(description = "강좌 생성 요청 데이터", required = true)
			@RequestBody ClassCreateRequestDto requestDto) {
		ClassResponseDto createdClass = classService.createClass(requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdClass);
	}

	@PostMapping("/batch")
	@Operation(summary = "강좌 일괄 조회", description = "여러 강좌를 ID 목록으로 한번에 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	public ResponseEntity<List<ClassResponseDto>> getClassesByIds(
			@Parameter(description = "조회할 강좌 ID 목록", required = true, example = "[1, 2, 3]")
			@RequestBody List<Integer> classIds) {
		List<ClassResponseDto> classes = classQueryService.getClassesByIds(classIds);
		return ResponseEntity.ok(classes);
	}
}


