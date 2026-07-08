package com.traveler.web.global.security.config;

import com.traveler.web.global.security.oauth2.CookieAuthorizationRequestRepository;
import com.traveler.web.global.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.traveler.web.global.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                // CORS는 api-gateway(globalcors)에서 일괄 처리 — 여기서 중복 설정하지 않음
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(
                        oauth2 -> oauth2.authorizationEndpoint(
                                        endpoint -> endpoint.baseUri("/api/v1/auth/oauth2/authorize")
                                                .authorizationRequestRepository(cookieAuthorizationRequestRepository))
                                .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/*"))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler) // 실패 핸들러 추가 권장
                        );

        return http.build();
    }
}
