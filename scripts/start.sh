#!/usr/bin/env bash
set -euo pipefail

APP_DIR="/home/ubuntu/app"
JAR="$APP_DIR/sawonz-backend.jar"

LOG_DIR="/home/ubuntu/logs"
RUN_DIR="/home/ubuntu/run"
PID_FILE="$RUN_DIR/app.pid"

PORT="${SERVER_PORT:-8080}"                 # 필요 시 환경변수로 바꿔 사용
PROFILE="${SPRING_PROFILES_ACTIVE:-private}"# default: private
TIMEZONE="${APP_TZ:-Asia/Seoul}"            # 한국 시간대

mkdir -p "$LOG_DIR" "$RUN_DIR"
cd "$APP_DIR"

# 혹시 남아있을 포트 점유 선제 정리(이중 안전장치)
if command -v lsof >/dev/null 2>&1; then
  PORT_PIDS=$(lsof -t -i:"$PORT" || true)
  if [ -n "${PORT_PIDS:-}" ]; then
    kill -15 $PORT_PIDS || true
    sleep 3
    for p in $PORT_PIDS; do ps -p "$p" >/dev/null 2>&1 && kill -9 "$p" || true; done
  fi
fi

# JAR 존재 확인
if [ ! -f "$JAR" ]; then
  echo "JAR not found: $JAR"
  exit 1
fi

# 외부 설정 경로 반영(이 줄이 기존엔 적용되지 않았습니다)
EXTRA_CFG="--spring.config.additional-location=file:/home/ubuntu/app/"
JAVA_OPTS="-Duser.timezone=${TIMEZONE}"
SPRING_OPTS="--spring.profiles.active=${PROFILE} --server.port=${PORT}"

# 기존 로그 보존하고 신규 로그로 append
touch "$LOG_DIR/app.out"

# 백그라운드 기동
nohup java $JAVA_OPTS -jar "$JAR" $EXTRA_CFG $SPRING_OPTS >> "$LOG_DIR/app.out" 2>&1 &

# PID 저장
echo $! > "$PID_FILE"
