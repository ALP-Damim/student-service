package com.kt.damim.student.service;

import com.kt.damim.student.dto.AttendanceCreateRequestDto;
import com.kt.damim.student.dto.AttendanceCreateResponseDto;
import com.kt.damim.student.dto.AttendanceResultDto;
import com.kt.damim.student.dto.ClassAttendanceResponseDto;
import com.kt.damim.student.dto.SessionAttendanceResponseDto;
import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.entity.User;
import com.kt.damim.student.exception.AttendanceException;
import com.kt.damim.student.repository.AttendanceRepository;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.SessionRepository;
import com.kt.damim.student.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;
    
    public ClassAttendanceResponseDto getClassAttendance(Integer studentId, Integer classId) {
        // 학생과 클래스 존재 확인
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다: " + studentId));
        
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("클래스를 찾을 수 없습니다: " + classId));
        
        // 해당 클래스의 모든 세션 조회
        List<Session> sessions = sessionRepository.findByClassIdOrderByOnDateAsc(classId);
        
        // 학생의 출석 기록 조회
        List<Attendance> attendances = attendanceRepository.findByStudentIdAndClassId(studentId, classId);
        
        // 각 세션별 출석 결과 생성
        List<AttendanceResultDto> attendanceResults = sessions.stream()
                .map(session -> {
                    // 해당 세션의 출석 기록 찾기
                    Attendance attendance = attendances.stream()
                            .filter(a -> a.getSessionId().equals(session.getSessionId()))
                            .findFirst()
                            .orElse(null);
                    
                    if (attendance != null) {
                        return AttendanceResultDto.from(attendance, session);
                    } else {
                        // 출석 기록이 없는 경우 기본값으로 생성
                        return AttendanceResultDto.createDefault(
                                session.getSessionId(),
                                session.getOnDate()
                        );
                    }
                })
                .collect(Collectors.toList());
        
        ClassAttendanceResponseDto response = ClassAttendanceResponseDto.builder()
                .studentId(studentId)
                .studentName(student.getEmail()) // User 엔티티에는 name이 없으므로 email 사용
                .classId(classId)
                .className(classEntity.getClassName())
                .attendanceResults(attendanceResults)
                .averageScore(0.0)
                .totalSessions(0L)
                .attendedSessions(0L)
                .attendanceRate(0.0)
                .build();

        response.calculateStatistics();

        return response;
    }
    
    public SessionAttendanceResponseDto getSessionAttendance(Integer studentId, Integer sessionId) {
        // 학생과 세션 존재 확인
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다: " + studentId));
        
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("세션을 찾을 수 없습니다: " + sessionId));
        
        // 클래스 정보 조회
        Class classEntity = classRepository.findById(session.getClassId())
                .orElse(null);
        
        // 출석 기록 조회
        List<Attendance> attendances = attendanceRepository.findByStudentIdAndSessionId(studentId, sessionId);
        Attendance attendance = attendances.isEmpty() ? null : attendances.get(0);
        
        // 응답 DTO (빌더) 생성 - attendanceResult 포함하여 생성
        SessionAttendanceResponseDto response;
        
        if (attendance != null) {
            response = SessionAttendanceResponseDto.builder()
                    .studentId(studentId)
                    .studentName(student.getEmail()) // User 엔티티에는 name이 없으므로 email 사용
                    .sessionId(sessionId)
                    .sessionName(session.getSessionName())
                    .className(classEntity != null ? classEntity.getClassName() : "Unknown")
                    .attendanceResult(AttendanceResultDto.from(attendance, session))
                    .build();
        } else {
            // 출석 기록이 없는 경우 기본값으로 생성
            response = SessionAttendanceResponseDto.builder()
                    .studentId(studentId)
                    .studentName(student.getEmail()) // User 엔티티에는 name이 없으므로 email 사용
                    .sessionId(sessionId)
                    .sessionName(session.getSessionName())
                    .className(classEntity != null ? classEntity.getClassName() : "Unknown")
                    .attendanceResult(AttendanceResultDto.createDefault(
                            session.getSessionId(),
                            session.getOnDate()
                    ))
                    .build();
        }
        
        return response;
    }
    
    public AttendanceCreateResponseDto createAttendance(AttendanceCreateRequestDto request) {
        // 학생 존재 확인
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new AttendanceException("학생을 찾을 수 없습니다: " + request.getStudentId()));
        
        // 세션 존재 확인
        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new AttendanceException("세션을 찾을 수 없습니다: " + request.getSessionId()));
        
        // 이미 출석 기록이 있는지 확인
        Attendance.AttendanceId attendanceId = new Attendance.AttendanceId(request.getSessionId(), request.getStudentId());
        if (attendanceRepository.existsById(attendanceId)) {
            throw new AttendanceException("이미 출석 기록이 존재합니다: sessionId=" + request.getSessionId() + ", studentId=" + request.getStudentId());
        }
        
        // 출석 기록 생성
        Attendance attendance = Attendance.builder()
                .sessionId(request.getSessionId())
                .studentId(request.getStudentId())
                .status(request.getStatus())
                .note(request.getNote())
                .build();
        
        Attendance savedAttendance = attendanceRepository.save(attendance);
        
        return AttendanceCreateResponseDto.from(savedAttendance);
    }
}
