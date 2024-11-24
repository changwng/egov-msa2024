package org.egovframe.cloud.cmsservice.service.content;

import lombok.RequiredArgsConstructor;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentListResponseDto;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentResponseDto;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentSaveRequestDto;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentUpdateRequestDto;
import org.egovframe.cloud.cmsservice.domain.content.Content;
import org.egovframe.cloud.cmsservice.domain.content.ContentRepository;
import org.egovframe.cloud.cmsservice.domain.content.ContentVersion;
import org.egovframe.cloud.cmsservice.domain.content.ContentVersionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * org.egovframe.cloud.cmsservice.service.content.ContentService
 * <p>
 * 콘텐츠 서비스 클래스
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
@RequiredArgsConstructor
@Service
public class ContentService {

    private final ContentRepository contentRepository;
    private final ContentVersionRepository contentVersionRepository;

    /**
     * 콘텐츠 목록 조회
     *
     * @param siteId   사이트 ID
     * @param keyword  검색어
     * @param pageable 페이지 정보
     * @return Page<ContentListResponseDto> 콘텐츠 목록 정보
     */
    @Transactional(readOnly = true)
    public Mono<Page<ContentListResponseDto>> findAll(Long siteId, String keyword, Pageable pageable) {
        return contentRepository.search(siteId, keyword, pageable.getPageSize(), pageable.getOffset())
                .map(ContentListResponseDto::new)
                .collectList()
                .zipWith(contentRepository.countBySiteIdAndTitleContaining(siteId, keyword))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    /**
     * 콘텐츠 상세 조회
     *
     * @param contentId 콘텐츠 ID
     * @return Mono<ContentResponseDto> 콘텐츠 상세 정보
     */
    @Transactional(readOnly = true)
    public Mono<ContentResponseDto> findById(Long contentId) {
        return contentRepository.findById(contentId)
                .map(ContentResponseDto::new);
    }

    /**
     * 콘텐츠 등록
     *
     * @param dto 등록할 콘텐츠 정보
     * @return Mono<ContentResponseDto> 등록된 콘텐츠 정보
     */
    @Transactional
    public Mono<ContentResponseDto> save(ContentSaveRequestDto dto) {
        Content content = dto.toEntity();
        return contentRepository.save(content)
                .flatMap(savedContent -> {
                    ContentVersion version = ContentVersion.from(savedContent);
                    return contentVersionRepository.save(version)
                            .thenReturn(new ContentResponseDto(savedContent));
                });
    }

    /**
     * 콘텐츠 수정
     *
     * @param contentId 콘텐츠 ID
     * @param dto      수정할 콘텐츠 정보
     * @return Mono<ContentResponseDto> 수정된 콘텐츠 정보
     */
    @Transactional
    public Mono<ContentResponseDto> update(Long contentId, ContentUpdateRequestDto dto) {
        return contentRepository.findById(contentId)
                .flatMap(content -> {
                    content.update(dto.getTitle(), dto.getContent(), dto.getContentType(),
                            dto.getTemplateId(), dto.getStatus(), dto.getPublishStartDate(),
                            dto.getPublishEndDate(), dto.getMetaTitle(), dto.getMetaDescription(),
                            dto.getMetaKeywords());
                    return contentRepository.save(content)
                            .flatMap(updatedContent -> {
                                ContentVersion version = ContentVersion.from(updatedContent);
                                return contentVersionRepository.save(version)
                                        .thenReturn(new ContentResponseDto(updatedContent));
                            });
                });
    }

    /**
     * 콘텐츠 삭제
     *
     * @param contentId 콘텐츠 ID
     * @return Mono<Void>
     */
    @Transactional
    public Mono<Void> delete(Long contentId) {
        return contentRepository.findById(contentId)
                .flatMap(content -> {
                    content.delete();
                    return contentRepository.save(content);
                })
                .then();
    }
}
