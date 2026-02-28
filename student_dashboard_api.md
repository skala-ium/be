### 📘 **학생용 종합 과제 대시보드 API 명세 (MVP)**

### 🔍 **API 개요**
*   **기능**: 로그인한 학생의 수강 강의 정보와 과제 현황을 한눈에 확인하는 대시보드 데이터를 제공합니다.
*   **특징**: 노션 콜아웃 스타일의 UI 구성을 위해 '미제출'과 '제출 완료' 과제를 백엔드에서 미리 분류하여 내려줍니다.
*   **엔드포인트**: `GET /api/v1/assignments/me/dashboard`
*   **인증 방식**: `Authorization: Bearer {token}` (학생 계정 토큰)

### 📋 **응답 데이터 구조 (Response Body)**
*   **studentName**: 로그인한 학생의 이름
*   **className**: 학생이 소속된 강의/반 이름
*   **unsubmittedAssignments**: 아직 제출하지 않은 과제 리스트 (마감 임박순)
*   **submittedAssignments**: 제출을 완료한 과제 리스트 (최근 제출순)

### 📦 **과제 블록(AssignmentBlock) 상세 항목**
*   **assignmentId**: 과제 고유 식별자 (UUID)
*   **title**: 과제 제목
*   **professorName**: 담당 교수님 성함
*   **deadline**: 과제 마감 기한
*   **dDay**: 계산된 마감 문자열 (예: "D-3", "기한 지남")
*   **status**: 현재 제출 상태 (NOT_SUBMITTED, SUBMITTED 등)
*   **submittedAt**: 실제 제출 시각
*   **topic**: 과제 주제/태그

### 📝 **응답 샘플 (JSON)**
```json
{
  "success": true,
  "data": {
    "studentName": "김코딩",
    "className": "Spring Boot 2기 A반",
    "unsubmittedAssignments": [
      {
        "assignmentId": "uuid-1234",
        "title": "JPA 연동 실습",
        "professorName": "이자바 교수님",
        "deadline": "2026-03-05T23:59:59",
        "dDay": "D-5",
        "status": "NOT_SUBMITTED",
        "submittedAt": null,
        "topic": "JPA"
      }
    ],
    "submittedAssignments": [
      {
        "assignmentId": "uuid-5678",
        "title": "환경 세팅 완료",
        "professorName": "박환경 교수님",
        "deadline": "2026-02-28T18:00:00",
        "dDay": "기한 지남",
        "status": "COMPLETED",
        "submittedAt": "2026-02-27T14:30:00",
        "topic": "Environment"
      }
    ]
  }
}
```

### 💡 **프론트엔드(Vue.js) 개발 가이드**
*   **데이터 바인딩**: `unsubmittedAssignments`와 `submittedAssignments`를 각각 `v-for`로 렌더링하세요.
*   **상태 표시**: `dDay`와 `status`를 활용해 시각적 피드백을 제공하세요.
*   **태그 UI**: `topic` 데이터를 사용해 태그 배지를 구현하세요.
