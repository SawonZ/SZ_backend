#!/usr/bin/env bash

set -euo pipefail              # 엄격 모드 적용

PID_FILE="/home/ubuntu/run/app.pid"
# start.sh에서 기록한 PID 파일 경로

if [ -f "$PID_FILE" ]; then
  # PID 파일이 존재하면(=이전에 실행했던 기록이 있으면)
  PID=$(cat "$PID_FILE")
  # 파일에서 PID 읽기

  if ps -p "$PID" >/dev/null 2>&1; then
    # 해당 PID의 프로세스가 실제로 실행 중이면
    kill -15 $PID
    # 우아한 종료(SIGTERM) 시도

    for i in {1..20}; do
      # 최대 20초(1초 간격) 대기하며 정상 종료 여부 확인
      ps -p "$PID" >/dev/null 2>&1 || break
      # 프로세스가 사라지면 탈출, 아직 있으면 1초 대기
      sleep 1
    done

    ps -p "$PID" >/dev/null 2>&1 && kill -9 "$PID" || true
    # 아직 살아있다면 강제 종료(SIGKILL)
    # '|| true'는 kill -9 실패 시에도 스크립트 전체 실패로 간주하지 않도록 함
  fi

  rm -f "$PID_FILE"
  # 더 이상 유효하지 않은 PID 파일 제거(다음 기동을 위해 정리)
fi
