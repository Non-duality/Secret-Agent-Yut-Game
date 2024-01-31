package com.ssafy.hungry.global.handler;

import com.ssafy.hungry.global.entity.SessionEntity;
import com.ssafy.hungry.global.repository.SessionRepository;
import com.ssafy.hungry.global.util.StompPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    private final SessionRepository sessionRepository ;

    public CustomHandshakeHandler(SessionRepository sessionRepository){
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        StompPrincipal principal = new StompPrincipal(UUID.randomUUID().toString());

        System.out.println("handshake  ");

        sessionRepository.save( new SessionEntity(email, principal));
        return principal;
    }
}
