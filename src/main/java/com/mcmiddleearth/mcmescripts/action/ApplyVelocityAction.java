package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.EntityTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.rotation.EventRotation;
import com.mcmiddleearth.mcmescripts.event.rotation.Rotation;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;

import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class ApplyVelocityAction extends EntityTargetedAction {

    public ApplyVelocityAction(EntityEventTarget target, EventRotation rotation, double magnitude, boolean override){
        super(target, (entity, context) -> {
            Location tmp = entity.getLocation().clone();
            Rotation direction = rotation.getRotation(context);
            tmp.setYaw(direction.getYaw());
            tmp.setPitch(direction.getPitch());
            Logger.getGlobal().warning("Direction: " + tmp.getDirection());
            Logger.getGlobal().warning("Force: " + tmp.getDirection().multiply(magnitude));
            entity.setVelocity(override ? entity.getVelocity().add(tmp.getDirection().multiply(magnitude)) : tmp.getDirection().multiply(magnitude));
        });
    }
}
