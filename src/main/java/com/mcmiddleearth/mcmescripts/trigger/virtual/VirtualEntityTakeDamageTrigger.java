package com.mcmiddleearth.mcmescripts.trigger.virtual;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.events.events.McmeEntityDamagedEvent;
import com.mcmiddleearth.entities.events.handler.EntityEventHandler;
import com.mcmiddleearth.entities.events.listener.McmeEventListener;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.EventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.ITriggerContainer;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;
import java.util.logging.Logger;

public class VirtualEntityTakeDamageTrigger extends EventTrigger implements McmeEventListener {

    private final EntityEventTarget target;

    public VirtualEntityTakeDamageTrigger(Action action, EntityEventTarget target) {
        super(action);
        this.target = target;
    }

    @Override
    public void register(ITriggerContainer triggerContainer) {
        super.register(triggerContainer);
        EntitiesPlugin.getEntityServer().registerEvents(MCMEScripts.getInstance(),this);
    }

    @Override
    public void unregister() {
        super.unregister();
        EntitiesPlugin.getEntityServer().unregisterEvents(MCMEScripts.getInstance(),this);
    }

    @SuppressWarnings("unused")
    @EntityEventHandler
    public void onEntityTakeDamage(McmeEntityDamagedEvent event) {
        Logger.getGlobal().warning("Damage event!");
        List<McmeEntity> targets = target.getTargets(new TriggerContext(this));
        if(targets.size() > 0 && targets.get(0) == event.getEntity()){
            TriggerContext context = new TriggerContext(this);
            call(context);
            Logger.getGlobal().warning("Entity took damage!");
        }
    }
}
