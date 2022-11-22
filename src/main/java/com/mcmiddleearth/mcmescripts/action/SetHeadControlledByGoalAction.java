package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.entities.entities.composite.CompositeEntity;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;

public class SetHeadControlledByGoalAction extends VirtualEntityTargetedAction {

    public SetHeadControlledByGoalAction(VirtualEntityEventTarget target, boolean headControlledByGoal) {
        super(target, (entity, context) -> {
            if(entity instanceof BakedAnimationEntity) {
                ((CompositeEntity)entity).setGoalHasHeadControl(headControlledByGoal);
            }
        });
    }
}