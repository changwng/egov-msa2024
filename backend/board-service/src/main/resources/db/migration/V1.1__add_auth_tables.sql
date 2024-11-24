-- 권한 테이블
CREATE TABLE permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL,
    created_by VARCHAR(255),
    created_date DATETIME,
    last_modified_by VARCHAR(255),
    last_modified_date DATETIME,
    UNIQUE KEY uk_permission_name (name)
);

-- 역할-권한 매핑 테이블
CREATE TABLE role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(50) NOT NULL,
    permission_id BIGINT NOT NULL,
    resource_id VARCHAR(100),
    created_by VARCHAR(255),
    created_date DATETIME,
    last_modified_by VARCHAR(255),
    last_modified_date DATETIME,
    FOREIGN KEY (permission_id) REFERENCES permission (id),
    UNIQUE KEY uk_role_permission (role, permission_id, resource_id)
);

-- 위임된 권한 테이블
CREATE TABLE delegated_authority (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    delegator_id VARCHAR(255) NOT NULL,
    delegatee_id VARCHAR(255) NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    resource_id VARCHAR(100) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date DATETIME,
    last_modified_by VARCHAR(255),
    last_modified_date DATETIME
);

-- 기본 권한 데이터
INSERT INTO permission (name, description, resource_type, action) VALUES
('BOARD_CREATE', '게시판 생성 권한', 'BOARD', 'CREATE'),
('BOARD_READ', '게시판 조회 권한', 'BOARD', 'READ'),
('BOARD_UPDATE', '게시판 수정 권한', 'BOARD', 'UPDATE'),
('BOARD_DELETE', '게시판 삭제 권한', 'BOARD', 'DELETE'),
('POST_CREATE', '게시물 생성 권한', 'POST', 'CREATE'),
('POST_READ', '게시물 조회 권한', 'POST', 'READ'),
('POST_UPDATE', '게시물 수정 권한', 'POST', 'UPDATE'),
('POST_DELETE', '게시물 삭제 권한', 'POST', 'DELETE');

-- 기본 역할-권한 매핑
INSERT INTO role_permission (role, permission_id) 
SELECT 'SUPER_ADMIN', id FROM permission;

INSERT INTO role_permission (role, permission_id) 
SELECT 'ADMIN', id FROM permission 
WHERE name IN ('BOARD_READ', 'POST_CREATE', 'POST_READ', 'POST_UPDATE', 'POST_DELETE');
