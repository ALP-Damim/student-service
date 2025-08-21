# 학생 출석 통계 서비스

학생의 학습 현황 및 출석 통계를 제공하는 Spring Boot 기반 REST API 서비스입니다.

## 🚀 기능

- **강좌 관리**: 강좌 등록, 조회, 필터링
- **출석 관리**: 클래스별/세션별 출석 통계 조회
- **사용자 관리**: 사용자 정보 및 프로필 관리
- **수강 신청**: 강좌 신청 및 수강 신청 목록 조회
- **세션 관리**: 강좌별 세션 목록 조회

## 🛠 기술 스택

- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Java 17**

## 📋 데이터베이스 스키마

### 테이블 구조

1. **users**: 사용자 정보 (학생/교사)
   - user_id (PK, Auto Increment)
   - email
   - password_hash
   - role (STUDENT, TEACHER)
   - is_active
   - created_at
   - updated_at

2. **classes**: 강좌 정보
   - class_id (PK, Auto Increment)
   - teacher_id (FK to users)
   - teacher_name (TEXT, NOT NULL)
   - className (TEXT, NOT NULL)
   - semester (TEXT, NOT NULL)
   - school_year (TEXT) - 학년도 (예: 2024, 2025)
   - subject (TEXT) - 과목명 (예: 자바프로그래밍, 데이터베이스)
   - zoom_url (TEXT)
   - held_day (INTEGER) - 비트셋으로 요일 표시 (월:1, 화:2, 수:4, 목:8, 금:16, 토:32, 일:64)
   - starts_at (TIME) - HH:MM 형식
   - ends_at (TIME) - HH:MM 형식
   - capacity (INTEGER)
   - created_at (TIMESTAMPTZ, NOT NULL)
   - updated_at (TIMESTAMPTZ, NOT NULL)

3. **sessions**: 강좌별 세션 정보
   - session_id (PK, Auto Increment)
   - class_id (FK to classes)
   - on_date

4. **enrollments**: 수강 신청 정보
   - student_id (PK, FK to users)
   - class_id (PK, FK to classes)
   - status
   - created_at

5. **attendance**: 출석 정보
   - session_id (PK, FK to sessions)
   - student_id (PK, FK to users)
   - status (PRESENT, ABSENT, LATE, EXCUSED)
   - note
   - created_at

## 🔧 설정

### 1. 환경변수 설정

프로젝트는 환경변수를 사용하여 설정을 관리합니다. 다음 스크립트를 실행하여 환경변수를 설정하세요:

**Windows:**
```bash
set-env.bat
```

**Linux/Mac:**
```bash
source set-env.sh
```

### 2. 데이터베이스 설정

**PostgreSQL 데이터베이스:**
```bash
docker-compose -f docker-compose.localdb.yml up -d
```

**애플리케이션 실행:**
```bash
./gradlew bootRun
```

## 📚 API 사용법

### 1. 강좌 관리 API

#### 1.1 강좌 등록

**엔드포인트**: `POST /api/classes`

**설명**: 새로운 강좌를 등록합니다. 강좌 생성 시 자동으로 앞으로 10개의 수업 세션이 생성됩니다.

**요청 본문**:
```json
{
  "teacherId": 1,
  "teacherName": "김교수",
  "className": "자바 프로그래밍",
  "semester": "2024-2",
  "schoolYear": "2024",
  "subject": "자바프로그래밍",
  "zoomUrl": "https://zoom.us/j/123456789",
  "heldDay": 7,
  "startsAt": "10:00:00",
  "endsAt": "12:00:00",
  "capacity": 30
}
```

**필수 필드**:
- `teacherId`: 교사 ID (Integer)
- `teacherName`: 교사 이름 (String)
- `className`: 강좌명 (String)
- `semester`: 학기 (String)
- `heldDay`: 요일 비트마스크 (Integer, 1~127)
- `startsAt`: 시작 시간 (HH:MM:SS 형식)
- `endsAt`: 종료 시간 (HH:MM:SS 형식)

