package com.mcmiddleearth.mcmescripts.event.rotation;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.composite.BakedAnimationEntity;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public class EntityCopyHeadRotation extends EventRotation {

    private final EntityEventTarget copyEntity;

    public EntityCopyHeadRotation(EntityEventTarget copyEntity, float offsetPitch, float offsetYaw) {
        super(offsetPitch, offsetYaw);
        this.copyEntity = copyEntity;
    }

    @Override
    public Rotation getRotation(TriggerContext context) {
        List<McmeEntity> targets = copyEntity.getTargets(context);
        if(targets.size() > 0) {
            McmeEntity entity = targets.get(0);

            Rotation rotation;

            // If entity is baked animation, we want to copy the animated head rotation
            if(entity instanceof BakedAnimationEntity){
                BakedAnimationEntity animationEntity = (BakedAnimationEntity) entity;
                rotation = new Rotation((float)animationEntity.getRealHeadPitch(),(float)animationEntity.getRealHeadYaw());
            } else {
                rotation = new Rotation(entity.getHeadPitch(),entity.getHeadYaw());
            }

            applyOffset(rotation);
            return rotation;
        }
        return null;
    }
}
