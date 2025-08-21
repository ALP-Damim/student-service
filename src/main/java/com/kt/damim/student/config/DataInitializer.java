package com.kt.damim.student.config;

import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.entity.User;
import com.kt.damim.student.repository.AttendanceRepository;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.SessionRepository;
import com.kt.damim.student.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
@Profile("!test")
@ConditionalOnProperty(name = "data.generation.enabled", havingValue = "false", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {
	
	private final UserRepository userRepository;
	private final ClassRepository classRepository;
	private final SessionRepository sessionRepository;
	private final AttendanceRepository attendanceRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void run(String... args) throws Exception {
		// 테스트 데이터가 이미 있는지 확인
		if (userRepository.count() > 0) {
			return;
		}
		
		// 사용자 데이터 생성 (학생)
		User student1 = User.builder()
				.email("kim@example.com")
				.passwordHash("hashed_password_1")
				.role(User.UserRole.STUDENT)
				.isActive(true)
				.createdAt(OffsetDateTime.now())
				.build();
		student1 = userRepository.save(student1);
		
		User student2 = User.builder()
				.email("lee@example.com")
				.passwordHash("hashed_password_2")
				.role(User.UserRole.STUDENT)
				.isActive(true)
				.createdAt(OffsetDateTime.now())
				.build();
		student2 = userRepository.save(student2);
		
		// 사용자 데이터 생성 (교사)
		User teacher1 = User.builder()
				.email("teacher@example.com")
				.passwordHash("hashed_password_teacher")
				.role(User.UserRole.TEACHER)
				.isActive(true)
				.createdAt(OffsetDateTime.now())
				.build();
		teacher1 = userRepository.save(teacher1);
		
		// 클래스 데이터 생성
		Class class1 = Class.builder()
				.teacherId(teacher1.getUserId())
				.teacherName("김교수")
				.className("자바프로그래밍")
				.semester("2024-1")
				.zoomUrl("https://zoom.us/j/123456789")
				.heldDay(7) // 월화수 (1+2+4)
				.startsAt(LocalTime.of(10, 0)) // 10:00
				.endsAt(LocalTime.of(12, 0)) // 12:00
				.capacity(30)
				.createdAt(OffsetDateTime.now())
				.build();
		class1 = classRepository.save(class1);
		
		Class class2 = Class.builder()
				.teacherId(teacher1.getUserId())
				.teacherName("김교수")
				.className("데이터베이스")
				.semester("2024-1")
				.zoomUrl("https://zoom.us/j/987654321")
				.heldDay(24) // 목금 (8+16)
				.startsAt(LocalTime.of(14, 0)) // 14:00
				.endsAt(LocalTime.of(16, 0)) // 16:00
				.capacity(25)
				.createdAt(OffsetDateTime.now())
				.build();
		class2 = classRepository.save(class2);
		
		// 세션 데이터 생성
		Session session1 = Session.builder()
				.classId(class1.getClassId())
				.sessionName("session1")
				.onDate(OffsetDateTime.now().minusDays(7))
				.build();
		session1 = sessionRepository.save(session1);
		
		Session session2 = Session.builder()
				.classId(class1.getClassId())
				.sessionName("session2")
				.onDate(OffsetDateTime.now().minusDays(5))
				.build();
		session2 = sessionRepository.save(session2);
		
		Session session3 = Session.builder()
				.classId(class2.getClassId())
				.sessionName("session3")
				.onDate(OffsetDateTime.now().minusDays(3))
				.build();
		session3 = sessionRepository.save(session3);
		
		// 출석 데이터 생성
		Attendance attendance1 = Attendance.builder()
				.sessionId(session1.getSessionId())
				.studentId(student1.getUserId())
				.status(Attendance.AttendanceStatus.PRESENT)
				.note("좋은 수업이었습니다")
				.createdAt(OffsetDateTime.now())
				.build();
		attendanceRepository.save(attendance1);
		
		Attendance attendance2 = Attendance.builder()
				.sessionId(session2.getSessionId())
				.studentId(student1.getUserId())
				.status(Attendance.AttendanceStatus.LATE)
				.note("지각했습니다")
				.createdAt(OffsetDateTime.now())
				.build();
		attendanceRepository.save(attendance2);
		
		Attendance attendance3 = Attendance.builder()
				.sessionId(session3.getSessionId())
				.studentId(student1.getUserId())
				.status(Attendance.AttendanceStatus.PRESENT)
				.note("훌륭한 수업")
				.createdAt(OffsetDateTime.now())
				.build();
		attendanceRepository.save(attendance3);
		
		Attendance attendance4 = Attendance.builder()
				.sessionId(session1.getSessionId())
				.studentId(student2.getUserId())
				.status(Attendance.AttendanceStatus.PRESENT)
				.note("매우 만족")
				.createdAt(OffsetDateTime.now())
				.build();
		attendanceRepository.save(attendance4);
		
		System.out.println("테스트 데이터가 성공적으로 생성되었습니다.");
		System.out.println("학생1: " + student1.getUserId());
		System.out.println("학생2: " + student2.getUserId());
		System.out.println("교사1: " + teacher1.getUserId());
		System.out.println("클래스1: " + class1.getClassId() + " (요일: " + class1.getHeldDaysString() + ", 시간: " + class1.getStartsAt() + "-" + class1.getEndsAt() + ")");
		System.out.println("클래스2: " + class2.getClassId() + " (요일: " + class2.getHeldDaysString() + ", 시간: " + class2.getStartsAt() + "-" + class2.getEndsAt() + ")");
		System.out.println("세션1: " + session1.getSessionId());
		System.out.println("세션2: " + session2.getSessionId());
		System.out.println("세션3: " + session3.getSessionId());
	}
}
