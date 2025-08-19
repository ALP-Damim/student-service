package com.kt.damim.student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.entity.User;
import com.kt.damim.student.repository.AttendanceRepository;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.SessionRepository;
import com.kt.damim.student.repository.UserRepository;
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

import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AttendanceControllerTest {

//     @Autowired
//     private WebApplicationContext webApplicationContext;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private ClassRepository classRepository;

//     @Autowired
//     private SessionRepository sessionRepository;

//     @Autowired
//     private AttendanceRepository attendanceRepository;

//     private MockMvc mockMvc;
//     private ObjectMapper objectMapper;

//     private User testStudent;
//     private Class testClass;
//     private Session testSession;
//     private Attendance testAttendance;

//     @BeforeEach
//     void setUp() {
//         mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//         objectMapper = new ObjectMapper();

//         // 테스트 데이터 생성
//         testStudent = User.builder()
//                 .email("test@example.com")
//                 .passwordHash("hashed_password")
//                 .role(User.UserRole.STUDENT)
//                 .isActive(true)
//                 .build();
//         testStudent = userRepository.save(testStudent);

//         testClass = Class.builder()
//                 .teacherId(999)
//                 .teacherName("테스트 교수")
//                 .semester("2024-1")
//                 .zoomUrl("https://zoom.us/j/test")
//                 .startsAt(OffsetDateTime.now().minusDays(30))
//                 .endsAt(OffsetDateTime.now().plusDays(90))
//                 .capacity(30)
//                 .build();
//         testClass = classRepository.save(testClass);

//         testSession = Session.builder()
//                 .classId(testClass.getClassId())
//                 .onDate(OffsetDateTime.now().minusDays(1))
//                 .build();
//         testSession = sessionRepository.save(testSession);

//         testAttendance = Attendance.builder()
//                 .sessionId(testSession.getSessionId())
//                 .studentId(testStudent.getUserId())
//                 .status(Attendance.AttendanceStatus.PRESENT)
//                 .note("테스트 출석")
//                 .build();
//         testAttendance = attendanceRepository.save(testAttendance);
//     }

//     @Test
//     void testGetClassAttendance() throws Exception {
//         mockMvc.perform(get("/api/attendance/class/{studentId}/{classId}", 
//                 testStudent.getUserId(), testClass.getClassId())
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.studentId").value(testStudent.getUserId()))
//                 .andExpect(jsonPath("$.studentName").value("test@example.com"))
//                 .andExpect(jsonPath("$.classId").value(testClass.getClassId()))
//                 .andExpect(jsonPath("$.className").value("2024-1"))
//                 .andExpect(jsonPath("$.attendanceResults").isArray())
//                 .andExpect(jsonPath("$.attendanceResults[0].sessionId").value(testSession.getSessionId()))
//                 .andExpect(jsonPath("$.attendanceResults[0].status").value("PRESENT"));
//     }

//     @Test
//     void testGetSessionAttendance() throws Exception {
//         mockMvc.perform(get("/api/attendance/session/{studentId}/{sessionId}", 
//                 testStudent.getUserId(), testSession.getSessionId())
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.studentId").value(testStudent.getUserId()))
//                 .andExpect(jsonPath("$.studentName").value("test@example.com"))
//                 .andExpect(jsonPath("$.sessionId").value(testSession.getSessionId()))
//                 .andExpect(jsonPath("$.sessionTitle").value("Session"))
//                 .andExpect(jsonPath("$.className").value("2024-1"))
//                 .andExpect(jsonPath("$.attendanceResult.sessionId").value(testSession.getSessionId()))
//                 .andExpect(jsonPath("$.attendanceResult.status").value("PRESENT"));
//     }

//     @Test
//     void testGetClassAttendanceWithNonExistentStudent() throws Exception {
//         Integer nonExistentStudentId = 999;
//         mockMvc.perform(get("/api/attendance/class/{studentId}/{classId}", 
//                 nonExistentStudentId, testClass.getClassId())
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(jsonPath("$.message").value("학생을 찾을 수 없습니다: " + nonExistentStudentId));
//     }

//     @Test
//     void testGetClassAttendanceWithNonExistentClass() throws Exception {
//         Integer nonExistentClassId = 999;
//         mockMvc.perform(get("/api/attendance/class/{studentId}/{classId}", 
//                 testStudent.getUserId(), nonExistentClassId)
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(jsonPath("$.message").value("클래스를 찾을 수 없습니다: " + nonExistentClassId));
//     }

//     @Test
//     void testGetSessionAttendanceWithNonExistentSession() throws Exception {
//         Integer nonExistentSessionId = 999;
//         mockMvc.perform(get("/api/attendance/session/{studentId}/{sessionId}", 
//                 testStudent.getUserId(), nonExistentSessionId)
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(jsonPath("$.message").value("세션을 찾을 수 없습니다: " + nonExistentSessionId));
//     }
}
