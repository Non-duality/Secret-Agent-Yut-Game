package com.ssafy.hungry.domain.game.entity.game.red;

import com.ssafy.hungry.domain.game.entity.game.Game;
import com.ssafy.hungry.domain.game.entity.game.GameMemberId;
import com.ssafy.hungry.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "red_team_member")
public class RedTeamMember {
    @EmbeddedId
    private GameMemberId id;

    @MapsId("gameCode")
    @ManyToOne
    @JoinColumn(name = "game_code" , referencedColumnName = "gameCode")
    private Game gameCode;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private UserEntity userId;
}