package com.mcmiddleearth.mcmescripts.event.rotation;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

public class TriggerContextRotation extends EventRotation {

    private final String key;

    public TriggerContextRotation(String key, float offsetPitch, float offsetYaw) {
        super(offsetPitch,offsetYaw);
        this.key = key;
    }

    @Override
    public Rotation getRotation(TriggerContext context) {
        Rotation contextRotation = context.getRotationContext(key);
        if(contextRotation != null){
            applyOffset(contextRotation);
            return contextRotation;
        }
        return null;
    }
}
