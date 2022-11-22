package com.mcmiddleearth.mcmescripts.event.position;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.util.Vector;

import java.util.List;

public class EntityCopyPosition extends EventPosition {

    private final EntityEventTarget copyEntity;

    public EntityCopyPosition(EntityEventTarget copyEntity, Vector offset, EEventPositionOffsetType offsetType) {
        super(offset, offsetType);
        this.copyEntity = copyEntity;
    }

    @Override
    public Vector getPosition(TriggerContext context) {

        List<McmeEntity> targets = copyEntity.getTargets(context);

        if(targets.size() > 0){
            McmeEntity entity = targets.get(0);
            Vector position = entity.getLocation().toVector();
            position.add(getOffset(entity.getLocation().getPitch(),entity.getLocation().getYaw()));

            return position;
        }
        return null;
    }
}
