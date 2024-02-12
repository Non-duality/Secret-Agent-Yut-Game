package com.ssafy.hungry.domain.friend.controller;

import com.ssafy.hungry.domain.friend.dto.MyFriendDto;
import com.ssafy.hungry.domain.friend.dto.ReceiveRequestFriendDto;
import com.ssafy.hungry.domain.friend.dto.SendRequestFriendDto;
import com.ssafy.hungry.domain.friend.entity.FriendEntity;
import com.ssafy.hungry.domain.friend.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private FriendService friendService;

    public FriendController(FriendService friendService){
        this.friendService = friendService;
    }

    //내 친구 목록 가져오기 api
    @GetMapping("/myfriend")
    public List<MyFriendDto> myFriend(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendService.myFriend(email);
    }

    //친구 요청 보내기 api
    @PostMapping("/send")
    public ResponseEntity<String> sendRequestToUser(@ModelAttribute SendRequestFriendDto dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(dto.getToUserEmail());
        String result = friendService.sendRequestToUser(dto, email);

        return ResponseEntity.status(200).body(result);
    }

    //친구 요청 수락 api
    @PostMapping("/accept")
    public ResponseEntity<String> acceptRequsetFromUser(@ModelAttribute ReceiveRequestFriendDto dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String result = friendService.acceptRequestFromUser(dto, email);
        return ResponseEntity.status(200).body(result);
    }

    //내가 보낸 요청 목록
    @GetMapping("/send")
    public List<SendRequestFriendDto> mySendRequest(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendService.mySendRequest(email);
    }


    //내가 받은 요청 목록
    @GetMapping("/receive")
    public List<ReceiveRequestFriendDto> myReceiveRequest(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendService.myReceiveRequest(email);
    }

    //요청 거절 api
    @PostMapping("/reject")
    public ResponseEntity<String> reject(ReceiveRequestFriendDto dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String result = friendService.reject(dto, email);
        return ResponseEntity.status(200).body(result);
    }
}
