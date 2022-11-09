package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;

public class SetOnFireAction extends PlayerTargetedAction {

    public SetOnFireAction(PlayerEventTarget target, int ticks) {
        super(target, (player, context) -> {

            player.setFireTicks(ticks);
        });
    }
}