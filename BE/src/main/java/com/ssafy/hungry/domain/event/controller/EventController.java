package com.ssafy.hungry.domain.event.controller;

import com.ssafy.hungry.domain.event.dto.EventDto;
import com.ssafy.hungry.global.entity.StompPrincipal;
import com.ssafy.hungry.global.repository.PrincipalRepository;
import com.ssafy.hungry.global.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EventController {
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRepository sessionRepository;
    private final PrincipalRepository principalRepository;

    @MessageMapping("/event")
    public void event(EventDto dto){
        System.out.println("pub 실행");

        String toUserEmail = dto.getToUserEmail();

        StompPrincipal principal = new StompPrincipal(sessionRepository.findById(toUserEmail).get().getStompPrincipal().getName());
        messagingTemplate.convertAndSendToUser(principal.getName(), "sub/event", dto);
    }
}
