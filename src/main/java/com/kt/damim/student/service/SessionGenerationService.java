package com.kt.damim.student.service;

import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionGenerationService {
    
    private final SessionRepository sessionRepository;
    
    /**
     * 강의 생성 후 자동으로 10개의 세션을 생성합니다.
     * 요일 비트마스크를 기반으로 해당 요일들에 세션을 생성합니다.
     */
    @Transactional
    public List<Session> generateSessionsForClass(Class classEntity) {
        List<Session> sessions = new ArrayList<>();
        
        // 현재 날짜부터 시작
        LocalDate currentDate = LocalDate.now();
        LocalTime startTime = classEntity.getStartsAt();
        LocalTime endTime = classEntity.getEndsAt();
        
        if (startTime == null || endTime == null) {
            log.warn("강의 시작/종료 시간이 설정되지 않아 세션을 생성할 수 없습니다. classId: {}", classEntity.getClassId());
            return sessions;
        }
        
        int sessionCount = 0;
        int maxAttempts = 100; // 무한 루프 방지
        int attempts = 0;
        
        while (sessionCount < 10 && attempts < maxAttempts) {
            // 현재 날짜의 요일 확인 (1=월요일, 7=일요일)
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            int dayBit = 1 << (dayOfWeek.getValue() - 1);
            
            // 해당 요일에 수업이 있는지 확인
            if (classEntity.getHeldDay() != null && (classEntity.getHeldDay() & dayBit) != 0) {
                // 세션 생성
                OffsetDateTime sessionDateTime = OffsetDateTime.of(
                    LocalDateTime.of(currentDate, startTime),
                    ZoneOffset.ofHours(9) // 한국 시간대
                );
                
                Session session = Session.builder()
                    .classId(classEntity.getClassId())
                    .sessionName(String.format("세션 %d", sessionCount + 1))
                    .onDate(sessionDateTime)
                    .build();
                
                Session savedSession = sessionRepository.save(session);
                sessions.add(savedSession);
                sessionCount++;
                
                log.info("세션 생성 완료: classId={}, sessionId={}, date={}, name={}", 
                    classEntity.getClassId(), savedSession.getSessionId(), 
                    sessionDateTime.toLocalDate(), savedSession.getSessionName());
            }
            
            // 다음 날로 이동
            currentDate = currentDate.plusDays(1);
            attempts++;
        }
        
        log.info("강의 {}에 대한 {}개의 세션이 생성되었습니다.", 
            classEntity.getClassName(), sessions.size());
        
        return sessions;
    }
    
    /**
     * 특정 강의의 세션을 모두 삭제합니다.
     */
    @Transactional
    public void deleteSessionsForClass(Integer classId) {
        List<Session> existingSessions = sessionRepository.findByClassIdOrderByOnDateAsc(classId);
        if (!existingSessions.isEmpty()) {
            sessionRepository.deleteAll(existingSessions);
            log.info("강의 {}의 {}개 세션이 삭제되었습니다.", classId, existingSessions.size());
        }
    }
}
