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
 * org.egovframe.cloud.cmsservice.domain.content.Content
 * <p>
 * 콘텐츠 엔티티 클래스
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
@Table("cms_content")
public class Content extends BaseEntity {

    @Id
    @Column("content_id")
    private Long id;

    @Column("site_id")
    private Long siteId;

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

    @Column("is_use")
    private Boolean isUse;

    @Builder
    public Content(Long siteId, String title, String content, String contentType,
                  String templateId, String status, LocalDateTime publishStartDate,
                  LocalDateTime publishEndDate, String metaTitle, String metaDescription,
                  String metaKeywords) {
        this.siteId = siteId;
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
        this.version = 1;
        this.isUse = true;
    }

    public void update(String title, String content, String contentType,
                      String templateId, String status, LocalDateTime publishStartDate,
                      LocalDateTime publishEndDate, String metaTitle, String metaDescription,
                      String metaKeywords) {
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
        this.version++;
    }

    public void delete() {
        this.isUse = false;
    }
}
