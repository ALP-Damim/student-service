package com.kt.damim.student.service;

import com.kt.damim.student.dto.ClassCreateRequestDto;
import com.kt.damim.student.dto.ClassResponseDto;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;

    @Transactional
    public ClassResponseDto createClass(ClassCreateRequestDto requestDto) {
        // 요청 데이터 검증
        validateClassCreateRequest(requestDto);

        // Class 엔티티 생성
        Class newClass = Class.builder()
                .teacherId(requestDto.getTeacherId())
                .teacherName(requestDto.getTeacherName())
                .className(requestDto.getClassName())
                .semester(requestDto.getSemester())
                .zoomUrl(requestDto.getZoomUrl())
                .heldDay(requestDto.getHeldDay())
                .startsAt(requestDto.getStartsAt())
                .endsAt(requestDto.getEndsAt())
                .capacity(requestDto.getCapacity())
                .build();

        // 데이터베이스에 저장
        Class savedClass = classRepository.save(newClass);

        // DTO로 변환하여 반환
        return toDto(savedClass);
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
                .zoomUrl(c.getZoomUrl())
                .heldDay(c.getHeldDay())
                .heldDaysString(c.getHeldDaysString())
                .startsAt(c.getStartsAt())
                .endsAt(c.getEndsAt())
                .build();
    }
}
