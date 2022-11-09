package com.mcmiddleearth.mcmescripts.event.rotation;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class EntityLookAtRotation extends EventRotation {

    private final EntityEventTarget copyEntity;
    private final EventPosition ownPosition;

    public EntityLookAtRotation(EntityEventTarget copyEntity, EventPosition ownPosition, float offsetPitch, float offsetYaw) {
        super(offsetPitch, offsetYaw);
        this.copyEntity = copyEntity;
        this.ownPosition = ownPosition;
    }

    @Override
    public Rotation getRotation(TriggerContext context) {
        List<McmeEntity> targets = copyEntity.getTargets(context);

        if(targets.size() > 0) {
            McmeEntity entity = targets.get(0);
            Vector startPosition = ownPosition.getPosition(context);
            Vector lookAtPosition = entity.getLocation().toVector();

            Vector dir = startPosition.subtract(lookAtPosition).normalize();

            // Hack to get pitch / yaw
            Location location = new Location(null,0,0,0);
            location.setDirection(dir);
            Rotation rotation = new Rotation(location.getPitch(),location.getYaw());
            applyOffset(rotation);
            return rotation;
        }
        return null;
    }
}
