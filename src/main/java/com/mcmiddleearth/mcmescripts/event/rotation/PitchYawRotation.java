package com.mcmiddleearth.mcmescripts.event.rotation;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

public class PitchYawRotation extends EventRotation {

    private final float pitch;
    private final float yaw;

    public PitchYawRotation(float pitch, float yaw) {
        super(0,0);
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public Rotation getRotation(TriggerContext context) {
        Rotation rotation = new Rotation(pitch,yaw);
        applyOffset(rotation);
        return rotation;
    }
}
