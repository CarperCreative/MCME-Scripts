package com.mcmiddleearth.mcmescripts.action.targeted;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;

public class PlayerTargetedAction extends Action {

    private final PlayerEventTarget target;

    private final BiConsumer<Player,TriggerContext> executor;

    public PlayerTargetedAction(PlayerEventTarget target, BiConsumer<Player,TriggerContext> executor) {
        this.target = target;
        this.executor = executor;
        //getDescriptor().indent().addLine("Target: " +target.getSelector()).outdent();
    }

    @Override
    protected void handler(TriggerContext context) {
        List<Player> selected = target.getTargets(context);

        selected.forEach(element -> {
                context.getDescriptor().addLine("Targeting: " + element.getName());
            context.getDescriptor().indent();
            executor.accept(element,context);
            context.getDescriptor().outdent();
        });
    }
}
