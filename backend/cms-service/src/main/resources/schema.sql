-- CMS 메뉴 테이블
CREATE TABLE IF NOT EXISTS cms_menu (
    menu_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_menu_id BIGINT,
    menu_name VARCHAR(100) NOT NULL,
    menu_order INT NOT NULL,
    menu_level INT NOT NULL,
    menu_url VARCHAR(255),
    menu_icon VARCHAR(100),
    is_use BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- CMS 페이지 테이블
CREATE TABLE IF NOT EXISTS cms_page (
    page_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    template_id VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    publish_start_date TIMESTAMP,
    publish_end_date TIMESTAMP,
    is_use BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- CMS 게시판 테이블
CREATE TABLE IF NOT EXISTS cms_board (
    board_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_name VARCHAR(100) NOT NULL,
    board_type VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    use_comment BOOLEAN DEFAULT false,
    use_attachment BOOLEAN DEFAULT false,
    attachment_size_limit BIGINT,
    is_use BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- CMS 게시글 테이블
CREATE TABLE IF NOT EXISTS cms_post (
    post_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    writer VARCHAR(100),
    view_count BIGINT DEFAULT 0,
    notice_yn BOOLEAN DEFAULT false,
    delete_yn BOOLEAN DEFAULT false,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES cms_board(board_id)
);

-- CMS 팝업 테이블
CREATE TABLE IF NOT EXISTS cms_popup (
    popup_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    popup_type VARCHAR(50) NOT NULL,
    location_x INT,
    location_y INT,
    width INT,
    height INT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    is_use BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- CMS 배너 테이블
CREATE TABLE IF NOT EXISTS cms_banner (
    banner_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    banner_type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    image_file VARCHAR(255),
    url VARCHAR(255),
    target VARCHAR(20),
    banner_order INT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    is_use BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- CMS 사이트 테이블
CREATE TABLE IF NOT EXISTS cms_site (
    site_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    site_name VARCHAR(100) NOT NULL,
    domain VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    template_id VARCHAR(50),
    is_default BOOLEAN DEFAULT false,
    is_use BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- CMS 콘텐츠 테이블
CREATE TABLE IF NOT EXISTS cms_content (
    content_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    site_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    content_type VARCHAR(50),
    template_id VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    publish_start_date TIMESTAMP,
    publish_end_date TIMESTAMP,
    meta_title VARCHAR(200),
    meta_description VARCHAR(500),
    meta_keywords VARCHAR(500),
    version INT DEFAULT 1,
    is_use BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (site_id) REFERENCES cms_site(site_id)
);

-- CMS 콘텐츠 버전 테이블
CREATE TABLE IF NOT EXISTS cms_content_version (
    version_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    content_type VARCHAR(50),
    template_id VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    publish_start_date TIMESTAMP,
    publish_end_date TIMESTAMP,
    meta_title VARCHAR(200),
    meta_description VARCHAR(500),
    meta_keywords VARCHAR(500),
    version INT NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (content_id) REFERENCES cms_content(content_id)
);

-- CMS 콘텐츠 권한 테이블
CREATE TABLE IF NOT EXISTS cms_content_authority (
    authority_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_id BIGINT NOT NULL,
    role_id VARCHAR(50) NOT NULL,
    permission VARCHAR(20) NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (content_id) REFERENCES cms_content(content_id)
);
