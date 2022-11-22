package com.mcmiddleearth.mcmescripts.event.eventTarget.context;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityContextTarget extends EntityEventTarget {

    private final String contextKey;

    public EntityContextTarget(String contextKey){
        this.contextKey = contextKey;
    }

    @Override
    public List<McmeEntity> getTargets(TriggerContext context) {

        List<McmeEntity> result = new ArrayList<>();

        result.addAll(context.getVirtualEntityContext(contextKey));
        result.addAll(context.getPlayerContext(contextKey)
                .stream().map(EntitiesPlugin.getEntityServer().getPlayerProvider()::getOrCreateMcmePlayer)
                .collect(Collectors.toList()));

        return result;
    }

    @Override
    public String toString() {
        return "(Entity from context) " + contextKey;
    }
}
