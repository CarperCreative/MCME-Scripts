package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;

public class EntityStateAction extends VirtualEntityTargetedAction {

    public EntityStateAction(VirtualEntityEventTarget target, String state) {
        super(target, (entity, context) -> {
            if(entity instanceof BakedAnimationEntity) {
                ((BakedAnimationEntity)entity).setState(state);
            }
        });
        getDescriptor().indent().addLine("State: "+state).outdent();
    }
}
