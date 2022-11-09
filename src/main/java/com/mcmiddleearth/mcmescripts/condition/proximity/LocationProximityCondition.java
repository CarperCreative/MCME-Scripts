package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.mcmescripts.condition.Criterion;
import com.mcmiddleearth.mcmescripts.condition.CriterionCondition;
import com.mcmiddleearth.mcmescripts.condition.targeted.EntityTargetedCondition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

public class LocationProximityCondition extends CriterionCondition {

    private final Location location;

    public LocationProximityCondition(EntityEventTarget target, Location center, Criterion test) {
        super(target, test);
        this.location = center;
    }

    @Override
    public boolean test(TriggerContext context) {
        context.getDescriptor().addLine(this.getClass().getSimpleName()).indent();
        return super.test(context);
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor()
                .addLine("Location: " + location);
    }

}
