package com.ssafy.hungry.global.filter;

import com.ssafy.hungry.global.util.JWTUtil;
import com.ssafy.hungry.user.dto.JoinDto;
import com.ssafy.hungry.user.dto.LoginDto;
import com.ssafy.hungry.user.entity.TokenEntity;
import com.ssafy.hungry.user.entity.UserEntity;
import com.ssafy.hungry.user.repository.TokenRepository;
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
        String refreshToken = request.getHeader("RefreshToken");

        //refresh 토큰과 함께 요청이 들어왔을 경우
        //redis에서 refreshToken값과 매칭되는 accessToken 값을 찾고 둘이 일치하면 재발급
        if(refreshToken != null){
            String token = authorization.split(" ")[1];
            //리프레시 토큰이 만료되었을 경우 재 로그인을 위한 401 반환
            if (jwtUtil.isExpired(refreshToken)) {

                System.out.println("token expired");
                filterChain.doFilter(request, response);

                //조건이 해당되면 메소드 종료 (필수)
                response.sendError(401);
                return;
            }
            //refreshToken 값을 키로 accessToken을 가져온다. 두 값이 일치하면 새로운 refreshToken 과 accessToken을 발급한다.
            if (token == repository.findById(refreshToken).get().getAccessToken()){
                String email = jwtUtil.getUsername(token);
                String role = jwtUtil.getRole(token);

                String acToken = jwtUtil.createAccessJwt(email, role, 15*60*1000L);
                String reToken = jwtUtil.createRefreshJwt(12*60*60*1000L);

                TokenEntity token1 = new TokenEntity(reToken, acToken, 12*60*60);
                repository.save(token1);
                repository.deleteById(refreshToken);

                response.addHeader("Authorization", "Bearer" + acToken);
                response.addHeader("RefreshToken", reToken);
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
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            response.sendError(401);
            return;
        }

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
}
