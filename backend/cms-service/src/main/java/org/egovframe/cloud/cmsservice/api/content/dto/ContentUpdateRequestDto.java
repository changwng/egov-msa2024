package org.egovframe.cloud.cmsservice.api.content.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * org.egovframe.cloud.cmsservice.api.content.dto.ContentUpdateRequestDto
 * <p>
 * 콘텐츠 수정 요청 DTO 클래스
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
public class ContentUpdateRequestDto {

    private String title;
    private String content;
    private String contentType;
    private String templateId;
    private String status;
    private LocalDateTime publishStartDate;
    private LocalDateTime publishEndDate;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;

    @Builder
    public ContentUpdateRequestDto(String title, String content, String contentType,
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
    }
}
