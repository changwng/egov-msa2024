package org.egovframe.cloud.cmsservice.config;

import org.egovframe.cloud.cmsservice.client.UserServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * org.egovframe.cloud.cmsservice.config.WebClientConfig
 * <p>
 * WebClient 설정 클래스
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
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient userServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://user-service")
                .build();
    }

    @Bean
    public UserServiceClient userServiceClient(WebClient userServiceWebClient) {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(userServiceWebClient))
                        .build();
        return httpServiceProxyFactory.createClient(UserServiceClient.class);
    }
}
