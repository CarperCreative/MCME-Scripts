package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.condition.targeted.VirtualEntityTargetedCondition;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;

public class TalkCondition extends VirtualEntityTargetedCondition {

    public TalkCondition(VirtualEntityEventTarget talkerTarget) {
        super(talkerTarget, (VirtualEntity::isTalking));
   }
}
