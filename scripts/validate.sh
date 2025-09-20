#!/usr/bin/env bash                                   # bash로 실행
set -euo pipefail                                     # 에러시 실패 처리(배포 실패로 표기되게 함)

# 애플리케이션 헬스 체크
# -s: silent(진행바/메시지 숨김)
# -f: HTTP 4xx/5xx일 때 실패(exit code != 0)
# 127.0.0.1: 로컬에서 직접 점검(보안그룹/퍼블릭IP와 무관)
# >/dev/null: 출력 버림(성공/실패 코드만 사용)
# || exit 1: 실패 시 즉시 비정상 종료 → CodeDeploy가 ValidateService 실패로 인지
# (포트/엔드포인트가 다르면 여기만 환경에 맞게 변경하세요)
curl -sf http://127.0.0.1:8080/actuator/health >/dev/null || exit 1
