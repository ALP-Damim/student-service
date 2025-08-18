package com.kt.damim.student.service;

import com.kt.damim.student.dto.AttendanceResultDto;
import com.kt.damim.student.dto.ClassAttendanceResponseDto;
import com.kt.damim.student.dto.SessionAttendanceResponseDto;
import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.entity.Student;
import com.kt.damim.student.repository.AttendanceRepository;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.SessionRepository;
import com.kt.damim.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {
    
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;
    
    public ClassAttendanceResponseDto getClassAttendance(Long studentId, Long classId) {
        // 학생과 클래스 존재 확인
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다: " + studentId));
        
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("클래스를 찾을 수 없습니다: " + classId));
        
        // 해당 클래스의 모든 세션 조회
        List<Session> sessions = sessionRepository.findByClassId(classId);
        
        // 학생의 출석 기록 조회
        List<Attendance> attendances = attendanceRepository.findByStudentIdAndClassId(studentId, classId);
        
        // 응답 DTO는 attendanceResults 계산 후 생성
        
        // 각 세션별 출석 결과 생성
        List<AttendanceResultDto> attendanceResults = sessions.stream()
                .map(session -> {
                    // 해당 세션의 출석 기록 찾기
                    Attendance attendance = attendances.stream()
                            .filter(a -> a.getSession().getId().equals(session.getId()))
                            .findFirst()
                            .orElse(null);
                    
                    if (attendance != null) {
                        return AttendanceResultDto.from(attendance);
                    } else {
                        // 출석 기록이 없는 경우 기본값으로 생성
                        return AttendanceResultDto.createDefault(
                                session.getId(),
                                session.getTitle(),
                                session.getStartTime(),
                                session.getEndTime()
                        );
                    }
                })
                .collect(Collectors.toList());
        
        ClassAttendanceResponseDto response = ClassAttendanceResponseDto.builder()
                .studentId(studentId)
                .studentName(student.getName())
                .classId(classId)
                .className(classEntity.getName())
                .attendanceResults(attendanceResults)
                .build();

        response.calculateStatistics();

        return response;
    }
    
    public SessionAttendanceResponseDto getSessionAttendance(Long studentId, Long sessionId) {
        // 학생과 세션 존재 확인
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다: " + studentId));
        
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("세션을 찾을 수 없습니다: " + sessionId));
        
        // 출석 기록 조회
        Attendance attendance = attendanceRepository.findByStudentIdAndSessionId(studentId, sessionId)
                .orElse(null);
        
        // 응답 DTO (빌더) 생성 - attendanceResult 포함하여 생성
        SessionAttendanceResponseDto response;
        
        if (attendance != null) {
            response = SessionAttendanceResponseDto.builder()
                    .studentId(studentId)
                    .studentName(student.getName())
                    .sessionId(sessionId)
                    .sessionTitle(session.getTitle())
                    .className(session.getClassEntity().getName())
                    .attendanceResult(AttendanceResultDto.from(attendance))
                    .build();
        } else {
            // 출석 기록이 없는 경우 기본값으로 생성
            response = SessionAttendanceResponseDto.builder()
                    .studentId(studentId)
                    .studentName(student.getName())
                    .sessionId(sessionId)
                    .sessionTitle(session.getTitle())
                    .className(session.getClassEntity().getName())
                    .attendanceResult(AttendanceResultDto.createDefault(
                            session.getId(),
                            session.getTitle(),
                            session.getStartTime(),
                            session.getEndTime()
                    ))
                    .build();
        }
        
        return response;
    }
}
