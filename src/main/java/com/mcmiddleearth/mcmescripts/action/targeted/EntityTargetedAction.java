package com.mcmiddleearth.mcmescripts.action.targeted;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;
import java.util.function.BiConsumer;

public class EntityTargetedAction extends Action {

    private final EntityEventTarget target;

    private final BiConsumer<McmeEntity,TriggerContext> executor;

    public EntityTargetedAction(EntityEventTarget target, BiConsumer<McmeEntity,TriggerContext> executor) {
        this.target = target;
        this.executor = executor;
    }

    @Override
    protected void handler(TriggerContext context) {
        List<McmeEntity> selected = target.getTargets(context);

        selected.forEach(element -> {
                context.getDescriptor().addLine("Targeting: " + element.getName());
            context.getDescriptor().indent();
            executor.accept(element,context);
            context.getDescriptor().outdent();
        });
    }
}
