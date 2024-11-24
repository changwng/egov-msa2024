package org.egovframe.cloud.boardservice.domain.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egovframe.cloud.servlet.domain.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;        // 권한 이름

    @Column(nullable = false)
    private String description; // 권한 설명

    @Column(nullable = false)
    private String resourceType; // 리소스 타입 (BOARD, POST, COMMENT 등)

    @Column(nullable = false)
    private String action;      // 행동 (READ, WRITE, DELETE 등)
}