**선택 필드**:
- `schoolYear`: 학년도 (String, 예: "2024", "2025")
- `subject`: 과목명 (String, 예: "자바프로그래밍", "데이터베이스")
- `zoomUrl`: 줌 URL (String)
- `capacity`: 수용 인원 (Integer)

**응답 예시**:
```json
{
  "classId": 1,
  "teacherId": 1,
  "teacherName": "김교수",
  "className": "자바 프로그래밍",
  "semester": "2024-2",
  "schoolYear": "2024",
  "subject": "자바프로그래밍",
  "zoomUrl": "https://zoom.us/j/123456789",
  "heldDay": 7,
  "heldDaysString": "월, 화, 수",
  "startsAt": "10:00:00",
  "endsAt": "12:00:00",
  "capacity": 30,
  "generatedSessions": [
    {
      "sessionId": 1,
      "sessionName": "세션 1",
      "onDate": "2024-12-02T10:00:00+09:00"
    },
    {
      "sessionId": 2,
      "sessionName": "세션 2",
      "onDate": "2024-12-03T10:00:00+09:00"
    }
  ]
}
```

**자동 세션 생성 규칙**:
- 강좌 생성 시 자동으로 앞으로 10개의 수업 세션이 생성됩니다
- 요일 비트마스크를 기반으로 해당 요일들에만 세션을 생성합니다
- 현재 날짜부터 시작하여 해당 요일을 만날 때마다 세션을 생성합니다
- 세션 이름은 "세션 1", "세션 2" 형태로 자동 생성됩니다
- 세션 시간은 강좌의 시작 시간을 기준으로 설정됩니다

**요일 비트마스크 설명**:
- 월: 1, 화: 2, 수: 4, 목: 8, 금: 16, 토: 32, 일: 64
- 예: 월화수 = 1 + 2 + 4 = 7
- 예: 목금 = 8 + 16 = 24

#### 1.2 강좌 목록 조회

**엔드포인트**: `GET /api/classes`

**설명**: 모든 강좌를 조회합니다. 쿼리 파라미터로 개수 제한, 학기 정렬, 요일 비트마스크 필터를 지원합니다.

**쿼리 파라미터**:
- `limit` (옵션): 최대 반환 개수. 생략 시 전체
- `semesterOrder` (옵션): `asc` 또는 `desc` (학기 정렬)
- `day` (옵션): 요일 비트마스크(1~127). 월:1, 화:2, 수:4, 목:8, 금:16, 토:32, 일:64. 합으로 여러 요일 지정
- `startId` (옵션): 시작 ID 기준으로 `classId >= startId`인 데이터부터 반환
- `teacherId` (옵션): 특정 교사가 담당하는 강좌만 필터링

**요청 예시**:
```bash
curl -X GET "http://localhost:8080/api/classes"
curl -X GET "http://localhost:8080/api/classes?limit=10"
curl -X GET "http://localhost:8080/api/classes?semesterOrder=desc&limit=5"
curl -X GET "http://localhost:8080/api/classes?day=24"                 # 목+금
curl -X GET "http://localhost:8080/api/classes?day=31&semesterOrder=asc" # 평일 정렬
curl -X GET "http://localhost:8080/api/classes?startId=50&limit=10"     # ID 50 이상 10개
curl -X GET "http://localhost:8080/api/classes?teacherId=1"            # 교사 ID 1의 강좌만
```

#### 1.3 여러 강좌 한번에 조회

**엔드포인트**: `POST /api/classes/batch`

**설명**: 여러 강좌를 ID 목록으로 한번에 조회합니다.

**요청 본문**:
```json
[1, 2, 3]
```

### 2. 출석 관리 API

#### 2.1 클래스별 출석 통계 조회

**엔드포인트**: `GET /api/attendance/class/{studentId}/{classId}`

