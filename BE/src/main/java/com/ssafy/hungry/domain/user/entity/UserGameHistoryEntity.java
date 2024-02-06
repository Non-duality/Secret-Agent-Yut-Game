package com.ssafy.hungry.domain.user.entity;

import com.ssafy.hungry.domain.game.entity.game.Game;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_game_history")
public class UserGameHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public UserEntity user;

    @ManyToOne
    @JoinColumn(name = "game_code", referencedColumnName = "gameCode")
    public Game gameCode;
}
