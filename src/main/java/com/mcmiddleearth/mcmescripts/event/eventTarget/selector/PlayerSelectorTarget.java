package com.mcmiddleearth.mcmescripts.event.eventTarget.selector;

import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerSelectorTarget extends PlayerEventTarget {

    private final PlayerSelector selector;

    public PlayerSelectorTarget(PlayerSelector selector){
        this.selector = selector;
    }

    @Override
    public List<Player> getTargets(TriggerContext context) {
        return selector.select(context);
    }

    @Override
    public String toString() {
        return "(Player from selector) " + selector.getSelector();
    }
}
