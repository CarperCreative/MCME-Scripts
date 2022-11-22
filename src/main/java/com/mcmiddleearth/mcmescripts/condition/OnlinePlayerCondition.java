package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;

public class OnlinePlayerCondition extends CriterionCondition {

    public OnlinePlayerCondition(PlayerEventTarget target, Criterion test) {
        super(target, test);
    }
}
