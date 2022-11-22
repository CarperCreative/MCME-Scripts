package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.api.MovementType;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;

public class SetMovementTypeAction extends VirtualEntityTargetedAction {

    public SetMovementTypeAction(VirtualEntityEventTarget target, MovementType movementType) {
        super(target, (entity,context) -> {
            entity.setMovementType(movementType);
        });
        getDescriptor().indent()
                .addLine("Movement type: "+movementType);
    }
}
