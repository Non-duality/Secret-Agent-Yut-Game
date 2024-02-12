package com.ssafy.hungry.domain.game.repository;

import com.ssafy.hungry.domain.game.entity.game.RedTeamUnitId;
import com.ssafy.hungry.domain.game.entity.game.red.RedTeamUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedTeamUnitRepository extends JpaRepository<RedTeamUnit, RedTeamUnitId> {
}
