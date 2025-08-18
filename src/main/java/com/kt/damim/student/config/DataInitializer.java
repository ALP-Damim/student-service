package com.kt.damim.student.config;

import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.entity.Student;
import com.kt.damim.student.repository.AttendanceRepository;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.SessionRepository;
import com.kt.damim.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {
    
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 테스트 데이터가 이미 있는지 확인
        if (studentRepository.count() > 0) {
            return;
        }
        
        // 학생 데이터 생성
        Student student1 = new Student();
        student1.setName("김철수");
        student1.setEmail("kim@example.com");
        student1.setStudentNumber("2024001");
        studentRepository.save(student1);
        
        Student student2 = new Student();
        student2.setName("이영희");
        student2.setEmail("lee@example.com");
        student2.setStudentNumber("2024002");
        studentRepository.save(student2);
        
        // 클래스 데이터 생성
        Class class1 = new Class();
        class1.setName("자바 프로그래밍");
        class1.setDescription("자바 기초부터 고급까지");
        classRepository.save(class1);
        
        Class class2 = new Class();
        class2.setName("스프링 부트");
        class2.setDescription("스프링 부트 웹 개발");
        classRepository.save(class2);
        
        // 세션 데이터 생성
        Session session1 = new Session();
        session1.setTitle("자바 기초 1강");
        session1.setDescription("변수와 데이터 타입");
        session1.setStartTime(LocalDateTime.now().minusDays(7));
        session1.setEndTime(LocalDateTime.now().minusDays(7).plusHours(2));
        session1.setClassEntity(class1);
        sessionRepository.save(session1);
        
        Session session2 = new Session();
        session2.setTitle("자바 기초 2강");
        session2.setDescription("제어문과 반복문");
        session2.setStartTime(LocalDateTime.now().minusDays(5));
        session2.setEndTime(LocalDateTime.now().minusDays(5).plusHours(2));
        session2.setClassEntity(class1);
        sessionRepository.save(session2);
        
        Session session3 = new Session();
        session3.setTitle("스프링 부트 1강");
        session3.setDescription("스프링 부트 소개");
        session3.setStartTime(LocalDateTime.now().minusDays(3));
        session3.setEndTime(LocalDateTime.now().minusDays(3).plusHours(2));
        session3.setClassEntity(class2);
        sessionRepository.save(session3);
        
        // 출석 데이터 생성
        Attendance attendance1 = new Attendance();
        attendance1.setStudent(student1);
        attendance1.setSession(session1);
        attendance1.setStatus(Attendance.AttendanceStatus.PRESENT);
        attendance1.setScore(85);
        attendance1.setComments("좋은 수업이었습니다");
        attendance1.setRecordedAt(LocalDateTime.now().minusDays(7));
        attendanceRepository.save(attendance1);
        
        Attendance attendance2 = new Attendance();
        attendance2.setStudent(student1);
        attendance2.setSession(session2);
        attendance2.setStatus(Attendance.AttendanceStatus.LATE);
        attendance2.setScore(70);
        attendance2.setComments("지각했습니다");
        attendance2.setRecordedAt(LocalDateTime.now().minusDays(5));
        attendanceRepository.save(attendance2);
        
        Attendance attendance3 = new Attendance();
        attendance3.setStudent(student1);
        attendance3.setSession(session3);
        attendance3.setStatus(Attendance.AttendanceStatus.PRESENT);
        attendance3.setScore(90);
        attendance3.setComments("훌륭한 수업");
        attendance3.setRecordedAt(LocalDateTime.now().minusDays(3));
        attendanceRepository.save(attendance3);
        
        Attendance attendance4 = new Attendance();
        attendance4.setStudent(student2);
        attendance4.setSession(session1);
        attendance4.setStatus(Attendance.AttendanceStatus.PRESENT);
        attendance4.setScore(95);
        attendance4.setComments("매우 만족");
        attendance4.setRecordedAt(LocalDateTime.now().minusDays(7));
        attendanceRepository.save(attendance4);
        
        System.out.println("테스트 데이터가 성공적으로 생성되었습니다.");
    }
}
