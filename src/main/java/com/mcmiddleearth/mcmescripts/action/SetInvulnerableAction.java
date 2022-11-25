package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.EntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;

public class SetInvulnerableAction extends EntityTargetedAction {

    public SetInvulnerableAction(EntityEventTarget target, boolean invulnerable) {
        super(target, (entity, context) -> entity.setInvulnerable(invulnerable));
    }
}