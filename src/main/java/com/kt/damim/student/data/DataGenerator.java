package com.kt.damim.student.data;

import com.kt.damim.student.config.DataGenerationConfig;
import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Enrollment;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.entity.User;
import com.kt.damim.student.repository.AttendanceRepository;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.EnrollmentRepository;
import com.kt.damim.student.repository.SessionRepository;
import com.kt.damim.student.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataGenerator implements CommandLineRunner {
    
    private final DataGenerationConfig config;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    private final SessionRepository sessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    
    private final Random random = new Random();
    
    // 교사 이름 목록
    private static final String[] TEACHER_NAMES = {
        "김교수", "이교수", "박교수", "최교수", "정교수",
        "한교수", "윤교수", "임교수", "강교수", "조교수"
    };
    
    // 과목명 목록
    private static final String[] SUBJECTS = {
        "자바프로그래밍", "스프링부트", "데이터베이스", "웹개발", "알고리즘",
        "운영체제", "네트워크", "소프트웨어공학", "컴퓨터구조", "인공지능",
        "머신러닝", "딥러닝", "클라우드컴퓨팅", "모바일앱개발", "게임개발",
        "보안프로그래밍", "블록체인", "IoT프로그래밍", "빅데이터분석", "DevOps"
    };
    
    // 학기 목록
    private static final String[] SEMESTERS = {"2024-1", "2024-2", "2025-1"};
    
    // 요일 비트셋 (월화수목금토일)
    // 월(1), 화(2), 수(4), 목(8), 금(16), 토(32), 일(64)
    // 월화수=7, 목금=24, 월화=3, 수목=12, 금토=48, 토일=96, 월화수목금=31, 월화수목금토=63, 월화수목금토일=127
    private static final int[] DAY_PATTERNS = {7, 24, 3, 12, 48, 96, 31, 63, 127};
    
    // 시간대 패턴
    private static final LocalTime[][] TIME_PATTERNS = {
        {LocalTime.of(9, 0), LocalTime.of(10, 30)},
        {LocalTime.of(10, 0), LocalTime.of(11, 30)},
        {LocalTime.of(13, 0), LocalTime.of(14, 30)},
        {LocalTime.of(14, 0), LocalTime.of(15, 30)},
        {LocalTime.of(15, 0), LocalTime.of(16, 30)}
    };
    
    @Override
    public void run(String... args) throws Exception {
        if (!config.isEnabled()) {
            log.info("데이터 생성이 비활성화되어 있습니다. data.generation.enabled=true로 설정하세요.");
            return;
        }
        
        log.info("대량 테스트 데이터 생성을 시작합니다...");
        
        // 기존 데이터 확인
        if (userRepository.count() > 0) {
            log.info("기존 데이터가 존재합니다. 데이터 생성을 건너뜁니다.");
            return;
        }
        
        long startTime = System.currentTimeMillis();
        
        // 1. 교사 생성
        List<User> teachers = generateTeachers();
        log.info("{}명의 교사를 생성했습니다.", teachers.size());
        
        // 2. 학생 생성
        List<User> students = generateStudents();
        log.info("{}명의 학생을 생성했습니다.", students.size());
        
        // 3. 클래스 생성
        List<Class> classes = generateClasses(teachers);
        log.info("{}개의 클래스를 생성했습니다.", classes.size());
        
        // 4. 세션 생성
        List<Session> sessions = generateSessions(classes);
        log.info("{}개의 세션을 생성했습니다.", sessions.size());
        
        // 5. 수강 신청 생성
        List<Enrollment> enrollments = generateEnrollments(students, classes);
        log.info("{}개의 수강 신청을 생성했습니다.", enrollments.size());
        
        // 6. 출석 데이터 생성
        int attendanceCount = generateAttendances(students, sessions);
        log.info("{}개의 출석 데이터를 생성했습니다.", attendanceCount);
        
        long endTime = System.currentTimeMillis();
        log.info("데이터 생성이 완료되었습니다. 소요시간: {}ms", endTime - startTime);
        
        printStatistics(teachers.size(), students.size(), classes.size(), sessions.size(), enrollments.size(), attendanceCount);
    }
    
    private List<User> generateTeachers() {
        List<User> teachers = new ArrayList<>();
        
        for (int i = 0; i < config.getTeacherCount(); i++) {
            User teacher = User.builder()
                    .email("teacher" + (i + 1) + "@university.edu")
                    .passwordHash("hashed_password_teacher_" + (i + 1))
                    .role(User.UserRole.TEACHER)
                    .isActive(true)
                    .createdAt(OffsetDateTime.now())
                    .build();
            
            teachers.add(userRepository.save(teacher));
        }
        
        return teachers;
    }
    
    private List<User> generateStudents() {
        List<User> students = new ArrayList<>();
        
        for (int i = 0; i < config.getStudentCount(); i++) {
            User student = User.builder()
                    .email("student" + (i + 1) + "@university.edu")
                    .passwordHash("hashed_password_student_" + (i + 1))
                    .role(User.UserRole.STUDENT)
                    .isActive(true)
                    .createdAt(OffsetDateTime.now())
                    .build();
            
            students.add(userRepository.save(student));
        }
        
        return students;
    }
    
    private List<Class> generateClasses(List<User> teachers) {
        List<Class> classes = new ArrayList<>();
        int classIndex = 0;
        
        for (User teacher : teachers) {
            for (int i = 0; i < config.getClassPerTeacher(); i++) {
                String subject = SUBJECTS[classIndex % SUBJECTS.length];
                String semester = SEMESTERS[random.nextInt(SEMESTERS.length)];
                int dayPattern = DAY_PATTERNS[random.nextInt(DAY_PATTERNS.length)];
                LocalTime[] timePattern = TIME_PATTERNS[random.nextInt(TIME_PATTERNS.length)];
                
                Class classEntity = Class.builder()
                        .teacherId(teacher.getUserId())
                        .teacherName(TEACHER_NAMES[teachers.indexOf(teacher) % TEACHER_NAMES.length])
                        .className(subject)
                        .semester(semester)
                        .zoomUrl("https://zoom.us/j/" + (100000000 + random.nextInt(900000000)))
                        .heldDay(dayPattern)
                        .startsAt(timePattern[0])
                        .endsAt(timePattern[1])
                        .capacity(20 + random.nextInt(31)) // 20-50명
                        .createdAt(OffsetDateTime.now())
                        .build();
                
                classes.add(classRepository.save(classEntity));
                classIndex++;
            }
        }
        
        return classes;
    }
    
    private List<Session> generateSessions(List<Class> classes) {
        List<Session> sessions = new ArrayList<>();
        
        for (Class classEntity : classes) {
            for (int i = 0; i < config.getSessionPerClass(); i++) {
                // 과거 3개월에서 미래 1개월 사이의 랜덤 날짜
                OffsetDateTime sessionDate = OffsetDateTime.now()
                        .minusDays(90 + random.nextInt(120))
                        .withHour(9 + random.nextInt(8)) // 9시-17시
                        .withMinute(random.nextInt(4) * 15); // 0, 15, 30, 45분
                
                Session session = Session.builder()
                        .classId(classEntity.getClassId())
                        .onDate(sessionDate)
                        .build();
                
                sessions.add(sessionRepository.save(session));
            }
        }
        
        return sessions;
    }
    
    private List<Enrollment> generateEnrollments(List<User> students, List<Class> classes) {
        List<Enrollment> enrollments = new ArrayList<>();
        
        for (User student : students) {
            // 학생별로 랜덤하게 5개 클래스 선택
            List<Class> availableClasses = new ArrayList<>(classes);
            int enrollmentCount = Math.min(config.getEnrollmentPerStudent(), availableClasses.size());
            
            for (int i = 0; i < enrollmentCount; i++) {
                if (availableClasses.isEmpty()) break;
                
                int randomIndex = random.nextInt(availableClasses.size());
                Class selectedClass = availableClasses.remove(randomIndex);
                
                Enrollment enrollment = Enrollment.builder()
                        .studentId(student.getUserId())
                        .classId(selectedClass.getClassId())
                        .status("ENROLLED")
                        .createdAt(OffsetDateTime.now())
                        .build();
                
                enrollments.add(enrollmentRepository.save(enrollment));
            }
        }
        
        return enrollments;
    }
    
    private int generateAttendances(List<User> students, List<Session> sessions) {
        int attendanceCount = 0;
        
        for (User student : students) {
            // 학생이 수강하는 클래스의 세션들만 필터링
            List<Session> studentSessions = getStudentSessions(student, sessions);
            
            for (Session session : studentSessions) {
                // 출석률에 따라 출석 여부 결정
                if (random.nextDouble() < config.getAttendanceRate()) {
                    Attendance.AttendanceStatus status = getRandomAttendanceStatus();
                    
                    Attendance attendance = Attendance.builder()
                            .sessionId(session.getSessionId())
                            .studentId(student.getUserId())
                            .status(status)
                            .note(getRandomNote(status))
                            .createdAt(session.getOnDate().plusMinutes(random.nextInt(30)))
                            .build();
                    
                    attendanceRepository.save(attendance);
                    attendanceCount++;
                }
            }
        }
        
        return attendanceCount;
    }
    
    private List<Session> getStudentSessions(User student, List<Session> allSessions) {
        // 학생이 수강하는 클래스의 세션들만 반환
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getUserId());
        List<Integer> enrolledClassIds = enrollments.stream()
                .map(Enrollment::getClassId)
                .toList();
        
        return allSessions.stream()
                .filter(session -> enrolledClassIds.contains(session.getClassId()))
                .toList();
    }
    
    private Attendance.AttendanceStatus getRandomAttendanceStatus() {
        double rand = random.nextDouble();
        if (rand < 0.7) return Attendance.AttendanceStatus.PRESENT;
        if (rand < 0.85) return Attendance.AttendanceStatus.LATE;
        if (rand < 0.95) return Attendance.AttendanceStatus.EXCUSED;
        return Attendance.AttendanceStatus.ABSENT;
    }
    
    private String getRandomNote(Attendance.AttendanceStatus status) {
        switch (status) {
            case PRESENT:
                return "좋은 수업이었습니다";
            case LATE:
                return "지각했습니다";
            case EXCUSED:
                return "사유로 인한 결석";
            case ABSENT:
                return "무단 결석";
            default:
                return "";
        }
    }
    
    private void printStatistics(int teacherCount, int studentCount, int classCount, 
                                int sessionCount, int enrollmentCount, int attendanceCount) {
        log.info("=== 데이터 생성 통계 ===");
        log.info("교사: {}명", teacherCount);
        log.info("학생: {}명", studentCount);
        log.info("클래스: {}개", classCount);
        log.info("세션: {}개", sessionCount);
        log.info("수강신청: {}개", enrollmentCount);
        log.info("출석기록: {}개", attendanceCount);
        log.info("평균 출석률: {:.1f}%", (double) attendanceCount / (studentCount * sessionCount) * 100);
        log.info("=====================");
    }
}
