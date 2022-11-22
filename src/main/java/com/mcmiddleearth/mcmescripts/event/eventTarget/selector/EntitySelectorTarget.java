package com.mcmiddleearth.mcmescripts.event.eventTarget.selector;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public class EntitySelectorTarget extends EntityEventTarget {

    private final Selector selector;

    public EntitySelectorTarget(Selector selector){
        this.selector = selector;
    }

    @Override
    public List<McmeEntity> getTargets(TriggerContext context) {
        return selector.selectAll(context);
    }

    @Override
    public String toString() {
        return "(Entity from selector) " + selector.getSelector();
    }
}
