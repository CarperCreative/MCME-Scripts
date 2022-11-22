package com.mcmiddleearth.mcmescripts.event.eventTarget.context;

import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerContextTarget extends PlayerEventTarget {

    private final String contextKey;

    public PlayerContextTarget(String contextKey){
        this.contextKey = contextKey;
    }

    @Override
    public List<Player> getTargets(TriggerContext context) {
        return context.getPlayerContext(contextKey);
    }

    @Override
    public String toString() {
        return "(Player from context) " + contextKey;
    }
}
