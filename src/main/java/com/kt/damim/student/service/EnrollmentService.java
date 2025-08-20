package com.kt.damim.student.service;

import com.kt.damim.student.dto.EnrollmentRequestDto;
import com.kt.damim.student.dto.EnrollmentResponseDto;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Enrollment;
import com.kt.damim.student.entity.User;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.EnrollmentRepository;
import com.kt.damim.student.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;

    @Transactional
    public EnrollmentResponseDto enrollInClass(EnrollmentRequestDto requestDto) {
        // 요청 데이터 검증
        validateEnrollmentRequest(requestDto);

        // 학생 존재 여부 및 역할 확인
        User student = userRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다: " + requestDto.getStudentId()));

        if (student.getRole() != User.UserRole.STUDENT) {
            throw new IllegalArgumentException("학생이 아닌 사용자는 강좌를 신청할 수 없습니다: " + requestDto.getStudentId());
        }

        // 강좌 존재 여부 확인
        Class classEntity = classRepository.findById(requestDto.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("강좌를 찾을 수 없습니다: " + requestDto.getClassId()));

        // 이미 수강 중인지 확인
        if (enrollmentRepository.existsByStudentIdAndClassId(requestDto.getStudentId(), requestDto.getClassId())) {
            throw new IllegalArgumentException("이미 수강 중인 강좌입니다: 학생 ID " + requestDto.getStudentId() + ", 강좌 ID " + requestDto.getClassId());
        }

        // 수강 신청 생성
        Enrollment enrollment = Enrollment.builder()
                .studentId(requestDto.getStudentId())
                .classId(requestDto.getClassId())
                .status("ENROLLED")
                .createdAt(java.time.OffsetDateTime.now())
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return toDto(savedEnrollment);
    }

    // 수강 신청 목록 조회 (다양한 조건으로 필터링)
    public List<EnrollmentResponseDto> getEnrollments(Integer studentId, Integer classId, String status) {
        List<Enrollment> enrollments;
        
        if (studentId != null && classId != null) {
            enrollments = enrollmentRepository.findByStudentIdAndClassId(studentId, classId);
        } else if (studentId != null) {
            enrollments = enrollmentRepository.findByStudentId(studentId);
        } else if (classId != null) {
            enrollments = enrollmentRepository.findByClassId(classId);
        } else {
            enrollments = enrollmentRepository.findAll();
        }
        
        // 상태 필터링
        if (status != null) {
            enrollments = enrollments.stream()
                    .filter(e -> status.equals(e.getStatus()))
                    .collect(Collectors.toList());
        }
        
        return enrollments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 특정 수강 신청 조회
    public EnrollmentResponseDto getEnrollmentById(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(new Enrollment.EnrollmentId(enrollmentId.intValue(), 0))
                .orElseThrow(() -> new IllegalArgumentException("수강 신청을 찾을 수 없습니다: " + enrollmentId));
        return toDto(enrollment);
    }

    // 수강 신청 취소
    @Transactional
    public void cancelEnrollment(Long enrollmentId) {
        // 실제로는 복합키를 사용하므로 다른 방식으로 조회해야 함
        // 임시로 모든 enrollment를 조회해서 ID로 찾는 방식 사용
        List<Enrollment> allEnrollments = enrollmentRepository.findAll();
        Enrollment enrollment = allEnrollments.stream()
                .filter(e -> e.getStudentId().equals(enrollmentId.intValue()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("수강 신청을 찾을 수 없습니다: " + enrollmentId));
        
        enrollmentRepository.delete(enrollment);
    }

    private void validateEnrollmentRequest(EnrollmentRequestDto requestDto) {
        if (requestDto.getStudentId() == null) {
            throw new IllegalArgumentException("학생 ID는 필수입니다.");
        }
        if (requestDto.getClassId() == null) {
            throw new IllegalArgumentException("강좌 ID는 필수입니다.");
        }
        if (requestDto.getStudentId() <= 0) {
            throw new IllegalArgumentException("학생 ID는 양수여야 합니다.");
        }
        if (requestDto.getClassId() <= 0) {
            throw new IllegalArgumentException("강좌 ID는 양수여야 합니다.");
        }
    }

    private EnrollmentResponseDto toDto(Enrollment enrollment) {
        return EnrollmentResponseDto.builder()
                .studentId(enrollment.getStudentId())
                .classId(enrollment.getClassId())
                .status(enrollment.getStatus())
                .createdAt(enrollment.getCreatedAt())
                .build();
    }
}
