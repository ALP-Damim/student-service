package com.kt.damim.student.service;

import com.kt.damim.student.dto.SessionResponseDto;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    
    private final SessionRepository sessionRepository;
    private final ClassRepository classRepository;
    
    /**
     * 강좌 ID로 세션 목록 조회
     */
    public List<SessionResponseDto> getSessionsByClassId(Integer classId) {
        // 강좌 존재 여부 확인
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("강좌를 찾을 수 없습니다. classId: " + classId));
        
        // 해당 강좌의 세션들 조회
        List<Session> sessions = sessionRepository.findByClassIdOrderByOnDateAsc(classId);
        
        // DTO로 변환하여 반환
        return sessions.stream()
                .map(SessionResponseDto::from)
                .collect(Collectors.toList());
    }
}
