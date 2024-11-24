package org.egovframe.cloud.cmsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * org.egovframe.cloud.cmsservice.CmsServiceApplication
 * <p>
 * CMS 서비스 어플리케이션 클래스
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
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"org.egovframe.cloud.common", "org.egovframe.cloud.cmsservice"})
public class CmsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsServiceApplication.class, args);
    }
}
