package com.mcmiddleearth.mcmescripts.event.eventTarget.context;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public class VirtualEntityContextTarget extends VirtualEntityEventTarget {

    private final String contextKey;

    public VirtualEntityContextTarget(String contextKey){
        this.contextKey = contextKey;
    }

    @Override
    public List<VirtualEntity> getTargets(TriggerContext context) {
        return context.getVirtualEntityContext(contextKey);
    }

    @Override
    public String toString() {
        return "(Virtual entity from context) " + contextKey;
    }
}
