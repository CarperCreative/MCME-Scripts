package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.composite.CompositeEntity;
import com.mcmiddleearth.entities.entities.composite.WingedFlightEntity;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class SetTurningSpeedAction extends VirtualEntityTargetedAction {

    public SetTurningSpeedAction(VirtualEntityEventTarget target, float turningSpeed) {
        super(target, (entity, context) -> {
            if(entity instanceof CompositeEntity){

                ((CompositeEntity) entity).setMaxRotationStep(turningSpeed);

                if(entity instanceof WingedFlightEntity){
                    ((WingedFlightEntity) entity).setMaxRotationStepFlight(turningSpeed);
                }
            }
        });
    }
}