**설명**: 특정 학생의 특정 클래스에 대한 모든 세션 출석 현황과 통계를 조회합니다.

**응답 예시**:
```json
{
  "studentId": 1,
  "studentName": "kim@example.com",
  "classId": 1,
  "className": "2024-1",
  "attendanceResults": [
    {
      "sessionId": 1,
      "sessionName": "session1",
      "sessionOnDate": "2024-01-15T10:00:00Z",
      "status": "PRESENT",
      "note": "좋은 수업이었습니다",
      "recordedAt": "2024-01-15T10:05:00Z"
    }
  ],
  "averageScore": 0.0,
  "totalSessions": 2,
  "attendedSessions": 1,
  "attendanceRate": 50.0
}
```

#### 2.2 세션별 출석 조회

**엔드포인트**: `GET /api/attendance/session/{studentId}/{sessionId}`

**설명**: 특정 학생의 특정 세션에 대한 출석 정보를 조회합니다.

### 3. 사용자 관리 API

#### 3.1 사용자 정보 조회

**엔드포인트**: `GET /api/users/{userId}`

**설명**: 특정 사용자의 정보를 조회합니다.

**응답 예시**:
```json
{
  "userId": 1,
  "email": "kim@example.com",
  "role": "STUDENT",
  "isActive": true,
  "createdAt": "2024-01-15T10:00:00Z",
  "updatedAt": "2024-01-15T10:00:00Z"
}
```

**사용자 역할 (role)**:
- `STUDENT`: 학생
- `TEACHER`: 교사

#### 3.2 사용자 프로필 조회

**엔드포인트**: `GET /api/user-profiles/{userId}`

**설명**: 특정 사용자의 상세 프로필 정보를 조회합니다.

**응답 예시**:
```json
{
  "userId": 1,
  "name": "김교수",
  "desiredCourse": "자바프로그래밍",
  "desiredJob": "소프트웨어 개발자",
  "birthDate": "1985-03-15",
  "school": "서울대학교",
  "phone": "010-1234-5678",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

#### 3.3 사용자 프로필 수정

**엔드포인트**: `PUT /api/user-profiles/{userId}`

**설명**: 특정 사용자의 프로필 정보를 수정합니다. 부분 수정을 지원합니다.

**요청 본문**:
```json
{
  "name": "김교수님",
  "desiredCourse": "스프링부트",
  "desiredJob": "백엔드 개발자",
  "birthDate": "1985-03-15",
  "school": "서울대학교",
  "phone": "010-1234-5678"
}
```

**데이터 검증**:
- 이름이 비어있으면 안 됩니다
- 생년월일은 미래 날짜일 수 없습니다
- 전화번호는 010-XXXX-XXXX 형식이어야 합니다

### 4. 수강 신청 API

#### 4.1 강좌 신청

**엔드포인트**: `POST /api/enrollments`

**설명**: 학생이 강좌에 수강 신청합니다.

**요청 본문**:
```json
{
  "studentId": 2,
  "classId": 1
}
```

**응답 예시** (201 Created):
```json
{
  "studentId": 2,
  "classId": 1,
  "status": "ENROLLED",
  "createdAt": "2024-01-20T15:30:00Z"
}
```

#### 4.2 수강 신청 목록 조회

**엔드포인트**: `GET /api/enrollments`

**설명**: 수강 신청 목록을 조회합니다. 다양한 조건으로 필터링 가능합니다.

**쿼리 파라미터**:
- `studentId` (옵션): 특정 학생의 수강 신청만 조회
- `classId` (옵션): 특정 강좌의 수강 신청만 조회
- `status` (옵션): 특정 상태의 수강 신청만 조회 (예: ENROLLED, CANCELLED)

**요청 예시**:
```bash
# 전체 수강 신청 조회
curl -X GET "http://localhost:8080/api/enrollments"

# 특정 학생의 수강 신청 조회
curl -X GET "http://localhost:8080/api/enrollments?studentId=2"

