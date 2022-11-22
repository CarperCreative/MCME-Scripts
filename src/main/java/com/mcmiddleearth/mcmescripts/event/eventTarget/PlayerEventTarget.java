package com.mcmiddleearth.mcmescripts.event.eventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class PlayerEventTarget implements IEventTarget<Player> {
    public abstract List<Player> getTargets(TriggerContext context);

    public abstract String toString();
}
