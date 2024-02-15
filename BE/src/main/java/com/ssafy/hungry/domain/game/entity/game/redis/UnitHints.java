package com.ssafy.hungry.domain.game.entity.game.redis;

import java.util.List;
import java.util.Map;

public class UnitHints {
    Map<Integer, List<String>> unitHint;

    public UnitHints ( Map<Integer, List<String>> unitHint){
        this.unitHint = unitHint;
    }

    public Map<Integer, List<String>> getUnitHint() {
        return unitHint;
    }
}
