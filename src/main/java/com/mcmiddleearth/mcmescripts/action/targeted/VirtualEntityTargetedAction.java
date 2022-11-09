package com.mcmiddleearth.mcmescripts.action.targeted;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;
import java.util.function.BiConsumer;

public class VirtualEntityTargetedAction extends Action {

    private final VirtualEntityEventTarget target;

    private final BiConsumer<VirtualEntity,TriggerContext> executor;

    public VirtualEntityTargetedAction(VirtualEntityEventTarget target, BiConsumer<VirtualEntity,TriggerContext> executor) {
        this.target = target;
        this.executor = executor;
    }

    @Override
    protected void handler(TriggerContext context) {
        List<VirtualEntity> selected = target.getTargets(context);

        selected.forEach(element -> {
                context.getDescriptor().addLine("Targeting: " + element.getName());
            context.getDescriptor().indent();
            executor.accept(element,context);
            context.getDescriptor().outdent();
        });
    }
}