# 특정 강좌의 수강 신청 조회
curl -X GET "http://localhost:8080/api/enrollments?classId=1"

# 특정 상태의 수강 신청 조회
curl -X GET "http://localhost:8080/api/enrollments?status=ENROLLED"

# 복합 조건 조회
curl -X GET "http://localhost:8080/api/enrollments?studentId=2&status=ENROLLED"
```

**검증 규칙**:
- 학생 ID와 강좌 ID는 필수이며 양수여야 합니다
- 신청하는 사용자는 반드시 학생(STUDENT) 역할이어야 합니다
- 존재하지 않는 학생이나 강좌는 신청할 수 없습니다
- 이미 수강 중인 강좌는 중복 신청할 수 없습니다

### 5. 세션 관리 API

#### 5.1 강좌별 세션 목록 조회

**엔드포인트**: `GET /api/sessions/classes/{classId}`

**설명**: 특정 강좌의 모든 세션을 날짜순으로 조회합니다.

**응답 예시**:
```json
[
  {
    "sessionId": 1,
    "classId": 1,
    "sessionName": "session1",
    "onDate": "2024-11-25T14:00:00Z",
    "createdAt": "2024-12-02T10:00:00Z"
  },
  {
    "sessionId": 2,
    "classId": 1,
    "sessionName": "session2",
    "onDate": "2024-11-27T14:00:00Z",
    "createdAt": "2024-12-02T10:00:00Z"
  }
]
```

## 📊 출석 상태

- **PRESENT**: 출석
- **ABSENT**: 결석
- **LATE**: 지각
- **EXCUSED**: 사유 결석

## 🔧 데이터 생성 시스템

### DataInitializer (기본 테스트 데이터)
- **위치**: `src/main/java/com/kt/damim/student/config/DataInitializer.java`
- **활성화 조건**: `data.generation.enabled=false` (기본값)
- **기능**: 소량의 기본 테스트 데이터 생성

### DataGenerator (대량 테스트 데이터)
- **위치**: `src/main/java/com/kt/damim/student/data/DataGenerator.java`
- **활성화 조건**: `data.generation.enabled=true`
- **기능**: 대량의 테스트 데이터 생성

### 설정 옵션
```properties
# 데이터 생성 활성화
data.generation.enabled=true

# 생성할 데이터 양 설정
data.generation.teacher-count=10          # 교사 수
data.generation.student-count=100         # 학생 수
data.generation.class-per-teacher=5       # 교사당 클래스 수
data.generation.enrollment-per-student=5  # 학생당 수강 과목 수
data.generation.session-per-class=10      # 클래스당 세션 수
data.generation.attendance-rate=0.7       # 출석률 (0.0 ~ 1.0)
```

## 🧪 테스트 환경

### 테스트 설정
- **데이터베이스**: H2 인메모리 데이터베이스 사용
- **프로파일**: `test` 프로파일 활성화
- **DataInitializer/DataGenerator**: 테스트 환경에서는 비활성화됨

### 테스트 파일 위치
- `src/test/resources/application.properties`: 테스트용 설정
- `src/test/java/com/kt/damim/student/controller/AttendanceControllerTest.java`: 컨트롤러 테스트

## 🐛 에러 처리

API는 다음과 같은 에러 상황을 처리합니다:

- **400 Bad Request**: 잘못된 요청 (존재하지 않는 학생/클래스/세션 ID)
- **500 Internal Server Error**: 서버 내부 오류

에러 응답 예시:
```json
{
  "timestamp": "2024-01-20T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "학생을 찾을 수 없습니다: 999"
}
```

## 📝 향후 확장 계획

- 학생별 전체 클래스 통계
- 기간별 출석 통계
- 성적 분석 및 리포트
- 출석 데이터 수정/삭제 기능
- 대시보드 API
- 사용자 인증 및 권한 관리
- 클래스 일정 관리 API
