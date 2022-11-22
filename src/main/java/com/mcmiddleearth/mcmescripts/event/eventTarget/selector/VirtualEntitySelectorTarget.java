package com.mcmiddleearth.mcmescripts.event.eventTarget.selector;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public class VirtualEntitySelectorTarget extends VirtualEntityEventTarget {

    private final Selector selector;

    public VirtualEntitySelectorTarget(Selector selector){
        this.selector = selector;
    }

    @Override
    public List<VirtualEntity> getTargets(TriggerContext context) {
        return selector.selectOnlyVirtualEntities(context);
    }

    @Override
    public String toString() {
        return "(Virtual Entity from selector) " + selector.getSelector();
    }
}
