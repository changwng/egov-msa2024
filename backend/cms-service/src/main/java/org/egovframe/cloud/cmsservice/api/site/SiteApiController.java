package org.egovframe.cloud.cmsservice.api.site;

import lombok.RequiredArgsConstructor;
import org.egovframe.cloud.cmsservice.api.site.dto.SiteResponseDto;
import org.egovframe.cloud.cmsservice.api.site.dto.SiteSaveRequestDto;
import org.egovframe.cloud.cmsservice.service.site.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * org.egovframe.cloud.cmsservice.api.site.SiteApiController
 * <p>
 * 사이트 API 컨트롤러 클래스
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
@RestController
@RequestMapping("/api/v1/sites")
public class SiteApiController {

    private final SiteService siteService;

    /**
     * 사이트 목록 조회
     *
     * @return 사이트 목록
     */
    @GetMapping
    public Flux<SiteResponseDto> findAll() {
        return siteService.findAll()
                .map(SiteResponseDto::new);
    }

    /**
     * 사이트 상세 조회
     *
     * @param id 사이트 ID
     * @return 사이트 상세 정보
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SiteResponseDto>> findById(@PathVariable Long id) {
        return siteService.findById(id)
                .map(site -> ResponseEntity.ok(new SiteResponseDto(site)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 도메인으로 사이트 조회
     *
     * @param domain 도메인
     * @return 사이트 정보
     */
    @GetMapping("/domain/{domain}")
    public Mono<ResponseEntity<SiteResponseDto>> findByDomain(@PathVariable String domain) {
        return siteService.findByDomain(domain)
                .map(site -> ResponseEntity.ok(new SiteResponseDto(site)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 기본 사이트 조회
     *
     * @return 기본 사이트 정보
     */
    @GetMapping("/default")
    public Mono<ResponseEntity<SiteResponseDto>> findDefaultSite() {
        return siteService.findDefaultSite()
                .map(site -> ResponseEntity.ok(new SiteResponseDto(site)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 사이트 등록
     *
     * @param requestDto 사이트 등록 요청 정보
     * @return 등록된 사이트 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SiteResponseDto> save(@Valid @RequestBody SiteSaveRequestDto requestDto) {
        return siteService.save(requestDto.toEntity())
                .map(SiteResponseDto::new);
    }

    /**
     * 사이트 수정
     *
     * @param id 사이트 ID
     * @param requestDto 사이트 수정 요청 정보
     * @return 수정된 사이트 정보
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SiteResponseDto>> update(@PathVariable Long id,
                                                      @Valid @RequestBody SiteSaveRequestDto requestDto) {
        return siteService.update(id, requestDto.toEntity())
                .map(site -> ResponseEntity.ok(new SiteResponseDto(site)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 사이트 삭제
     *
     * @param id 사이트 ID
     * @return void
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return siteService.delete(id);
    }
}
