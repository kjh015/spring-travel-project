package com.traveler.sign.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider; // 클라이언트 요청은 서블릿 디스패처가 받음. 그 사이에 필터를 놔서 유효한 요청만 서블릿으로 가도록 필터링함

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        String token = jwtTokenProvider.resolveToken(request);
        logger.info("[doFilterInternal] token 값 추출 완료. token : {}", token);

        logger.info("[doFilterInternal] token 값 유효성 체크 시작");
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("[doFilterInternal] token 값 유효성 체크 완료");
        }
        filterChain.doFilter(request, response);	// filterChain의 다음 필터 실행
    }
}
