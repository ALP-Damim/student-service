#!/bin/bash
# Linux/Mac 환경변수 설정 스크립트

echo "PostgreSQL 환경변수를 설정합니다..."

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

echo "환경변수 설정이 완료되었습니다."
echo ""
echo "설정된 환경변수:"
echo "POSTGRES_USER=$POSTGRES_USER"
echo "POSTGRES_DB=$POSTGRES_DB"
echo "POSTGRES_HOST=$POSTGRES_HOST"
echo "POSTGRES_PORT=$POSTGRES_PORT"
echo "APP_PORT=$APP_PORT"
echo ""
echo "이제 다음 명령어로 애플리케이션을 실행할 수 있습니다:"
echo "./gradlew bootRun"
