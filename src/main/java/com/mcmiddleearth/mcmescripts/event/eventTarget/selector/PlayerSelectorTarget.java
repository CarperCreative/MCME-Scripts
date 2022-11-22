package com.mcmiddleearth.mcmescripts.event.eventTarget.selector;

import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerSelectorTarget extends PlayerEventTarget {

    private final Selector selector;

    public PlayerSelectorTarget(Selector selector){
        this.selector = selector;
    }

    @Override
    public List<Player> getTargets(TriggerContext context) {
        return selector.selectOnlyPlayers(context);
    }

    @Override
    public String toString() {
        return "(Player from selector) " + selector.getSelector();
    }
}
