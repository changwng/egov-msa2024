package org.egovframe.cloud.cmsservice.domain.content;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egovframe.cloud.cmsservice.domain.BaseEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * org.egovframe.cloud.cmsservice.domain.content.ContentVersion
 * <p>
 * 콘텐츠 버전 엔티티 클래스
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
@Table("cms_content_version")
public class ContentVersion extends BaseEntity {

    @Id
    @Column("version_id")
    private Long id;

    @Column("content_id")
    private Long contentId;

    @Column("title")
    private String title;

    @Column("content")
    private String content;

    @Column("content_type")
    private String contentType;

    @Column("template_id")
    private String templateId;

    @Column("status")
    private String status;

    @Column("publish_start_date")
    private LocalDateTime publishStartDate;

    @Column("publish_end_date")
    private LocalDateTime publishEndDate;

    @Column("meta_title")
    private String metaTitle;

    @Column("meta_description")
    private String metaDescription;

    @Column("meta_keywords")
    private String metaKeywords;

    @Column("version")
    private Integer version;

    @Builder
    public ContentVersion(Long contentId, String title, String content, String contentType,
                         String templateId, String status, LocalDateTime publishStartDate,
                         LocalDateTime publishEndDate, String metaTitle, String metaDescription,
                         String metaKeywords, Integer version) {
        this.contentId = contentId;
        this.title = title;
        this.content = content;
        this.contentType = contentType;
        this.templateId = templateId;
        this.status = status;
        this.publishStartDate = publishStartDate;
        this.publishEndDate = publishEndDate;
        this.metaTitle = metaTitle;
        this.metaDescription = metaDescription;
        this.metaKeywords = metaKeywords;
        this.version = version;
    }

    public static ContentVersion from(Content content) {
        return ContentVersion.builder()
                .contentId(content.getId())
                .title(content.getTitle())
                .content(content.getContent())
                .contentType(content.getContentType())
                .templateId(content.getTemplateId())
                .status(content.getStatus())
                .publishStartDate(content.getPublishStartDate())
                .publishEndDate(content.getPublishEndDate())
                .metaTitle(content.getMetaTitle())
                .metaDescription(content.getMetaDescription())
                .metaKeywords(content.getMetaKeywords())
                .version(content.getVersion())
                .build();
    }
}
