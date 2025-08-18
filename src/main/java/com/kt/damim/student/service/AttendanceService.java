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
        
        // 응답 DTO 생성
        ClassAttendanceResponseDto response = new ClassAttendanceResponseDto();
        response.setStudentId(studentId);
        response.setStudentName(student.getName());
        response.setClassId(classId);
        response.setClassName(classEntity.getName());
        
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
                        AttendanceResultDto dto = new AttendanceResultDto();
                        dto.setSessionId(session.getId());
                        dto.setSessionTitle(session.getTitle());
                        dto.setSessionStartTime(session.getStartTime());
                        dto.setSessionEndTime(session.getEndTime());
                        dto.setStatus(Attendance.AttendanceStatus.ABSENT);
                        dto.setScore(0);
                        dto.setComments("출석 기록 없음");
                        return dto;
                    }
                })
                .collect(Collectors.toList());
        
        response.setAttendanceResults(attendanceResults);
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
        
        // 응답 DTO 생성
        SessionAttendanceResponseDto response = new SessionAttendanceResponseDto();
        response.setStudentId(studentId);
        response.setStudentName(student.getName());
        response.setSessionId(sessionId);
        response.setSessionTitle(session.getTitle());
        response.setClassName(session.getClassEntity().getName());
        
        if (attendance != null) {
            response.setAttendanceResult(AttendanceResultDto.from(attendance));
        } else {
            // 출석 기록이 없는 경우 기본값으로 생성
            AttendanceResultDto dto = new AttendanceResultDto();
            dto.setSessionId(session.getId());
            dto.setSessionTitle(session.getTitle());
            dto.setSessionStartTime(session.getStartTime());
            dto.setSessionEndTime(session.getEndTime());
            dto.setStatus(Attendance.AttendanceStatus.ABSENT);
            dto.setScore(0);
            dto.setComments("출석 기록 없음");
            response.setAttendanceResult(dto);
        }
        
        return response;
    }
}
