#!/usr/bin/env bash

set -euo pipefail

# 애플리케이션 JAR 위치
APP_DIR="/home/ubuntu/app"

JAR="$APP_DIR/sawonz-backend.jar"   # 실행할 JAR 파일 경로(배포 시 appspec.yml에서 복사됨)
LOG_DIR="/home/ubuntu/logs"         # 표준 출력/에러 로그 저장 디렉터리
RUN_DIR="/home/ubuntu/run"          # PID 파일(프로세스 ID) 저장 디렉터리
PID_FILE="$RUN_DIR/app.pid"         # 현재 실행 중인 프로세스의 PID를 기록할 파일

# 로그/런타임 디렉터리 보장(없으면 생성)
mkdir -p "$LOG_DIR" "$RUN_DIR"

# PID 파일이 존재하고, 그 PID가 실제로 실행 중이면(중복 기동 방지)
if [ -f "$PID_FILE" ] && ps -p "$(cat "$PID_FILE")" >/dev/null 2>&1; then
  # 이미 실행 중이므로 재기동하지 않고 정상 종료
  echo "already running"; exit 0
fi

# 외부 설정을 확실히 읽게 함
cd "$APP_DIR"
EXTRA_CFG="--spring.config.additional-location=file:/home/ubuntu/app/"

# 백그라운드로 JAR 실행
# - nohup: 터미널 종료(SIGHUP)에도 프로세스가 계속 실행되도록 함
# - 표준출력/표준에러를 app.out으로 리다이렉트(2>&1: 에러도 합침)
# - &: 백그라운드 실행
nohup java -jar "$JAR" > "$LOG_DIR/app.out" 2>&1 &

# $!: 바로 직전에 백그라운드로 실행한 프로세스의 PID
# PID 파일에 기록하여 stop 훅에서 참조 가능하게 함
echo $! > "$PID_FILE"
