package org.egovframe.cloud.boardservice.domain.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egovframe.cloud.common.domain.Role;
import org.egovframe.cloud.servlet.domain.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "role_permission")
public class RolePermission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @Column(name = "resource_id")
    private String resourceId;  // 특정 리소스 ID (예: 게시판 ID)
}
