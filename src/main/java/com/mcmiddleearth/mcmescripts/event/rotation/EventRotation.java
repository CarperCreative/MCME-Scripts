package com.mcmiddleearth.mcmescripts.event.rotation;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

public abstract class EventRotation {

    private final float offsetPitch;
    private final float offsetYaw;

    public EventRotation(float offsetPitch, float offsetYaw){
        this.offsetPitch = offsetPitch;
        this.offsetYaw = offsetYaw;
    }

    protected void applyOffset(Rotation rotation){
        rotation.setPitch(rotation.getPitch()+offsetPitch);
        rotation.setYaw(rotation.getYaw()+offsetYaw);
    }

    public abstract Rotation getRotation(TriggerContext context);
}
