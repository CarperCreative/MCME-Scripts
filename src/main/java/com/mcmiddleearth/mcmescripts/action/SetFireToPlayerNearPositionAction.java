package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;

import java.util.logging.Logger;

public class SetFireToPlayerNearPositionAction extends PlayerTargetedAction {

    public SetFireToPlayerNearPositionAction(PlayerEventTarget target, EventPosition position, double radius, int ticks) {
        super(target, (player, context) -> {
            Logger.getGlobal().warning("FIRE " + player.getLocation().toVector().distance(position.getPosition(context)) + " " + radius);
            if(player.getLocation().toVector().distance(position.getPosition(context)) <= radius){
                Logger.getGlobal().warning("SET ON FIRE");
                player.setFireTicks(ticks);
            }
        });
    }
}