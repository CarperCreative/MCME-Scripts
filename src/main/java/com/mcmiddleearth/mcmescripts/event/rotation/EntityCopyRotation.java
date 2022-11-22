package com.mcmiddleearth.mcmescripts.event.rotation;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public class EntityCopyRotation extends EventRotation {

    private final EntityEventTarget copyEntity;

    public EntityCopyRotation(EntityEventTarget copyEntity, float offsetPitch, float offsetYaw) {
        super(offsetPitch, offsetYaw);
        this.copyEntity = copyEntity;
    }

    @Override
    public Rotation getRotation(TriggerContext context) {
        List<McmeEntity> targets = copyEntity.getTargets(context);

        if(targets.size() > 0) {
            McmeEntity entity = targets.get(0);
            Rotation rotation = new Rotation(entity.getPitch(),entity.getYaw());
            applyOffset(rotation);
            return rotation;
        }
        return null;
    }
}
