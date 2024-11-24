package org.egovframe.cloud.cmsservice.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * org.egovframe.cloud.cmsservice.client.dto.UserRolesResponseDto
 * <p>
 * User Service 사용자 권한 응답 DTO 클래스
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
public class UserRolesResponseDto {
    private String userId;
    private List<String> roles;
}
