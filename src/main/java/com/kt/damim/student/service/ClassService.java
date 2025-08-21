package com.kt.damim.student.service;

import com.kt.damim.student.dto.ClassCreateRequestDto;
import com.kt.damim.student.dto.ClassCreateResponseDto;
import com.kt.damim.student.dto.ClassResponseDto;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassService {

    private final ClassRepository classRepository;
    private final SessionGenerationService sessionGenerationService;

    @Transactional
    public ClassCreateResponseDto createClass(ClassCreateRequestDto requestDto) {
        // 요청 데이터 검증
        validateClassCreateRequest(requestDto);

        // Class 엔티티 생성
        Class newClass = Class.builder()
                .teacherId(requestDto.getTeacherId())
                .teacherName(requestDto.getTeacherName())
                .className(requestDto.getClassName())
                .semester(requestDto.getSemester())
                .schoolYear(requestDto.getSchoolYear())
                .subject(requestDto.getSubject())
                .zoomUrl(requestDto.getZoomUrl())
                .heldDay(requestDto.getHeldDay())
                .startsAt(requestDto.getStartsAt())
                .endsAt(requestDto.getEndsAt())
                .capacity(requestDto.getCapacity())
                .build();

        // 데이터베이스에 저장
        Class savedClass = classRepository.save(newClass);

        // 강의 생성 후 자동으로 10개의 세션 생성
        List<Session> generatedSessions = new ArrayList<>();
        try {
            generatedSessions = sessionGenerationService.generateSessionsForClass(savedClass);
            log.info("강의 '{}' 생성 완료 및 {}개 세션 자동 생성 완료", savedClass.getClassName(), generatedSessions.size());
        } catch (Exception e) {
            log.error("강의는 생성되었지만 세션 생성 중 오류가 발생했습니다: {}", e.getMessage());
            // 강의 생성은 성공했으므로 세션 생성 실패는 로그만 남기고 계속 진행
        }

        // 새로운 응답 DTO로 변환하여 반환
        return ClassCreateResponseDto.from(savedClass, generatedSessions);
    }

    private void validateClassCreateRequest(ClassCreateRequestDto requestDto) {
        if (requestDto.getTeacherId() == null) {
            throw new IllegalArgumentException("교사 ID는 필수입니다.");
        }
        if (requestDto.getTeacherName() == null || requestDto.getTeacherName().trim().isEmpty()) {
            throw new IllegalArgumentException("교사 이름은 필수입니다.");
        }
        if (requestDto.getClassName() == null || requestDto.getClassName().trim().isEmpty()) {
            throw new IllegalArgumentException("강좌명은 필수입니다.");
        }
        if (requestDto.getSemester() == null || requestDto.getSemester().trim().isEmpty()) {
            throw new IllegalArgumentException("학기는 필수입니다.");
        }
        if (requestDto.getHeldDay() == null || requestDto.getHeldDay() < 1 || requestDto.getHeldDay() > 127) {
            throw new IllegalArgumentException("요일 정보는 1~127 사이의 값이어야 합니다.");
        }
        if (requestDto.getStartsAt() == null) {
            throw new IllegalArgumentException("시작 시간은 필수입니다.");
        }
        if (requestDto.getEndsAt() == null) {
            throw new IllegalArgumentException("종료 시간은 필수입니다.");
        }
        if (requestDto.getStartsAt().isAfter(requestDto.getEndsAt())) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }
    }

    private ClassResponseDto toDto(Class c) {
        return ClassResponseDto.builder()
                .classId(c.getClassId())
                .teacherId(c.getTeacherId())
                .teacherName(c.getTeacherName())
                .className(c.getClassName())
                .semester(c.getSemester())
                .schoolYear(c.getSchoolYear())
                .subject(c.getSubject())
                .zoomUrl(c.getZoomUrl())
                .heldDay(c.getHeldDay())
                .heldDaysString(c.getHeldDaysString())
                .startsAt(c.getStartsAt())
                .endsAt(c.getEndsAt())
                .build();
    }
}
