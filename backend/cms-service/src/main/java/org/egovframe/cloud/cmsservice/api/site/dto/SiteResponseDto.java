package org.egovframe.cloud.cmsservice.api.site.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egovframe.cloud.cmsservice.domain.site.Site;

/**
 * org.egovframe.cloud.cmsservice.api.site.dto.SiteResponseDto
 * <p>
 * 사이트 응답 DTO 클래스
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
public class SiteResponseDto {

    private Long id;
    private String name;
    private String domain;
    private String description;
    private String templateId;
    private Boolean isDefault;
    private Boolean isUse;

    @Builder
    public SiteResponseDto(Site entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.domain = entity.getDomain();
        this.description = entity.getDescription();
        this.templateId = entity.getTemplateId();
        this.isDefault = entity.getIsDefault();
        this.isUse = entity.getIsUse();
    }
}
