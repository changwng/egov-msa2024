package org.egovframe.cloud.cmsservice.api.content.dto;

import lombok.Getter;
import org.egovframe.cloud.cmsservice.domain.content.Content;

import java.time.LocalDateTime;

/**
 * org.egovframe.cloud.cmsservice.api.content.dto.ContentResponseDto
 * <p>
 * 콘텐츠 상세 응답 DTO 클래스
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
public class ContentResponseDto {

    private final Long contentId;
    private final Long siteId;
    private final String title;
    private final String content;
    private final String contentType;
    private final String templateId;
    private final String status;
    private final LocalDateTime publishStartDate;
    private final LocalDateTime publishEndDate;
    private final String metaTitle;
    private final String metaDescription;
    private final String metaKeywords;
    private final Integer version;
    private final LocalDateTime lastModifiedDate;

    public ContentResponseDto(Content entity) {
        this.contentId = entity.getId();
        this.siteId = entity.getSiteId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.contentType = entity.getContentType();
        this.templateId = entity.getTemplateId();
        this.status = entity.getStatus();
        this.publishStartDate = entity.getPublishStartDate();
        this.publishEndDate = entity.getPublishEndDate();
        this.metaTitle = entity.getMetaTitle();
        this.metaDescription = entity.getMetaDescription();
        this.metaKeywords = entity.getMetaKeywords();
        this.version = entity.getVersion();
        this.lastModifiedDate = entity.getLastModifiedDate();
    }
}
