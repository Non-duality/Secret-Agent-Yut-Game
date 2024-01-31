package com.ssafy.hungry.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.hungry.domain.login.dto.LoginDto;
import com.ssafy.hungry.domain.user.detail.CustomUserDetails;
import com.ssafy.hungry.global.util.JWTUtil;
<<<<<<< HEAD
=======
import com.ssafy.hungry.domain.user.dto.LoginDto;
>>>>>>> feat/BE-room
import com.ssafy.hungry.domain.user.entity.TokenEntity;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import com.ssafy.hungry.domain.user.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    private final TokenRepository repository;

    public JWTFilter(JWTUtil jwtUtil, TokenRepository repository) {

        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 스웨거 경로를 확인하고 필터 적용을 건너뛰기
        if (request.getRequestURI().startsWith("/swagger-ui") ||
                request.getRequestURI().startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 존재하거나 Bearer로 시작했는지 확인.
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰 검증
        // ( 비밀키 + 만료 + 잘못된 토큰 ) -- > 체크
        if (!jwtUtil.validateToken(token)) {
            System.out.println("토큰 검증 시도 중 문제 발생");

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("msg", "토큰 검증 시도 중 문제 발생");

            response.setStatus(406);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(resultMap));
            response.getWriter().flush();
//            filterChain.doFilter(request, response);
            return;
        }

        //토큰에서 username과 role 획득
        String email = jwtUtil.getUserId(token);

        //userEntity를 생성하여 값 set
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword("temppassword");

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
