package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.condition.DoubleCriterion;
import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

public class PlayerProximityCondition extends Condition {

    private final PlayerEventTarget playerTarget;
    private final VirtualEntityEventTarget entityTarget;
    private final DoubleCriterion test;

    public PlayerProximityCondition(PlayerEventTarget playerTarget, VirtualEntityEventTarget entityTarget, DoubleCriterion test) {
        this.playerTarget = playerTarget;
        this.entityTarget = entityTarget;
        this.test = test;
    }

    @Override
    public boolean test(TriggerContext context) {
        context.getDescriptor().addLine(this.getClass().getSimpleName()).indent();
        Player player = playerTarget.getTargets(context).get(0);
        VirtualEntity entity = entityTarget.getTargets(context).get(0);
        if(player!=null) {
            context.withContext("playerTarget",player);
            context.getDescriptor()
                    .addLine("Found player: "+player.getName());
        } else {
            context.getDescriptor()
                    .addLine("Found center player: --none--");
            return false;
        }
        if(entity!=null) {
            context.withContext("entityTarget",entity);
            context.getDescriptor()
                    .addLine("Found entity: "+entity.getName());
        } else {
            context.getDescriptor()
                    .addLine("Found center player: --none--");
            return false;
        }
        return test.apply(player.getLocation().distance(entity.getLocation()));
    }
}
