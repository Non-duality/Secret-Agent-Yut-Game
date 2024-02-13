package com.ssafy.hungry.domain.board.controller;


import com.ssafy.hungry.domain.board.entity.BoardEntity;
import com.ssafy.hungry.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public List<BoardEntity> boardList(){
        return boardService.boardList();
    }
}
