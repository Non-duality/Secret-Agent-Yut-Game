package com.ssafy.hungry.domain.event.controller;

import com.ssafy.hungry.domain.event.dto.EventDto;
import com.ssafy.hungry.global.entity.StompPrincipal;
import com.ssafy.hungry.global.repository.SessionRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class EventController {
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRepository sessionRepository;

    public EventController(SessionRepository sessionRepository, SimpMessagingTemplate messagingTemplate){
        this.sessionRepository = sessionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/event")
    public void event(EventDto dto){
        System.out.println("pub 실행");
        System.out.println(dto);
        String toUserEmail = dto.getToUserEmail();
        StompPrincipal principal = new StompPrincipal(sessionRepository.findById(toUserEmail).get().getStompPrincipal().getName());
        System.out.println(principal.getName());
        messagingTemplate.convertAndSendToUser(principal.getName(), "sub/event", dto);
    }
}
