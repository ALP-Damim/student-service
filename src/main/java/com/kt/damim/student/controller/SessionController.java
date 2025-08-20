package com.kt.damim.student.controller;

import com.kt.damim.student.dto.SessionResponseDto;
import com.kt.damim.student.service.SessionService;
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
public class SessionController {
    
    private final SessionService sessionService;
    
    /**
     * 강좌별 세션 목록 조회
     * GET /api/sessions/classes/{classId}
     */
    @GetMapping("/classes/{classId}")
    public ResponseEntity<List<SessionResponseDto>> getSessionsByClassId(@PathVariable Integer classId) {
        List<SessionResponseDto> sessions = sessionService.getSessionsByClassId(classId);
        return ResponseEntity.ok(sessions);
    }
}
