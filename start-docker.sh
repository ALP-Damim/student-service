#!/bin/bash
# Docker Compose 시작 스크립트 (Linux/Mac)

echo "Docker Compose로 PostgreSQL과 PgAdmin을 시작합니다..."

# 환경변수 설정
export POSTGRES_USER=app
export POSTGRES_PASSWORD=app_pw
export POSTGRES_DB=appdb
export POSTGRES_HOST=localhost
export POSTGRES_PORT=5432
export PGADMIN_EMAIL=admin@example.com
export PGADMIN_PASSWORD=admin_pw
export PGADMIN_PORT=5050

echo "환경변수가 설정되었습니다."
echo ""

# Docker Compose 실행
docker-compose up -d

echo ""
echo "PostgreSQL과 PgAdmin이 시작되었습니다."
echo ""
echo "접속 정보:"
echo "- PostgreSQL: localhost:5432"
echo "- PgAdmin: http://localhost:5050"
echo "  - 이메일: $PGADMIN_EMAIL"
echo "  - 비밀번호: $PGADMIN_PASSWORD"
echo ""
echo "데이터베이스 연결 정보:"
echo "- 데이터베이스: $POSTGRES_DB"
echo "- 사용자: $POSTGRES_USER"
echo "- 비밀번호: $POSTGRES_PASSWORD"
echo ""
echo "이제 다음 명령어로 애플리케이션을 실행할 수 있습니다:"
echo "./gradlew bootRun"
