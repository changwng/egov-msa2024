package org.egovframe.cloud.cmsservice.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

/**
 * org.egovframe.cloud.cmsservice.client.UserServiceClient
 * <p>
 * User Service 클라이언트 인터페이스
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
@HttpExchange("/api/v1/users")
public interface UserServiceClient {

    @GetExchange("/{userId}")
    Mono<UserResponseDto> getUser(@PathVariable("userId") String userId);

    @GetExchange("/{userId}/roles")
    Mono<UserRolesResponseDto> getUserRoles(@PathVariable("userId") String userId);
}
