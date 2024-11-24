package org.egovframe.cloud.cmsservice.domain.site;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egovframe.cloud.cmsservice.domain.BaseEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * org.egovframe.cloud.cmsservice.domain.site.Site
 * <p>
 * 사이트 엔티티 클래스
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
@Table("cms_site")
public class Site extends BaseEntity {

    @Id
    @Column("site_id")
    private Long id;

    @Column("site_name")
    private String name;

    @Column("domain")
    private String domain;

    @Column("description")
    private String description;

    @Column("template_id")
    private String templateId;

    @Column("is_default")
    private Boolean isDefault;

    @Column("is_use")
    private Boolean isUse;

    @Builder
    public Site(String name, String domain, String description, String templateId, Boolean isDefault, Boolean isUse) {
        this.name = name;
        this.domain = domain;
        this.description = description;
        this.templateId = templateId;
        this.isDefault = isDefault;
        this.isUse = isUse;
    }

    public void update(String name, String domain, String description, String templateId, Boolean isDefault, Boolean isUse) {
        this.name = name;
        this.domain = domain;
        this.description = description;
        this.templateId = templateId;
        this.isDefault = isDefault;
        this.isUse = isUse;
    }
}
