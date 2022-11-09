package com.mcmiddleearth.mcmescripts.condition.proximity;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.condition.Criterion;
import com.mcmiddleearth.mcmescripts.condition.CriterionCondition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class VirtualEntityProximityCondition extends CriterionCondition {

    private final String entityName;

    public VirtualEntityProximityCondition(EntityEventTarget target, String entityName, Criterion test) {
        super(target,test);
        this.entityName = entityName;
    }

    @Override
    public boolean test(TriggerContext context) {
        context.getDescriptor().addLine(this.getClass().getSimpleName()).indent();
        McmeEntity entity = EntitiesPlugin.getEntityServer().getEntity(entityName);
        if(entity instanceof VirtualEntity) {
            context.getDescriptor()
                    .addLine("Found center entity: "+entity.getName());
        } else {
            context.getDescriptor()
                    .addLine("Found center entity: --none--");
        }
        TriggerContext virtualEntityContext = new TriggerContext(context);
        if(entity instanceof VirtualEntity) {
            virtualEntityContext.withEntity(entity);
        }
        return super.test(virtualEntityContext);
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor()
                .addLine("Entity: "+entityName);
    }

}
