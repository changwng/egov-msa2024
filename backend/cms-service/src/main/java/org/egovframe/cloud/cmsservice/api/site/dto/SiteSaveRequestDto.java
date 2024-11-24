package org.egovframe.cloud.cmsservice.api.site.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egovframe.cloud.cmsservice.domain.site.Site;

import javax.validation.constraints.NotBlank;

/**
 * org.egovframe.cloud.cmsservice.api.site.dto.SiteSaveRequestDto
 * <p>
 * 사이트 저장 요청 DTO 클래스
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
public class SiteSaveRequestDto {

    @NotBlank(message = "사이트명은 필수입니다")
    private String name;

    @NotBlank(message = "도메인은 필수입니다")
    private String domain;

    private String description;
    private String templateId;
    private Boolean isDefault;
    private Boolean isUse;

    @Builder
    public SiteSaveRequestDto(String name, String domain, String description,
                            String templateId, Boolean isDefault, Boolean isUse) {
        this.name = name;
        this.domain = domain;
        this.description = description;
        this.templateId = templateId;
        this.isDefault = isDefault;
        this.isUse = isUse;
    }

    public Site toEntity() {
        return Site.builder()
                .name(name)
                .domain(domain)
                .description(description)
                .templateId(templateId)
                .isDefault(isDefault)
                .isUse(isUse)
                .build();
    }
}
