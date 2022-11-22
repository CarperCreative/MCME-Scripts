package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.condition.targeted.VirtualEntityTargetedCondition;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;

public class TalkCondition extends VirtualEntityTargetedCondition {

    public TalkCondition(VirtualEntityEventTarget talkerTarget) {
        super(talkerTarget, (VirtualEntity::isTalking));
   }
}
