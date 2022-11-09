package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;

public class OnlinePlayerCondition extends CriterionCondition {

    public OnlinePlayerCondition(PlayerEventTarget target, Criterion test) {
        super(target, test);
    }

}
