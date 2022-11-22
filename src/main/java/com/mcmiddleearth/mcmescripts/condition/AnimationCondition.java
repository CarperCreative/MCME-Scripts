package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.mcmescripts.condition.targeted.VirtualEntityTargetedCondition;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;

public class AnimationCondition extends VirtualEntityTargetedCondition {

    private final String currentAnimation;
    private final Boolean manualAnimationControl;
    private final Boolean manualOverride;
    private final Boolean instantAnimationSwitching;

    public AnimationCondition(VirtualEntityEventTarget target, String currentAnimation,
                              Boolean manualAnimationControl,
                              Boolean instantAnimationSwitching, Boolean manualOverride) {
        super(target, entity -> {
            if(entity instanceof BakedAnimationEntity) {
                return (currentAnimation == null || currentAnimation.equalsIgnoreCase(((BakedAnimationEntity)entity).getCurrentAnimation().getName()))
                    && (manualAnimationControl == null || manualAnimationControl == ((BakedAnimationEntity) entity).isManualAnimationControl())
                    && (instantAnimationSwitching == null || instantAnimationSwitching == ((BakedAnimationEntity) entity).isInstantAnimationSwitching())
                    && (manualOverride == null || manualOverride == ((BakedAnimationEntity)entity).isManualOverride());
            } else {
                return false;
            }
        });
        this.currentAnimation = currentAnimation;
        this.manualAnimationControl = manualAnimationControl;
        this.instantAnimationSwitching = instantAnimationSwitching;
        this.manualOverride = manualOverride;
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().indent()
                .addLine("Current Animation: "+(currentAnimation!=null?currentAnimation:"--ignore--"))
                .addLine("Manual Animation: "+(manualAnimationControl!=null?manualAnimationControl:"--ignore--"))
                .addLine("Manual Override: "+(manualOverride!=null?manualOverride:"--ignore--"))
                .addLine("Instant Switching: "+(instantAnimationSwitching!=null?instantAnimationSwitching:"--ignore--"))
                .outdent();
    }

}
