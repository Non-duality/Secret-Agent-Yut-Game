package com.ssafy.hungry.domain.event.controller;

import com.ssafy.hungry.domain.event.dto.EventDto;
import com.ssafy.hungry.global.repository.SessionRepository;
import com.ssafy.hungry.global.util.StompPrincipal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class EventStompController {
    private final SessionRepository sessionRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public EventStompController(SessionRepository sessionRepository, SimpMessagingTemplate messagingTemplate){
        this.sessionRepository = sessionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/event")
    public void event (EventDto dto){
        StompPrincipal principal = sessionRepository.findById(dto.getToUserEmail()).get().getPrincipal();
        messagingTemplate.convertAndSendToUser(principal.getName(), "topic/chat/", dto);
    }
}
