package com.mcmiddleearth.mcmescripts.bossbattle;

import com.google.gson.JsonElement;
import com.mcmiddleearth.mcmescripts.IEntityContainer;
import com.mcmiddleearth.mcmescripts.compiler.TriggerCompiler;
import com.mcmiddleearth.mcmescripts.trigger.ITriggerContainer;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class TimelineSlot implements ITriggerContainer {

    private final String name;
    private final double duration;

    private boolean isActive;

    private Set<Trigger> triggers = new HashSet<>();
    private Set<Trigger> startTriggers = new HashSet<>();

    private final JsonElement eventsJson;
    private final JsonElement triggerEventsJson;

    private final BossBattle bossBattle;

    public TimelineSlot(String name, BossBattle bossBattle, double duration, JsonElement eventsJson, JsonElement triggerEventsJson) {
        this.bossBattle = bossBattle;
        this.name = name;
        this.duration = duration;
        this.eventsJson = eventsJson;
        this.triggerEventsJson = triggerEventsJson;
    }

    public void activateTriggers(){
        // Triggers must be recompiled every time since not all triggers are stateless.
        if(eventsJson != null) triggers = TriggerCompiler.compileTriggers(eventsJson);

        Logger.getGlobal().info("Activating triggers for timeline slot: " + getName());
        triggers.forEach(trigger -> trigger.register(this));

        if(triggerEventsJson != null) startTriggers = TriggerCompiler.compileTriggers(triggerEventsJson);

        Logger.getGlobal().info("Triggering start triggers for timeline slot: " + getName());
        startTriggers.forEach(trigger -> {
            trigger.setTriggerContainer(this);
            trigger.call(new TriggerContext(trigger));
        });
    }

    public void deactivateTriggers(){
        // Removing like this to prevent ConcurrentModificationException because trigger.unregister() also removes it from the triggers set
        while (getTriggers().size() > 0){
            getTriggers().iterator().next().unregister();
        }
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    @Override
    public void removeTrigger(Trigger trigger) {
        triggers.remove(trigger);
    }

    @Override
    public IEntityContainer getEntityContainer() {
        return bossBattle;
    }

    @Override
    public BossBattle getBossBattle() {
        return bossBattle;
    }

    @Override
    public Set<Trigger> getTriggers() {
        return triggers;
    }

    public double getDuration() {
        return duration;
    }

    public boolean isActive() {
        return isActive;
    }
}
