package com.ssafy.hungry.global.filter;

import com.ssafy.hungry.global.util.JWTUtil;
import com.ssafy.hungry.domain.user.dto.LoginDto;
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

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    private final TokenRepository repository;

    public JWTFilter(JWTUtil jwtUtil, TokenRepository repository) {

        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");
        //requset에서 RefreshToken 헤더를 찾음
        String refreshToken = request.getHeader("RefreshToken");

        //refresh 토큰과 함께 요청이 들어왔을 경우
        //redis에서 refreshToken값과 매칭되는 accessToken 값을 찾고 둘이 일치하면 재발급
        if(refreshToken != null){
            String accessToken = authorization.split(" ")[1];
            System.out.println("refresh Token 검증");
            //리프레시 토큰이 만료되었을 경우 재 로그인을 위한 401 반환
            try{
                jwtUtil.isExpired(refreshToken);
            }catch(ExpiredJwtException e){
                System.out.println("token expired");

                response.sendError(401, "require login");
            }
            //refreshToken 값을 키로 accessToken을 가져온다. 두 값이 일치하면 새로운 refreshToken 과 accessToken을 발급한다.
            TokenEntity tokenEntity = repository.findById(refreshToken).get();
            if (accessToken.equals(tokenEntity.getAccessToken())) {
                String email = tokenEntity.getEmail();
                String role = tokenEntity.getRole();

                //token 재발급
                String acToken = jwtUtil.createAccessJwt(email, role, 15 * 1000L);
                String reToken = jwtUtil.createRefreshJwt(12 * 60 * 60 * 1000L);

                TokenEntity token1 = new TokenEntity(reToken, acToken, email, role, 12 * 60 * 60);
                repository.save(token1);
                //이전 refreshToken 은 삭제
                repository.deleteById(refreshToken);

                response.addHeader("Authorization", "Bearer " + acToken);
                response.addHeader("RefreshToken", reToken);

                //userEntity를 생성하여 값 set
                UserEntity userEntity = new UserEntity();
                userEntity.setEmail(email);
                userEntity.setPassword("temppassword");
                userEntity.setRole(role);

                //UserDetails에 회원 정보 객체 담기
                LoginDto customUserDetails = new LoginDto(userEntity);

                //스프링 시큐리티 인증 토큰 생성
                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                //세션에 사용자 등록
                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request, response);
                return;
            }else {
                response.sendError(401, "require login");
                return;
            }
        }

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        try{
            if (!jwtUtil.isExpired(token)) {
                //토큰에서 username과 role 획득
                String email = jwtUtil.getUsername(token);
                String role = jwtUtil.getRole(token);

                //userEntity를 생성하여 값 set
                UserEntity userEntity = new UserEntity();
                userEntity.setEmail(email);
                userEntity.setPassword("temppassword");
                userEntity.setRole(role);

                //UserDetails에 회원 정보 객체 담기
                LoginDto customUserDetails = new LoginDto(userEntity);

                //스프링 시큐리티 인증 토큰 생성
                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                //세션에 사용자 등록
                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request, response);
            }
        }catch(ExpiredJwtException e){
            System.out.println("token expired");

            response.sendError(401, "token expired");
        }
    }
}
