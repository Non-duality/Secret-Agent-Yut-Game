package com.ssafy.hungry.domain.board.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "boards")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String subject;
    private String content;
}
