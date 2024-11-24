# 전자정부 MSA 게시판 서비스 권한 설계 문서

## 1. 프로젝트 개요

### 1.1 기본 정보
- 프로젝트명: 전자정부 MSA 게시판 서비스
- 개발 프레임워크: Spring Boot 2.7.12
- 개발 언어: Java 8
- 아키텍처: Microservice Architecture (MSA)

### 1.2 주요 마이크로서비스
1. User Service: 사용자 인증 및 기본 권한 관리
2. Board Service: 게시판 관련 기능 및 세부 권한 관리

## 2. 권한 시스템 설계

### 2.1 User Service 권한 체계

#### 2.1.1 주요 컴포넌트
1. AuthorizationService
   - 전체 시스템의 인가(Authorization) 처리
   - URL 기반의 권한 체크
   - 캐시를 통한 권한 정보 최적화

2. RoleAuthorizationService
   - 역할별 권한 관리
   - 권한 할당 및 회수

3. TokenProvider
   - JWT 토큰 기반의 인증 처리
   - Stateless 인증 구현

#### 2.1.2 특징
- 중앙집중식 사용자 관리
- JWT 기반의 토큰 인증
- 캐시를 통한 성능 최적화
- URL 패턴 기반의 권한 체크

### 2.2 Board Service 권한 체계

#### 2.2.1 데이터 모델
1. Permission (권한)
   ```sql
   CREATE TABLE permission (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(100) NOT NULL,
       description VARCHAR(255) NOT NULL,
       resource_type VARCHAR(50) NOT NULL,
       action VARCHAR(50) NOT NULL
   );
   ```

2. RolePermission (역할-권한 매핑)
   ```sql
   CREATE TABLE role_permission (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       role VARCHAR(50) NOT NULL,
       permission_id BIGINT NOT NULL,
       resource_id VARCHAR(100)
   );
   ```

3. DelegatedAuthority (위임된 권한)
   ```sql
   CREATE TABLE delegated_authority (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       delegator_id VARCHAR(255) NOT NULL,
       delegatee_id VARCHAR(255) NOT NULL,
       resource_type VARCHAR(50) NOT NULL,
       resource_id VARCHAR(100) NOT NULL,
       start_date DATETIME NOT NULL,
       end_date DATETIME NOT NULL
   );
   ```

#### 2.2.2 권한 구조
1. 리소스 타입
   - BOARD: 게시판
   - POST: 게시물
   - COMMENT: 댓글

2. 작업 타입
   - CREATE: 생성
   - READ: 조회
   - UPDATE: 수정
   - DELETE: 삭제

3. 사용자 역할
   - SUPER_ADMIN: 최고 관리자
   - ADMIN: 관리자
   - MANAGER: 매니저
   - USER: 일반 사용자
   - GUEST: 게스트

#### 2.2.3 권한 체크 구현
1. BoardApiController
   ```java
   @GetMapping("/api/v1/boards")
   @PreAuthorize("hasPermission(null, 'BOARD', 'READ')")
   public Page<BoardListResponseDto> findAll() { ... }

   @PostMapping("/api/v1/boards")
   @PreAuthorize("hasPermission(null, 'BOARD', 'CREATE')")
   public BoardResponseDto save() { ... }
   ```

2. PostsApiController
   ```java
   @GetMapping("/api/v1/posts/{boardNo}")
   @PreAuthorize("hasPermission(#boardNo, 'BOARD', 'READ')")
   public Page<PostsListResponseDto> findPage() { ... }

   @PutMapping("/api/v1/posts/{boardNo}/{postsNo}")
   @PreAuthorize("hasPermission(#boardNo, 'BOARD', 'UPDATE') and hasPermission(#postsNo, 'POST', 'UPDATE')")
   public PostsResponseDto update() { ... }
   ```

3. CommentApiController
   ```java
   @PostMapping("/api/v1/comments")
   @PreAuthorize("hasPermission(#requestDto.boardNo, 'BOARD', 'CREATE')")
   public CommentResponseDto save() { ... }

   @DeleteMapping("/api/v1/comments/{boardNo}/{postsNo}/{commentNo}")
   @PreAuthorize("hasPermission(#boardNo, 'BOARD', 'DELETE') and hasPermission(#commentNo, 'COMMENT', 'DELETE')")
   public void delete() { ... }
   ```

## 3. 권한 처리 흐름

### 3.1 인증 흐름
1. 사용자 로그인 (User Service)
2. JWT 토큰 발급
3. 토큰을 포함한 API 요청
4. 토큰 검증 및 기본 권한 확인
5. 세부 권한 체크 (Board Service)

### 3.2 권한 체크 계층
```
User Service (기본 권한)
    │
    ├── 인증(Authentication)
    ├── JWT 토큰 발급
    └── URL 기반 권한 체크
          │
Board Service (세부 권한)
    │
    ├── 게시판 권한
    │   ├── CREATE
    │   ├── READ
    │   ├── UPDATE
    │   └── DELETE
    │
    ├── 게시물 권한
    │   ├── CREATE
    │   ├── READ
    │   ├── UPDATE
    │   └── DELETE
    │
    └── 댓글 권한
        ├── CREATE
        ├── READ
        ├── UPDATE
        └── DELETE
```

## 4. 특징 및 장점

### 4.1 MSA 적합성
- 서비스별 독립적인 권한 관리
- 확장 가능한 권한 체계
- 유연한 권한 위임 시스템

### 4.2 보안성
- JWT 기반의 안전한 인증
- 세밀한 리소스 접근 제어
- 권한 위임 이력 관리

### 4.3 성능
- 캐시를 통한 권한 정보 최적화
- Stateless 인증으로 서버 부하 감소

### 4.4 유지보수성
- 명확한 권한 체계
- 코드 재사용성
- 확장 가능한 구조

## 5. 고려사항 및 향후 개선점

### 5.1 현재 고려사항
- 서비스 간 권한 동기화
- 캐시 무효화 전략
- 위임된 권한의 유효기간 관리
- 권한 체크 성능 최적화

### 5.2 향후 개선점
- 더 세밀한 권한 감사(Audit) 기능
- 권한 위임 워크플로우 개선
- 권한 체크 캐싱 메커니즘 개선
- 실시간 권한 변경 알림 시스템

## 6. 결론
본 권한 설계는 MSA 환경에서 중앙집중식 사용자 관리와 도메인별 세부 권한 관리를 효과적으로 구현하였습니다. 특히 게시판 서비스에 특화된 세부 권한 체계를 통해 안전하고 유연한 접근 제어가 가능하며, 향후 시스템 확장성도 고려되어 있습니다.
