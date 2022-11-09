package com.mcmiddleearth.mcmescripts.event.position;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.logging.Logger;

public class EntityCopyHeadPosition extends EventPosition {

    private final EntityEventTarget copyEntity;

    public EntityCopyHeadPosition(EntityEventTarget copyEntity, Vector offset, EEventPositionOffsetType offsetType) {
        super(offset, offsetType);
        this.copyEntity = copyEntity;
    }

    @Override
    public Vector getPosition(TriggerContext context) {

        List<McmeEntity> targets = copyEntity.getTargets(context);

        if(targets.size() > 0){
            McmeEntity entity = targets.get(0);

            Vector position = entity.getHeadPosition();
            position.add(getOffset(entity.getHeadPitch(),entity.getHeadYaw()));
            return position;
        }
        return null;
    }
}
