package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;
import com.mcmiddleearth.mcmescripts.event.target.context.VirtualEntityContextTarget;

public class AnimationAction extends VirtualEntityTargetedAction {

    public AnimationAction(VirtualEntityEventTarget target, String animation, boolean override) {
        super(target, (entity, context) -> {
            if(entity instanceof BakedAnimationEntity) {
                ((BakedAnimationEntity)entity).setAnimation(animation, override,null,0);
            }
        });
        getDescriptor().indent().addLine("Animation: "+animation).addLine("Override: "+override).outdent();
    }

}