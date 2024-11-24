package org.egovframe.cloud.cmsservice.domain.content;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * org.egovframe.cloud.cmsservice.domain.content.ContentRepository
 * <p>
 * 콘텐츠 레포지토리 인터페이스
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
public interface ContentRepository extends R2dbcRepository<Content, Long> {
    
    @Query("SELECT * FROM cms_content WHERE site_id = :siteId AND status = :status " +
           "AND publish_start_date <= CURRENT_TIMESTAMP " +
           "AND (publish_end_date IS NULL OR publish_end_date >= CURRENT_TIMESTAMP) " +
           "AND is_use = true")
    Flux<Content> findAllPublished(Long siteId, String status);

    @Query("SELECT * FROM cms_content WHERE site_id = :siteId AND title LIKE CONCAT('%', :keyword, '%') " +
           "ORDER BY content_id DESC LIMIT :size OFFSET :offset")
    Flux<Content> search(Long siteId, String keyword, int size, long offset);

    Mono<Long> countBySiteIdAndTitleContaining(Long siteId, String keyword);
}
