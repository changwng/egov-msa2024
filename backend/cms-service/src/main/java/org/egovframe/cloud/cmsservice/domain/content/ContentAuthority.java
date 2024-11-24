package org.egovframe.cloud.cmsservice.domain.content;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egovframe.cloud.cmsservice.domain.BaseEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * org.egovframe.cloud.cmsservice.domain.content.ContentAuthority
 * <p>
 * 콘텐츠 권한 엔티티 클래스
 *
 * @author 표준프레임워크센터
 * @version 1.0
 * @since 2024/01/01
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *     수정일        수정자           수정내용
 *  ----------    --------    ---------------------------
 *  2024/01/01    changwng     최초 생성
 * </pre>
 */
@Getter
@NoArgsConstructor
@Table("cms_content_authority")
public class ContentAuthority extends BaseEntity {

    @Id
    @Column("authority_id")
    private Long id;

    @Column("content_id")
    private Long contentId;

    @Column("role_id")
    private String roleId;

    @Column("permission")
    private String permission;

    @Builder
    public ContentAuthority(Long contentId, String roleId, String permission) {
        this.contentId = contentId;
        this.roleId = roleId;
        this.permission = permission;
    }
}
