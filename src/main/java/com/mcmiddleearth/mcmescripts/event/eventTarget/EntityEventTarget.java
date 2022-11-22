package com.mcmiddleearth.mcmescripts.event.eventTarget;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public abstract class EntityEventTarget implements IEventTarget<McmeEntity> {
    public abstract List<McmeEntity> getTargets(TriggerContext context);

    public abstract String toString();
}
