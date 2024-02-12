package com.ssafy.hungry.domain.game.repository;

import com.ssafy.hungry.domain.game.entity.game.GameStatus;
import com.ssafy.hungry.domain.game.entity.game.GameStatusId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStatusRepository extends JpaRepository<GameStatus, GameStatusId> {
}
