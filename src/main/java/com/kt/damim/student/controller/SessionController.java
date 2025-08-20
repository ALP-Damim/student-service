package com.kt.damim.student.controller;

import com.kt.damim.student.dto.SessionResponseDto;
import com.kt.damim.student.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Tag(name = "세션 관리", description = "강좌 세션 조회 API")
public class SessionController {
    
    private final SessionService sessionService;
    
    @GetMapping("/classes/{classId}")
    @Operation(summary = "강좌별 세션 목록 조회", description = "특정 강좌의 모든 세션 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (존재하지 않는 클래스 ID)"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<List<SessionResponseDto>> getSessionsByClassId(
            @Parameter(description = "클래스 ID", example = "1") @PathVariable Integer classId) {
        List<SessionResponseDto> sessions = sessionService.getSessionsByClassId(classId);
        return ResponseEntity.ok(sessions);
    }
}
