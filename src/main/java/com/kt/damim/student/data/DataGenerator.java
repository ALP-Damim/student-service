package com.kt.damim.student.data;

import com.kt.damim.student.config.DataGenerationConfig;
import com.kt.damim.student.entity.Attendance;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Enrollment;
import com.kt.damim.student.entity.Session;
import com.kt.damim.student.entity.User;
import com.kt.damim.student.entity.UserProfile;
import com.kt.damim.student.repository.AttendanceRepository;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.EnrollmentRepository;
import com.kt.damim.student.repository.SessionRepository;
import com.kt.damim.student.repository.UserRepository;
import com.kt.damim.student.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataGenerator implements CommandLineRunner {
    
    private final DataGenerationConfig config;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
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
    
    // 학생 이름 목록
    private static final String[] STUDENT_NAMES = {
        "김학생", "이학생", "박학생", "최학생", "정학생",
        "한학생", "윤학생", "임학생", "강학생", "조학생",
        "서학생", "신학생", "오학생", "유학생", "장학생",
        "전학생", "제학생", "하학생", "고학생", "문학생"
    };
    
    // 학교 목록
    private static final String[] SCHOOLS = {
        "서울대학교", "연세대학교", "고려대학교", "성균관대학교", "한양대학교",
        "중앙대학교", "경희대학교", "동국대학교", "홍익대학교", "건국대학교"
    };
    
    // 희망 과목 목록
    private static final String[] DESIRED_COURSES = {
        "자바프로그래밍", "웹개발", "모바일앱개발", "데이터분석", "인공지능",
        "게임개발", "보안", "클라우드컴퓨팅", "DevOps", "블록체인"
    };
    
    // 희망 직업 목록
    private static final String[] DESIRED_JOBS = {
        "소프트웨어 개발자", "웹 개발자", "모바일 앱 개발자", "데이터 분석가", "AI 엔지니어",
        "게임 개발자", "보안 전문가", "DevOps 엔지니어", "프로젝트 매니저", "IT 컨설턴트"
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
    // 1~3개 요일 조합만 사용
    private static final int[] DAY_PATTERNS = {
        1, 2, 4, 8, 16, 32, 64,  // 단일 요일
        3, 5, 6, 9, 10, 12, 17, 18, 20, 24, 33, 34, 36, 40, 48, 65, 66, 68, 72, 80, 96,  // 2개 요일
        7, 11, 13, 14, 19, 21, 22, 25, 26, 28, 35, 37, 38, 41, 42, 44, 49, 50, 52, 56, 67, 69, 70, 73, 74, 76, 81, 82, 84, 88, 97, 98, 100, 104, 112  // 3개 요일
    };
    
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
        
        // 1. 교사 생성 및 프로필 생성
        List<User> teachers = generateTeachers();
        generateTeacherProfiles(teachers);
        log.info("{}명의 교사와 프로필을 생성했습니다.", teachers.size());
        
        // 2. 학생 생성 및 프로필 생성
        List<User> students = generateStudents();
        generateStudentProfiles(students);
        log.info("{}명의 학생과 프로필을 생성했습니다.", students.size());
        
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
    
    private void generateTeacherProfiles(List<User> teachers) {
        for (int i = 0; i < teachers.size(); i++) {
            User teacher = teachers.get(i);
            String teacherName = TEACHER_NAMES[i % TEACHER_NAMES.length];
            
            UserProfile profile = UserProfile.builder()
                    .userId(teacher.getUserId())
                    .name(teacherName)
                    .desiredCourse(DESIRED_COURSES[random.nextInt(DESIRED_COURSES.length)])
                    .desiredJob(DESIRED_JOBS[random.nextInt(DESIRED_JOBS.length)])
                    .birthDate(generateRandomBirthDate(30, 60)) // 교사는 30-60세
                    .school(SCHOOLS[random.nextInt(SCHOOLS.length)])
                    .phone("010-" + String.format("%04d", random.nextInt(10000)) + "-" + String.format("%04d", random.nextInt(10000)))
                    .createdAt(OffsetDateTime.now())
                    .build();
            
            userProfileRepository.save(profile);
        }
    }
    
    private void generateStudentProfiles(List<User> students) {
        for (int i = 0; i < students.size(); i++) {
            User student = students.get(i);
            String studentName = STUDENT_NAMES[i % STUDENT_NAMES.length];
            
            UserProfile profile = UserProfile.builder()
                    .userId(student.getUserId())
                    .name(studentName)
                    .desiredCourse(DESIRED_COURSES[random.nextInt(DESIRED_COURSES.length)])
                    .desiredJob(DESIRED_JOBS[random.nextInt(DESIRED_JOBS.length)])
                    .birthDate(generateRandomBirthDate(18, 25)) // 학생은 18-25세
                    .school(SCHOOLS[random.nextInt(SCHOOLS.length)])
                    .phone("010-" + String.format("%04d", random.nextInt(10000)) + "-" + String.format("%04d", random.nextInt(10000)))
                    .createdAt(OffsetDateTime.now())
                    .build();
            
            userProfileRepository.save(profile);
        }
    }
    
    private LocalDate generateRandomBirthDate(int minAge, int maxAge) {
        int currentYear = OffsetDateTime.now().getYear();
        int birthYear = currentYear - minAge - random.nextInt(maxAge - minAge + 1);
        int birthMonth = 1 + random.nextInt(12);
        int birthDay = 1 + random.nextInt(28); // 간단히 28일로 제한
        
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }
    
    private List<Class> generateClasses(List<User> teachers) {
        List<Class> classes = new ArrayList<>();
        int classIndex = 0;
        
        for (User teacher : teachers) {
            List<Class> teacherClasses = new ArrayList<>(); // 교사별로 이미 생성된 강좌들
            
            for (int i = 0; i < config.getClassPerTeacher(); i++) {
                String subject = SUBJECTS[classIndex % SUBJECTS.length];
                String semester = SEMESTERS[random.nextInt(SEMESTERS.length)];
                
                // 시간 충돌이 없는 요일/시간 조합 찾기
                ClassSchedule schedule = findNonConflictingSchedule(teacherClasses);
                
                if (schedule == null) {
                    log.warn("교사 {}에게 시간 충돌이 없는 스케줄을 찾을 수 없습니다. 기본 스케줄을 사용합니다.", teacher.getUserId());
                    // 기본 스케줄 사용
                    int dayPattern = DAY_PATTERNS[random.nextInt(DAY_PATTERNS.length)];
                    LocalTime[] timePattern = TIME_PATTERNS[random.nextInt(TIME_PATTERNS.length)];
                    schedule = new ClassSchedule(dayPattern, timePattern[0], timePattern[1]);
                }
                
                Class classEntity = Class.builder()
                        .teacherId(teacher.getUserId())
                        .teacherName(TEACHER_NAMES[teachers.indexOf(teacher) % TEACHER_NAMES.length])
                        .className(subject)
                        .semester(semester)
                        .zoomUrl("https://zoom.us/j/" + (100000000 + random.nextInt(900000000)))
                        .heldDay(schedule.dayPattern)
                        .startsAt(schedule.startTime)
                        .endsAt(schedule.endTime)
                        .capacity(20 + random.nextInt(31)) // 20-50명
                        .createdAt(OffsetDateTime.now())
                        .build();
                
                Class savedClass = classRepository.save(classEntity);
                classes.add(savedClass);
                teacherClasses.add(savedClass);
                classIndex++;
            }
        }
        
        return classes;
    }
    
    /**
     * 시간 충돌이 없는 스케줄 찾기
     */
    private ClassSchedule findNonConflictingSchedule(List<Class> existingClasses) {
        List<ClassSchedule> availableSchedules = new ArrayList<>();
        
        // 모든 가능한 요일/시간 조합을 확인하여 충돌이 없는 스케줄들 수집
        for (int dayPattern : DAY_PATTERNS) {
            for (LocalTime[] timePattern : TIME_PATTERNS) {
                ClassSchedule candidate = new ClassSchedule(dayPattern, timePattern[0], timePattern[1]);
                
                boolean hasConflict = false;
                for (Class existingClass : existingClasses) {
                    if (hasTimeOverlap(candidate.dayPattern, candidate.startTime, candidate.endTime,
                                     existingClass.getHeldDay(), existingClass.getStartsAt(), existingClass.getEndsAt())) {
                        hasConflict = true;
                        break;
                    }
                }
                
                if (!hasConflict) {
                    availableSchedules.add(candidate);
                }
            }
        }
        
        // 충돌이 없는 스케줄이 있으면 랜덤하게 선택
        if (!availableSchedules.isEmpty()) {
            int randomIndex = random.nextInt(availableSchedules.size());
            return availableSchedules.get(randomIndex);
        }
        
        return null; // 충돌이 없는 스케줄을 찾을 수 없음
    }
    
    /**
     * 두 스케줄 간의 시간 충돌 여부 확인 (오버로드된 메서드)
     */
    private boolean hasTimeOverlap(int day1, LocalTime start1, LocalTime end1, 
                                 int day2, LocalTime start2, LocalTime end2) {
        // 요일이 겹치는지 확인
        if ((day1 & day2) == 0) {
            return false; // 요일이 겹치지 않으면 충돌 없음
        }
        
        // 시간이 겹치는지 확인
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    /**
     * 강좌 스케줄을 담는 내부 클래스
     */
    private static class ClassSchedule {
        final int dayPattern;
        final LocalTime startTime;
        final LocalTime endTime;
        
        ClassSchedule(int dayPattern, LocalTime startTime, LocalTime endTime) {
            this.dayPattern = dayPattern;
            this.startTime = startTime;
            this.endTime = endTime;
        }
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
                        .sessionName("session" + (i + 1))
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
            List<Class> enrolledClasses = new ArrayList<>(); // 이미 신청한 강좌들
            int enrollmentCount = Math.min(config.getEnrollmentPerStudent(), availableClasses.size());
            
            for (int i = 0; i < enrollmentCount; i++) {
                if (availableClasses.isEmpty()) break;
                
                // 시간 충돌이 없는 강좌들만 필터링
                List<Class> nonConflictingClasses = filterNonConflictingClasses(availableClasses, enrolledClasses);
                
                if (nonConflictingClasses.isEmpty()) {
                    // 충돌이 없는 강좌가 없으면 기존 방식으로 진행
                    log.warn("학생 {}에게 시간 충돌이 없는 강좌가 없습니다. 기존 방식으로 진행합니다.", student.getUserId());
                    break;
                }
                
                int randomIndex = random.nextInt(nonConflictingClasses.size());
                Class selectedClass = nonConflictingClasses.get(randomIndex);
                
                // 선택된 강좌를 availableClasses에서 제거
                availableClasses.remove(selectedClass);
                enrolledClasses.add(selectedClass);
                
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
    
    /**
     * 시간 충돌이 없는 강좌들만 필터링
     */
    private List<Class> filterNonConflictingClasses(List<Class> availableClasses, List<Class> enrolledClasses) {
        return availableClasses.stream()
                .filter(availableClass -> !hasTimeConflict(availableClass, enrolledClasses))
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 강좌가 이미 신청한 강좌들과 시간 충돌이 있는지 확인
     */
    private boolean hasTimeConflict(Class newClass, List<Class> enrolledClasses) {
        for (Class enrolledClass : enrolledClasses) {
            if (hasTimeOverlap(newClass, enrolledClass)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 두 강좌 간의 시간 충돌 여부 확인
     */
    private boolean hasTimeOverlap(Class class1, Class class2) {
        // 요일이 겹치는지 확인
        if ((class1.getHeldDay() & class2.getHeldDay()) == 0) {
            return false; // 요일이 겹치지 않으면 충돌 없음
        }
        
        // 시간이 겹치는지 확인
        LocalTime start1 = class1.getStartsAt();
        LocalTime end1 = class1.getEndsAt();
        LocalTime start2 = class2.getStartsAt();
        LocalTime end2 = class2.getEndsAt();
        
        // 시간 겹침 확인: (start1 < end2) && (start2 < end1)
        return start1.isBefore(end2) && start2.isBefore(end1);
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
