package com.ssafy.hungry.domain.game.repository;

import com.ssafy.hungry.domain.game.entity.game.GameStatus;
import com.ssafy.hungry.domain.game.entity.game.TeamStatusId;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedTeamStatusRepository extends JpaRepository<RedTeamStatus, TeamStatusId> {
}
