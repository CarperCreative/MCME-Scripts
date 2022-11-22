package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.condition.targeted.VirtualEntityTargetedCondition;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;

public class NoTalkCondition extends VirtualEntityTargetedCondition {

    public NoTalkCondition(VirtualEntityEventTarget talkerTarget) {
        super(talkerTarget, (entity -> !entity.isTalking()));
   }
}
