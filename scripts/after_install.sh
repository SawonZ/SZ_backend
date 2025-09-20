#!/usr/bin/env bash

set -euo pipefail

# 배포/로그/PID 보관 디렉터리 생성(-p: 이미 있어도 OK)
sudo mkdir -p /home/ubuntu/app /home/ubuntu/logs /home/ubuntu/run

# /home/ubuntu 이하의 소유자/그룹을 ubuntu로 재귀 변경(권한 문제 방지)
sudo chown -R ubuntu:ubuntu /home/ubuntu

# 배포 훅에서 실행할 .sh 파일들에 실행 권한 추가
chmod +x /home/ubuntu/deploy/*.sh