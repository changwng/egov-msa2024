package org.egovframe.cloud.boardservice.domain.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egovframe.cloud.servlet.domain.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "delegated_authority")
public class DelegatedAuthority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delegator_id", nullable = false)
    private String delegatorId;  // 권한을 위임한 관리자

    @Column(name = "delegatee_id", nullable = false)
    private String delegateeId;  // 권한을 위임받은 사용자

    @Column(name = "resource_type", nullable = false)
    private String resourceType; // 리소스 타입 (BOARD, POST 등)

    @Column(name = "resource_id", nullable = false)
    private String resourceId;   // 리소스 ID (게시판 ID 등)

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate; // 위임 시작일

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;   // 위임 종료일

    @Column(nullable = false)
    private boolean active = true;   // 위임 활성화 상태
}
