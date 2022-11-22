package com.mcmiddleearth.mcmescripts.event.eventTarget;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;

public interface IEventTarget<T> {

    public abstract List<T> getTargets(TriggerContext context);
    public abstract String toString();
}
