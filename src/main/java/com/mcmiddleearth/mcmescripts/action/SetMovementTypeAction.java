package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.api.MovementType;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.EventGoalFactory;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;

public class SetMovementTypeAction extends VirtualEntityTargetedAction {

    public SetMovementTypeAction(VirtualEntityEventTarget target, MovementType movementType) {
        super(target, (entity,context) -> {
            entity.setMovementType(movementType);
        });
        getDescriptor().indent()
                .addLine("Movement type: "+movementType);
    }
}
