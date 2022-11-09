package com.mcmiddleearth.mcmescripts.event;

import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.rotation.EventRotation;
import com.mcmiddleearth.mcmescripts.event.rotation.Rotation;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EventGoalFactory {
    private final VirtualEntityGoalFactory factory;
    private final EntityEventTarget eventGoalTarget;
    private final EventPosition eventGoalPosition;
    private final EventRotation eventGoalRotation;

    private final List<EventPosition> checkpoints;

    public EventGoalFactory(VirtualEntityGoalFactory factory, EntityEventTarget eventGoalTarget, EventPosition eventGoalPosition, EventRotation eventGoalRotation, List<EventPosition> checkpoints){
        this.factory = factory;
        this.eventGoalTarget = eventGoalTarget;
        this.eventGoalPosition = eventGoalPosition;
        this.checkpoints = checkpoints;
        this.eventGoalRotation = eventGoalRotation;
    }

    public VirtualEntityGoalFactory getFactory(TriggerContext context){

        List<McmeEntity> targets = eventGoalTarget != null ? eventGoalTarget.getTargets(context) : new ArrayList<>();

        Vector position = eventGoalPosition != null ? eventGoalPosition.getPosition(context) : null;
        Rotation rotation = eventGoalRotation != null ? eventGoalRotation.getRotation(context) : null;

        Location location = position != null ? new Location(context.getWorld(), position.getX(), position.getY(), position.getZ()) : null;

        if(rotation != null && location != null){
            location.setPitch(rotation.getPitch());
            location.setYaw(rotation.getYaw());
        }

        Location[] checkpointsArray = null;

        if(checkpoints != null && checkpoints.size() > 0){
            checkpointsArray = new Location[checkpoints.size()];
            for (int i = 0; i < checkpoints.size(); i++) {
                Vector pos = checkpoints.get(i).getPosition(context);
                checkpointsArray[i] = new Location(context.getWorld(), pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return factory
                .withTargetEntity(!targets.isEmpty() ? targets.get(0) : null)
                .withTargetLocation(location)
                .withCheckpoints(checkpointsArray);
    }
}