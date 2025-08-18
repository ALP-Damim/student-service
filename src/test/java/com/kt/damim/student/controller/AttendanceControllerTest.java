package com.kt.damim.student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.entity.Student;
import com.kt.damim.student.repository.AttendanceRepository;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.SessionRepository;
import com.kt.damim.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AttendanceControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Student testStudent;
    private Class testClass;
    private Session testSession;
    private Attendance testAttendance;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        // 테스트 데이터 생성
        testStudent = new Student();
        testStudent.setName("테스트 학생");
        testStudent.setEmail("test@example.com");
        testStudent.setStudentNumber("2024001");
        testStudent = studentRepository.save(testStudent);

        testClass = new Class();
        testClass.setName("테스트 클래스");
        testClass.setDescription("테스트용 클래스");
        testClass = classRepository.save(testClass);

        testSession = new Session();
        testSession.setTitle("테스트 세션");
        testSession.setDescription("테스트용 세션");
        testSession.setStartTime(LocalDateTime.now().minusDays(1));
        testSession.setEndTime(LocalDateTime.now().minusDays(1).plusHours(2));
        testSession.setClassEntity(testClass);
        testSession = sessionRepository.save(testSession);

        testAttendance = new Attendance();
        testAttendance.setStudent(testStudent);
        testAttendance.setSession(testSession);
        testAttendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        testAttendance.setScore(85);
        testAttendance.setComments("테스트 출석");
        testAttendance.setRecordedAt(LocalDateTime.now().minusDays(1));
        testAttendance = attendanceRepository.save(testAttendance);
    }

    @Test
    void testGetClassAttendance() throws Exception {
        mockMvc.perform(get("/api/attendance/class/{studentId}/{classId}", 
                testStudent.getId(), testClass.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(testStudent.getId()))
                .andExpect(jsonPath("$.studentName").value("테스트 학생"))
                .andExpect(jsonPath("$.classId").value(testClass.getId()))
                .andExpect(jsonPath("$.className").value("테스트 클래스"))
                .andExpect(jsonPath("$.attendanceResults").isArray())
                .andExpect(jsonPath("$.attendanceResults[0].sessionId").value(testSession.getId()))
                .andExpect(jsonPath("$.attendanceResults[0].status").value("PRESENT"))
                .andExpect(jsonPath("$.attendanceResults[0].score").value(85));
    }

    @Test
    void testGetSessionAttendance() throws Exception {
        mockMvc.perform(get("/api/attendance/session/{studentId}/{sessionId}", 
                testStudent.getId(), testSession.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(testStudent.getId()))
                .andExpect(jsonPath("$.studentName").value("테스트 학생"))
                .andExpect(jsonPath("$.sessionId").value(testSession.getId()))
                .andExpect(jsonPath("$.sessionTitle").value("테스트 세션"))
                .andExpect(jsonPath("$.className").value("테스트 클래스"))
                .andExpect(jsonPath("$.attendanceResult.sessionId").value(testSession.getId()))
                .andExpect(jsonPath("$.attendanceResult.status").value("PRESENT"))
                .andExpect(jsonPath("$.attendanceResult.score").value(85));
    }

    @Test
    void testGetClassAttendanceWithNonExistentStudent() throws Exception {
        mockMvc.perform(get("/api/attendance/class/{studentId}/{classId}", 
                999L, testClass.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("학생을 찾을 수 없습니다: 999"));
    }

    @Test
    void testGetClassAttendanceWithNonExistentClass() throws Exception {
        mockMvc.perform(get("/api/attendance/class/{studentId}/{classId}", 
                testStudent.getId(), 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("클래스를 찾을 수 없습니다: 999"));
    }

    @Test
    void testGetSessionAttendanceWithNonExistentSession() throws Exception {
        mockMvc.perform(get("/api/attendance/session/{studentId}/{sessionId}", 
                testStudent.getId(), 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("세션을 찾을 수 없습니다: 999"));
    }
}
