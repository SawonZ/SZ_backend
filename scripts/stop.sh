#!/usr/bin/env bash
set -euo pipefail

PID_FILE="/home/ubuntu/run/app.pid"
PORT="8080"
JAR_NAME="sawonz-backend.jar"

# 1) PID 파일 기준 종료
if [ -f "$PID_FILE" ]; then
  PID=$(cat "$PID_FILE" || true)
  if [ -n "${PID:-}" ] && ps -p "$PID" >/dev/null 2>&1; then
    kill -15 "$PID" || true
    for i in {1..20}; do
      ps -p "$PID" >/dev/null 2>&1 || break
      sleep 1
    done
    ps -p "$PID" >/dev/null 2>&1 && kill -9 "$PID" || true
  fi
  rm -f "$PID_FILE"
fi

# 2) 포트 점유 프로세스 추가 종료(스크립트 외 기동 케이스 대비)
if command -v lsof >/dev/null 2>&1; then
  PORT_PIDS=$(lsof -t -i:"$PORT" || true)
  if [ -n "${PORT_PIDS:-}" ]; then
    kill -15 $PORT_PIDS || true
    sleep 3
    for p in $PORT_PIDS; do ps -p "$p" >/dev/null 2>&1 && kill -9 "$p" || true; done
  fi
fi

# 3) JAR 이름으로도 마무리 (혹시 포트가 변경되어 떠 있던 경우)
pgrep -f "$JAR_NAME" >/dev/null 2>&1 && pkill -9 -f "$JAR_NAME" || true
