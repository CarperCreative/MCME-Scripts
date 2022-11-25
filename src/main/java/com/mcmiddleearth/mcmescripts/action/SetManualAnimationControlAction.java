package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;

public class SetManualAnimationControlAction extends VirtualEntityTargetedAction {

    public SetManualAnimationControlAction(VirtualEntityEventTarget target,boolean manual) {
        super(target, (entity, context) -> {
            if(entity instanceof BakedAnimationEntity) {
                ((BakedAnimationEntity)entity).setManualAnimationControl(manual);
            }
        });
    }

}