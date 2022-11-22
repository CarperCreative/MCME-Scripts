package com.mcmiddleearth.mcmescripts.event.eventTarget.selector;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public class VirtualEntitySelectorTarget extends VirtualEntityEventTarget {

    private final VirtualEntitySelector selector;

    public VirtualEntitySelectorTarget(VirtualEntitySelector selector){
        this.selector = selector;
    }

    @Override
    public List<VirtualEntity> getTargets(TriggerContext context) {
        return selector.select(context);
    }

    @Override
    public String toString() {
        return "(Virtual Entity from selector) " + selector.getSelector();
    }
}
