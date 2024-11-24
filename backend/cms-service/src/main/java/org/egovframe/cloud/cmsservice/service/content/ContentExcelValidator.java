package org.egovframe.cloud.cmsservice.service.content;

import lombok.extern.slf4j.Slf4j;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentExcelUploadDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * org.egovframe.cloud.cmsservice.service.content.ContentExcelValidator
 * <p>
 * 콘텐츠 엑셀 데이터 검증 클래스
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
@Slf4j
@Component
public class ContentExcelValidator {

    /**
     * 엑셀 데이터 유효성을 검증합니다.
     *
     * @param dto 검증할 DTO
     * @param rowNum 엑셀 행 번호
     * @return 오류 메시지 목록
     */
    public List<String> validate(ContentExcelUploadDto dto, int rowNum) {
        List<String> errors = new ArrayList<>();

        // 필수 필드 검증
        if (!StringUtils.hasText(dto.getTitle())) {
            errors.add(String.format("행 %d: 제목은 필수 입력값입니다.", rowNum));
        }
        if (!StringUtils.hasText(dto.getContent())) {
            errors.add(String.format("행 %d: 내용은 필수 입력값입니다.", rowNum));
        }
        if (!StringUtils.hasText(dto.getContentType())) {
            errors.add(String.format("행 %d: 콘텐츠 타입은 필수 입력값입니다.", rowNum));
        }
        if (!StringUtils.hasText(dto.getStatus())) {
            errors.add(String.format("행 %d: 상태는 필수 입력값입니다.", rowNum));
        }

        // 날짜 유효성 검증
        if (dto.getPublishStartDate() != null && dto.getPublishEndDate() != null) {
            if (dto.getPublishStartDate().isAfter(dto.getPublishEndDate())) {
                errors.add(String.format("행 %d: 게시 시작일이 종료일보다 늦을 수 없습니다.", rowNum));
            }
        }

        // 콘텐츠 타입 검증
        if (StringUtils.hasText(dto.getContentType()) && 
            !isValidContentType(dto.getContentType())) {
            errors.add(String.format("행 %d: 유효하지 않은 콘텐츠 타입입니다.", rowNum));
        }

        // 상태 검증
        if (StringUtils.hasText(dto.getStatus()) && 
            !isValidStatus(dto.getStatus())) {
            errors.add(String.format("행 %d: 유효하지 않은 상태값입니다.", rowNum));
        }

        return errors;
    }

    private boolean isValidContentType(String contentType) {
        return contentType.matches("^(HTML|TEXT|MARKDOWN)$");
    }

    private boolean isValidStatus(String status) {
        return status.matches("^(DRAFT|PUBLISHED|ARCHIVED)$");
    }
}
