package org.egovframe.cloud.cmsservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * org.egovframe.cloud.cmsservice.config.TokenProvider
 * <p>
 * JWT 토큰 처리 클래스
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
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "authorities";

    public Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        return new ReactiveJwtAuthenticationConverterAdapter(source -> {
            Collection<GrantedAuthority> authorities = extractAuthorities(source);
            return Mono.just(new JwtAuthenticationToken(source, authorities));
        });
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> authorities = jwt.getClaimAsStringList(AUTHORITIES_KEY);
        if (authorities == null) {
            return Collections.emptyList();
        }
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
