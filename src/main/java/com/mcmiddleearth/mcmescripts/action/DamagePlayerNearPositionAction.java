package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;

public class DamagePlayerNearPositionAction extends PlayerTargetedAction {

    public DamagePlayerNearPositionAction(PlayerEventTarget target, EventPosition position, double radius, double damage) {
        super(target, (player, context) -> {
            if(player.getLocation().toVector().distance(position.getPosition(context)) <= radius){
                player.damage(damage);
            }
        });
    }
}