package com.mcmiddleearth.mcmescripts.bossbattle.unused;

import java.util.*;

/**
 * BossBattleInstance contains state for a boss battle. This references a stateless
 * Instance of a boss battle which contains events
 **/

public class BossBattleInstance {//implements ITriggerContainer {

    /*private final BossBattleElement sourceBossBattle;

    private TimelineElement currentTimeline;
    private TimelineSlotElement currentTimelineSlot;
    private int currentTimelineSlotIndex;

    private final List<Trigger> currentEvents = new ArrayList<>();

    private final Map<String, AbstractTag<?>> tags = new HashMap<>();


    public BossBattleInstance(BossBattleElement sourceBossBattle){
        this.sourceBossBattle = sourceBossBattle;
        updateActiveTimeline(sourceBossBattle.getTimelines().get(0),0);
    }

    private void queueNextTimelineSlot(){

        TimelineElement targetTimeline = currentTimeline;
        int targetTimeSlotIndex = currentTimelineSlotIndex + 1;

        // If the current timeline slot is the last timeline slot of the timeline, the timeline's end behaviour determines what happens next
        if(currentTimelineSlotIndex >= currentTimeline.getTimelineSlots().size()){

            switch ((EEndOfTimelineBehaviour) currentTimeline.getEndOfTimelineBehaviour()){
                case CONTINUE       -> targetTimeSlotIndex = currentTimelineSlotIndex;
                case LOOP           -> targetTimeSlotIndex = 0;
                case GO_TO_TIMELINE -> {
                    targetTimeline = currentTimeline.getEndOfTimelineTimeline();
                    targetTimeSlotIndex = 0;
                }
            }
        }

        // Set up an event for switching the current timeline
        long time = System.currentTimeMillis() + (long) currentTimelineSlot.getDuration();
        DecisionTreeTrigger trigger = new OnceRealTimeTrigger(new TimelineSetAction(targetTimeline,targetTimeSlotIndex,this), time);
        trigger.setCallOnce(true);
    }

    public void updateActiveTimeline(TimelineElement targetTimeline, int targetSlotIndex) {
        currentTimeline = targetTimeline;
        currentTimelineSlotIndex = targetSlotIndex;
        currentTimelineSlot = currentTimeline.getTimelineSlots().get(currentTimelineSlotIndex);
        queueNextTimelineSlot();
    }

    public void updateCurrentEvents(){
        currentEvents.clear();
        for (Event e : currentTimelineSlot.getEntityBehaviors().get(0).getEvents()) {
            // Map<Class<? extends floo.network.elements.events.triggers.Trigger>,Class<? extends Trigger>> triggers = new HashMap<>();
            // triggers.put(AnimationChangedTrigger.class,AnimationChangeTrigger.class);
            //Map<Class<? extends floo.network.elements.events.triggers.Trigger>, Callable<Trigger>> triggers = new HashMap<>();
            //triggers.put(AnimationChangedTrigger.class,() -> new AnimationChangeTrigger(null,"",""));

            Trigger trigger = null;

            if(e.getTrigger() instanceof floo.network.elements.events.triggers.triggers.PlayerJoinTrigger) {
                trigger = new PlayerJoinTrigger(null);
            } else if(e.getTrigger() instanceof floo.network.elements.events.triggers.triggers.PlayerQuitTrigger) {
                trigger = new PlayerQuitTrigger(null);
            } else if(e.getTrigger() instanceof floo.network.elements.events.triggers.triggers.RealPeriodicTrigger) {
                floo.network.elements.events.triggers.triggers.RealPeriodicTrigger triggerElement = (floo.network.elements.events.triggers.triggers.RealPeriodicTrigger) e.getTrigger();
                trigger = new PeriodicRealTimeTrigger(null,triggerElement.getInterval());
            } else if(e.getTrigger() instanceof floo.network.elements.events.triggers.triggers.ServerPeriodicTrigger) {
                floo.network.elements.events.triggers.triggers.ServerPeriodicTrigger triggerElement = (floo.network.elements.events.triggers.triggers.ServerPeriodicTrigger) e.getTrigger();
                trigger = new PeriodicServerTimeTrigger(null,triggerElement.getInterval());
            } else if(e.getTrigger() instanceof floo.network.elements.events.triggers.triggers.RealTimedTrigger) {
                floo.network.elements.events.triggers.triggers.RealTimedTrigger triggerElement = (floo.network.elements.events.triggers.triggers.RealTimedTrigger) e.getTrigger();
                trigger = new OnceRealTimeTrigger(null,triggerElement.getInterval());
                trigger.setCallOnce(true);
            } else if(e.getTrigger() instanceof floo.network.elements.events.triggers.triggers.ServerTimedTrigger) {
                floo.network.elements.events.triggers.triggers.ServerTimedTrigger triggerElement = (floo.network.elements.events.triggers.triggers.ServerTimedTrigger) e.getTrigger();
                trigger = new OnceServerTimeTrigger(null,triggerElement.getInterval());
                trigger.setCallOnce(true);
            }

            List<Action> actions;

            for (Action a : e.getActions()) {
                Action action = null;

                if(a instanceof floo.network.elements.events.actions.actions.GoToTimelineAction) {
                    floo.network.elements.events.actions.actions.GoToTimelineAction actionElement = (floo.network.elements.events.actions.actions.GoToTimelineAction) a;
                    //action = new TimelineSetAction(actionElement);
                }
            }
        }
    }

    public void setTag(String name, String value) {
        tags.put(name, new StringTag(name, value));
    }

    public void setTag(AbstractTag<?> tag) {
        tags.put(tag.getName(), tag);
    }

    public void deleteTag(String name) {
        tags.remove(name);
    }

    public boolean hasTag(String name) {
        return tags.containsKey(name);
    }

    @Override
    public String getName() {
        return sourceBossBattle.getName();
    }

    @Override
    public void addTrigger(Trigger trigger) {
        currentEvents.add(trigger);
    }

    @Override
    public void removeTrigger(Trigger trigger) {
        currentEvents.remove(trigger);
    }

    @Override
    public Set<Trigger> getTriggers() {
        return null;
    }*/
}
