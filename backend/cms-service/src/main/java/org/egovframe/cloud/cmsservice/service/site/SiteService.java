package org.egovframe.cloud.cmsservice.service.site;

import lombok.RequiredArgsConstructor;
import org.egovframe.cloud.cmsservice.domain.site.Site;
import org.egovframe.cloud.cmsservice.domain.site.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * org.egovframe.cloud.cmsservice.service.site.SiteService
 * <p>
 * 사이트 서비스 클래스
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
@Service
@RequiredArgsConstructor
@Transactional
public class SiteService {

    private final SiteRepository siteRepository;

    /**
     * 사이트 목록 조회
     *
     * @return 사이트 목록
     */
    @Transactional(readOnly = true)
    public Flux<Site> findAll() {
        return siteRepository.findAll();
    }

    /**
     * 사이트 상세 조회
     *
     * @param id 사이트 ID
     * @return 사이트 상세 정보
     */
    @Transactional(readOnly = true)
    public Mono<Site> findById(Long id) {
        return siteRepository.findById(id);
    }

    /**
     * 도메인으로 사이트 조회
     *
     * @param domain 도메인
     * @return 사이트 정보
     */
    @Transactional(readOnly = true)
    public Mono<Site> findByDomain(String domain) {
        return siteRepository.findByDomain(domain);
    }

    /**
     * 기본 사이트 조회
     *
     * @return 기본 사이트 정보
     */
    @Transactional(readOnly = true)
    public Mono<Site> findDefaultSite() {
        return siteRepository.findByIsDefaultIsTrue();
    }

    /**
     * 사이트 등록
     *
     * @param site 사이트 정보
     * @return 등록된 사이트 정보
     */
    public Mono<Site> save(Site site) {
        return siteRepository.save(site);
    }

    /**
     * 사이트 수정
     *
     * @param id 사이트 ID
     * @param site 수정할 사이트 정보
     * @return 수정된 사이트 정보
     */
    public Mono<Site> update(Long id, Site site) {
        return siteRepository.findById(id)
                .flatMap(existingSite -> {
                    existingSite.update(
                            site.getName(),
                            site.getDomain(),
                            site.getDescription(),
                            site.getTemplateId(),
                            site.getIsDefault(),
                            site.getIsUse()
                    );
                    return siteRepository.save(existingSite);
                });
    }

    /**
     * 사이트 삭제
     *
     * @param id 사이트 ID
     * @return void
     */
    public Mono<Void> delete(Long id) {
        return siteRepository.deleteById(id);
    }
}
