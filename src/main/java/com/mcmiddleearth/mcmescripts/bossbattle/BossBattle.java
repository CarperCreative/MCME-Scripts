package com.mcmiddleearth.mcmescripts.bossbattle;

import com.mcmiddleearth.entities.api.EntityAPI;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.IEntityContainer;
import com.mcmiddleearth.mcmescripts.ITagContainer;
import com.mcmiddleearth.mcmescripts.action.bossbattle.GoToTimelineAction;
import com.mcmiddleearth.mcmescripts.quest.tags.AbstractTag;
import com.mcmiddleearth.mcmescripts.trigger.DecisionTreeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.ITriggerContainer;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.OnceRealTimeTrigger;

import java.util.*;
import java.util.logging.Logger;

/**
 * Represents a quest of a party of players. If there are two parties doing the same quest at the same time
 * there will be two quest objects. One for each party.
 *
 * A quest consists of one or several stages. Each stage is a script. If a stage is enabled it can be triggered and loaded
 * like any script depending on given conditions.
 */
public class BossBattle implements ITriggerContainer, IEntityContainer, ITagContainer {

    private final String name;
    private final Map<String,Timeline> timelines = new HashMap<>();
    private final Map<String, AbstractTag<?>> tags = new HashMap<>();

    private final Set<McmeEntity> entities = new HashSet<>();

    private String currentTimelineName;
    private int currentTimelineSlotIndex = 0;

    /**
     * Triggers for switching between timeline slots
     */
    private final Set<Trigger> timelineTriggers = new HashSet<>();

    public BossBattle(String name){
        this.name = name;
    }

    public BossBattle(String name, List<Timeline> timelinesList){
        this.name = name;
        setTimelines(timelinesList);
    }

    public void setTimelines(List<Timeline> timelinesList){
        timelines.clear();
        for (Timeline t : timelinesList) {
            timelines.put(t.getName(),t);
        }
        updateActiveTimeline(timelinesList.get(0).getName(),0);
    }

    public void updateActiveTimeline(String newTimelineName, int newSlotIndex) {
        // Deactivate triggers associated with the old timeline slot
        Timeline currentTimeline = timelines.get(currentTimelineName);
        TimelineSlot currentTimelineSlot = currentTimeline != null ? currentTimeline.getSlots().get(currentTimelineSlotIndex) : null;
        if (currentTimelineSlot != null) currentTimelineSlot.deactivateTriggers();

        // Remove queued timelines
        while (timelineTriggers.size() > 0){
            timelineTriggers.iterator().next().unregister();
        }

        currentTimelineSlotIndex = newSlotIndex;
        currentTimelineName = newTimelineName;

        // Activate triggers associated with the new timeline slot
        timelines.get(currentTimelineName).getSlots().get(currentTimelineSlotIndex).activateTriggers();

        queueNextTimelineConfiguration();
    }

    public void restartTimeline() {
        updateActiveTimeline(currentTimelineName,currentTimelineSlotIndex);
    }

    private void queueNextTimelineConfiguration(){
        Timeline currentTimeline = timelines.get(currentTimelineName);

        // If the current timeline slot is the last timeline slot of the timeline, the timeline's end behaviour determines what happens next
        if(currentTimelineSlotIndex+1 >= currentTimeline.getSlots().size()){

            switch (currentTimeline.getEndOfTimelineBehaviour()){
                case LOOP           -> {
                    int targetTimelineSlotIndex = 0;
                    queueTimelineConfiguration(currentTimelineName,targetTimelineSlotIndex);
                    Logger.getGlobal().info("End of timeline. (LOOP) Queuing timeline configuration for " + getName() + " as Timeline: " + currentTimelineName + ", slot: " + targetTimelineSlotIndex);
                }
                case GO_TO_TIMELINE -> {
                    String targetTimelineName = currentTimeline.getEndOfTimelineNextTimelineName();
                    int targetTimelineSlotIndex = 0;
                    queueTimelineConfiguration(targetTimelineName,targetTimelineSlotIndex);
                    Logger.getGlobal().info("End of timeline. (GO TO) Queuing timeline configuration for " + getName() + " as Timeline: " + targetTimelineName + ", slot: " + targetTimelineSlotIndex);
                }
                case CONTINUE           -> {
                    Logger.getGlobal().info("End of timeline. (CONTINUE) Timeline set as continue for " + getName() + ", no new timeline configuration is queued.");
                }
            }
        } else {
            queueTimelineConfiguration(currentTimelineName,currentTimelineSlotIndex+1);
            Logger.getGlobal().info("Queuing timeline configuration for " + getName() + " as Timeline: " + currentTimelineName + ", slot: " + currentTimelineSlotIndex+1);
        }
    }

    private void queueTimelineConfiguration(String targetTimelineName, int targetTimelineSlotIndex){
        Timeline currentTimeline = timelines.get(currentTimelineName);
        TimelineSlot currentTimelineSlot = currentTimeline.getSlots().get(currentTimelineSlotIndex);

        // Set up an event for switching the current timeline
        long time = System.currentTimeMillis() + (long) (currentTimelineSlot.getDuration() * 1000);
        DecisionTreeTrigger trigger = new OnceRealTimeTrigger(new GoToTimelineAction(new TimelineConfiguration(targetTimelineName,targetTimelineSlotIndex)), time);
        trigger.setName("Queued Timeline Configuration");
        trigger.setCallOnce(true);
        trigger.register(this);
        Logger.getGlobal().info("Queued new timeline configuration for " + getName() + ", in " + (long) (currentTimelineSlot.getDuration()*1000) + " ms.");
    }

    public void setTag(AbstractTag<?> tag) {
        tags.put(tag.getName(), tag);
    }

    @Override
    public AbstractTag<?> getTag(String name) {
        return tags.get(name);
    }

    public void deleteTag(String name) {
        tags.remove(name);
    }

    public boolean hasTag(String name) {
        return tags.containsKey(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addTrigger(Trigger trigger) {
        timelineTriggers.add(trigger);
    }

    @Override
    public void removeTrigger(Trigger trigger) {
        timelineTriggers.remove(trigger);
    }

    @Override
    public IEntityContainer getEntityContainer() {
        return this;
    }

    @Override
    public Set<Trigger> getTriggers() {
        return timelineTriggers;
    }

    @Override
    public void addEntity(McmeEntity entity) {
        entities.add(entity);
    }

    @Override
    public void removeEntity(McmeEntity entity) {
        entities.remove(entity);
    }

    @Override
    public Set<McmeEntity> getEntities() {
        return entities;
    }

    public void unregisterAllTriggers(){
        for(String timelineName : timelines.keySet()){
            timelines.get(timelineName).getSlots().forEach(TimelineSlot::deactivateTriggers);
        }
        unregisterTimelineTriggers();
    }

    public void unregisterTimelineTriggers(){
        // Removing like this to prevent ConcurrentModificationException because trigger.unregister() also removes it from the triggers set
        while (getTriggers().size() > 0){
            getTriggers().iterator().next().unregister();
        }
    }

    public void stop() {
        unregisterAllTriggers();
        for (McmeEntity entity : entities) {
            Logger.getGlobal().warning("Removing entity: " + entity.getName());
            EntityAPI.removeEntity(entity); // TODO: Why doesn't entity.remove() work? What does that do
            //entity.remove();
        }
    }
}
