package org.egovframe.cloud.cmsservice.domain.content;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * org.egovframe.cloud.cmsservice.domain.content.ContentAuthorityRepository
 * <p>
 * 콘텐츠 권한 레포지토리 인터페이스
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
public interface ContentAuthorityRepository extends R2dbcRepository<ContentAuthority, Long> {
    
    Flux<ContentAuthority> findByContentId(Long contentId);
    
    Mono<Void> deleteByContentId(Long contentId);
    
    Flux<ContentAuthority> findByRoleId(String roleId);
}
