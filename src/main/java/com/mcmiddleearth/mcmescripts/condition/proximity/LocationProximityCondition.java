package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.condition.DoubleCriterion;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;
import java.util.logging.Logger;

public class LocationProximityCondition extends Condition {

    private final EventPosition position;
    private final EntityEventTarget target;
    private final DoubleCriterion test;

    public LocationProximityCondition(EntityEventTarget target, EventPosition position, DoubleCriterion test) {
        this.target = target;
        this.position = position;
        this.test = test;
    }

    @Override
    public boolean test(TriggerContext context) {
        context.getDescriptor().addLine(this.getClass().getSimpleName()).indent();
        List<McmeEntity> entities =target.getTargets(context);
        if(entities.size() > 0){
            return test.apply(entities.get(0).getLocation().toVector().distance(position.getPosition(context)));
        }
        Logger.getGlobal().warning("No target found");
        return false;
    }
}
