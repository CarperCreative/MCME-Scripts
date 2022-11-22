package com.mcmiddleearth.mcmescripts.event.eventTarget;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public abstract class VirtualEntityEventTarget implements IEventTarget<VirtualEntity> {
    public abstract List<VirtualEntity> getTargets(TriggerContext context);

    public abstract String toString();
}
