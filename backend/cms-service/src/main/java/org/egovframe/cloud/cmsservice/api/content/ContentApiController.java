package org.egovframe.cloud.cmsservice.api.content;

import lombok.RequiredArgsConstructor;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentListResponseDto;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentResponseDto;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentSaveRequestDto;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentUpdateRequestDto;
import org.egovframe.cloud.cmsservice.service.content.ContentService;
import org.egovframe.cloud.cmsservice.service.content.ExcelContentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.security.RolesAllowed;

/**
 * org.egovframe.cloud.cmsservice.api.content.ContentApiController
 * <p>
 * 콘텐츠 API 컨트롤러 클래스
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
@RequestMapping("/api/v1/contents")
public class ContentApiController {

    private final ContentService contentService;
    private final ExcelContentService excelContentService;

    /**
     * 콘텐츠 목록 조회
     *
     * @param siteId   사이트 ID
     * @param keyword  검색어
     * @param page     페이지 번호
     * @param size     페이지 크기
     * @return Mono<Page<ContentListResponseDto>> 콘텐츠 목록 정보
     */
    @GetMapping
    public Mono<Page<ContentListResponseDto>> findAll(@RequestParam Long siteId,
                                                    @RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return contentService.findAll(siteId, keyword, PageRequest.of(page, size));
    }

    /**
     * 콘텐츠 상세 조회
     *
     * @param contentId 콘텐츠 ID
     * @return Mono<ContentResponseDto> 콘텐츠 상세 정보
     */
    @GetMapping("/{contentId}")
    public Mono<ContentResponseDto> findById(@PathVariable Long contentId) {
        return contentService.findById(contentId);
    }

    /**
     * 콘텐츠 등록
     *
     * @param dto 등록할 콘텐츠 정보
     * @return Mono<ContentResponseDto> 등록된 콘텐츠 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ContentResponseDto> save(@RequestBody ContentSaveRequestDto dto) {
        return contentService.save(dto);
    }

    /**
     * 콘텐츠 수정
     *
     * @param contentId 콘텐츠 ID
     * @param dto      수정할 콘텐츠 정보
     * @return Mono<ContentResponseDto> 수정된 콘텐츠 정보
     */
    @PutMapping("/{contentId}")
    public Mono<ContentResponseDto> update(@PathVariable Long contentId,
                                         @RequestBody ContentUpdateRequestDto dto) {
        return contentService.update(contentId, dto);
    }

    /**
     * 콘텐츠 삭제
     *
     * @param contentId 콘텐츠 ID
     * @return Mono<Void>
     */
    @DeleteMapping("/{contentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long contentId) {
        return contentService.delete(contentId);
    }

    /**
     * 엑셀 파일을 업로드하여 컨텐츠를 일괄 생성합니다.
     *
     * @param file    엑셀 파일
     * @param siteId  사이트 ID
     * @return 생성된 컨텐츠 목록
     */
    @PostMapping("/excel-upload/{siteId}")
    @RolesAllowed("ADMIN")
    public Flux<Content> uploadExcelContents(@RequestParam("file") MultipartFile file,
                                           @PathVariable Long siteId) {
        return excelContentService.uploadExcelContents(file, siteId);
    }
}
