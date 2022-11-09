package com.mcmiddleearth.mcmescripts.event.position;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TriggerContextPosition extends EventPosition{

    private final String key;

    public TriggerContextPosition(String key, Vector offset, EEventPositionOffsetType offsetType) {
        super(offset,offsetType);
        this.key = key;
    }

    @Override
    public Vector getPosition(TriggerContext context) {
        Vector contextPosition = context.getPositionContext(key);
        if(contextPosition != null){
            return contextPosition.add(getOffset());
        }
        return null;
    }
}
