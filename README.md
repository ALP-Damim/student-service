# 학생 출석 통계 서비스

학생의 학습 현황 및 성적 통계를 제공하는 Spring Boot 기반 REST API 서비스입니다.

## 🚀 기능

- **클래스별 출석 통계**: 특정 학생의 특정 클래스에 대한 모든 세션 출석 현황 및 통계
- **세션별 출석 조회**: 특정 학생의 특정 세션에 대한 출석 정보 조회
- **자동 통계 계산**: 평균 점수, 출석률, 총 세션 수 등 자동 계산

## 🛠 기술 스택

- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Java 17**

## 📋 데이터베이스 스키마

### 테이블 구조

1. **student**: 학생 정보
   - id (PK)
   - name
   - email
   - student_number

2. **class**: 강좌 정보
   - id (PK)
   - name
   - description

3. **session**: 강좌별 세션 정보
   - id (PK)
   - title
   - description
   - start_time
   - end_time
   - class_id (FK)

4. **attendance**: 출석 정보
   - id (PK)
   - student_id (FK)
   - session_id (FK)
   - status (PRESENT, ABSENT, LATE, EXCUSED)
   - score
   - comments
   - recorded_at

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

또는 수동으로 환경변수를 설정할 수 있습니다:

```bash
# PostgreSQL 설정
export POSTGRES_USER=app
export POSTGRES_PASSWORD=app_pw
export POSTGRES_DB=appdb
export POSTGRES_HOST=localhost
export POSTGRES_PORT=5432

# PgAdmin 설정
export PGADMIN_EMAIL=admin@example.com
export PGADMIN_PASSWORD=admin_pw
export PGADMIN_PORT=5050

# 애플리케이션 설정
export APP_PORT=8080
```

### 2. Docker Compose로 데이터베이스 시작

Docker Compose를 사용하여 PostgreSQL과 PgAdmin을 쉽게 시작할 수 있습니다:

**Windows:**
```bash
start-docker.bat
```

**Linux/Mac:**
```bash
./start-docker.sh
```

또는 수동으로 실행:
```bash
docker-compose up -d
```

### 3. 수동 데이터베이스 설정

로컬 PostgreSQL을 사용하는 경우, 다음 명령어로 데이터베이스를 생성하세요:

```sql
CREATE DATABASE appdb;
CREATE USER app WITH PASSWORD 'app_pw';
GRANT ALL PRIVILEGES ON DATABASE appdb TO app;
```

## 🚀 실행 방법

### 1. 데이터베이스 시작

먼저 PostgreSQL 데이터베이스를 시작하세요:

**Windows:**
```bash
start-docker.bat
```

**Linux/Mac:**
```bash
./start-docker.sh
```

### 2. 애플리케이션 실행

**Windows:**
```bash
gradlew.bat bootRun
```

**Linux/Mac:**
```bash
./gradlew bootRun
```

또는

```bash
./gradlew build
java -jar build/libs/student-service-0.0.1-SNAPSHOT.jar
```

### 2. 테스트 실행

**Windows:**
```bash
gradlew.bat test
```

**Linux/Mac:**
```bash
./gradlew test
```

### 3. 테스트 데이터 자동 생성

애플리케이션 실행 시 `DataInitializer`가 자동으로 테스트 데이터를 생성합니다:

- **학생**: 김철수, 이영희
- **클래스**: 자바 프로그래밍, 스프링 부트
- **세션**: 각 클래스별 세션들
- **출석 데이터**: 다양한 출석 상태와 점수

## 📚 API 사용법

### 1. 클래스별 출석 통계 조회

**엔드포인트**: `GET /api/attendance/class/{studentId}/{classId}`

**설명**: 특정 학생의 특정 클래스에 대한 모든 세션 출석 현황과 통계를 조회합니다.

**요청 예시**:
```bash
curl -X GET "http://localhost:8080/api/attendance/class/1/1"
```

**응답 예시**:
```json
{
  "studentId": 1,
  "studentName": "김철수",
  "classId": 1,
  "className": "자바 프로그래밍",
  "attendanceResults": [
    {
      "sessionId": 1,
      "sessionTitle": "자바 기초 1강",
      "sessionStartTime": "2024-01-15T10:00:00",
      "sessionEndTime": "2024-01-15T12:00:00",
      "status": "PRESENT",
      "score": 85,
      "comments": "좋은 수업이었습니다",
      "recordedAt": "2024-01-15T10:05:00"
    },
    {
      "sessionId": 2,
      "sessionTitle": "자바 기초 2강",
      "sessionStartTime": "2024-01-17T10:00:00",
      "sessionEndTime": "2024-01-17T12:00:00",
      "status": "LATE",
      "score": 70,
      "comments": "지각했습니다",
      "recordedAt": "2024-01-17T10:15:00"
    }
  ],
  "averageScore": 77.5,
  "totalSessions": 2,
  "attendedSessions": 1,
  "attendanceRate": 50.0
}
```

### 2. 세션별 출석 조회

**엔드포인트**: `GET /api/attendance/session/{studentId}/{sessionId}`

**설명**: 특정 학생의 특정 세션에 대한 출석 정보를 조회합니다.

**요청 예시**:
```bash
curl -X GET "http://localhost:8080/api/attendance/session/1/1"
```

**응답 예시**:
```json
{
  "studentId": 1,
  "studentName": "김철수",
  "sessionId": 1,
  "sessionTitle": "자바 기초 1강",
  "className": "자바 프로그래밍",
  "attendanceResult": {
    "sessionId": 1,
    "sessionTitle": "자바 기초 1강",
    "sessionStartTime": "2024-01-15T10:00:00",
    "sessionEndTime": "2024-01-15T12:00:00",
    "status": "PRESENT",
    "score": 85,
    "comments": "좋은 수업이었습니다",
    "recordedAt": "2024-01-15T10:05:00"
  }
}
```

## 📊 출석 상태

- **PRESENT**: 출석
- **ABSENT**: 결석
- **LATE**: 지각
- **EXCUSED**: 사유 결석

## 🔧 DataInitializer

`DataInitializer` 클래스는 애플리케이션 시작 시 자동으로 테스트 데이터를 생성합니다.

### 위치
`src/main/java/com/kt/damim/student/config/DataInitializer.java`

### 기능
- 애플리케이션 시작 시 자동 실행
- 기존 데이터가 있으면 생성하지 않음
- 다음 테스트 데이터를 생성:
  - 2명의 학생 (김철수, 이영희)
  - 2개의 클래스 (자바 프로그래밍, 스프링 부트)
  - 3개의 세션 (각 클래스별)
  - 4개의 출석 기록 (다양한 상태와 점수)

### 비활성화 방법
테스트 데이터 생성을 원하지 않는다면 `@Component` 어노테이션을 주석 처리하거나 클래스를 삭제하세요.

## 🧪 테스트 환경

### 테스트 설정
- **데이터베이스**: H2 인메모리 데이터베이스 사용
- **프로파일**: `test` 프로파일 활성화
- **DataInitializer**: 테스트 환경에서는 비활성화됨

### 테스트 파일 위치
- `src/test/resources/application.properties`: 테스트용 설정
- `src/test/java/com/kt/damim/student/controller/AttendanceControllerTest.java`: 컨트롤러 테스트

### 테스트 실행 결과
- 모든 API 엔드포인트 테스트
- 정상 케이스 및 예외 케이스 테스트
- MockMvc를 사용한 통합 테스트

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
