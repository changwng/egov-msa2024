package org.egovframe.cloud.cmsservice.service.content;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.egovframe.cloud.cmsservice.api.content.dto.ContentExcelUploadDto;
import org.egovframe.cloud.cmsservice.domain.content.Content;
import org.egovframe.cloud.cmsservice.domain.content.ContentRepository;
import org.egovframe.cloud.cmsservice.service.content.validator.ContentExcelValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * org.egovframe.cloud.cmsservice.service.content.ExcelContentService
 * <p>
 * 엑셀 컨텐츠 서비스 클래스
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
@Service
@RequiredArgsConstructor
public class ExcelContentService {

    private final ContentRepository contentRepository;
    private final ContentExcelValidator validator;

    /**
     * 엑셀 파일을 읽어 컨텐츠를 일괄 생성합니다.
     *
     * @param file    엑셀 파일
     * @param siteId  사이트 ID
     * @return 생성된 컨텐츠 목록
     */
    @Transactional
    public Flux<Content> uploadExcelContents(MultipartFile file, Long siteId) {
        try {
            List<ContentExcelUploadDto> dtos = readExcelFile(file);
            List<String> errors = validateExcelData(dtos);
            
            if (!errors.isEmpty()) {
                return Flux.error(new IllegalArgumentException(
                    String.format("엑셀 데이터 검증 실패:\n%s", String.join("\n", errors))
                ));
            }
            
            return Flux.fromIterable(dtos)
                    .map(dto -> dto.toEntity(siteId))
                    .flatMap(contentRepository::save)
                    .doOnNext(content -> log.info("컨텐츠 생성 완료: ID={}, 제목={}", content.getId(), content.getTitle()));
        } catch (IOException e) {
            return Flux.error(new RuntimeException("엑셀 파일 처리 중 오류가 발생했습니다.", e));
        }
    }

    private List<String> validateExcelData(List<ContentExcelUploadDto> dtos) {
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < dtos.size(); i++) {
            errors.addAll(validator.validate(dtos.get(i), i + 2)); // 엑셀의 실제 행 번호는 2부터 시작
        }
        return errors;
    }

    private List<ContentExcelUploadDto> readExcelFile(MultipartFile file) throws IOException {
        List<ContentExcelUploadDto> contents = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ContentExcelUploadDto dto = ContentExcelUploadDto.builder()
                        .title(getCellValue(row.getCell(0)))
                        .content(getCellValue(row.getCell(1)))
                        .contentType(getCellValue(row.getCell(2)))
                        .templateId(getCellValue(row.getCell(3)))
                        .status(getCellValue(row.getCell(4)))
                        .publishStartDate(getCellDateTime(row.getCell(5)))
                        .publishEndDate(getCellDateTime(row.getCell(6)))
                        .metaTitle(getCellValue(row.getCell(7)))
                        .metaDescription(getCellValue(row.getCell(8)))
                        .metaKeywords(getCellValue(row.getCell(9)))
                        .build();

                contents.add(dto);
            }
        }
        
        return contents;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    private LocalDateTime getCellDateTime(Cell cell) {
        if (cell == null) return null;
        
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
        } catch (Exception e) {
            log.error("날짜 변환 중 오류 발생", e);
        }
        return null;
    }
}
