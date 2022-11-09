package com.mcmiddleearth.mcmescripts.action.bossbattle;

import com.mcmiddleearth.mcmescripts.bossbattle.BossBattle;
import com.mcmiddleearth.mcmescripts.bossbattle.TimelineConfiguration;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class GoToTimelineAction extends BossBattleAction{

    private final String targetTimelineName;
    private final int targetSlotIndex;

    public GoToTimelineAction(TimelineConfiguration timelineConfiguration) {
        this.targetTimelineName = timelineConfiguration.getTimelineName();
        this.targetSlotIndex = timelineConfiguration.getTimeslotIndex();
        getDescriptor().indent().addLine("Timeline: " + targetTimelineName).addLine("Slot: " + targetSlotIndex).outdent();
    }

    @Override
    protected void handler(BossBattle instance, TriggerContext context) {
        instance.updateActiveTimeline(targetTimelineName,targetSlotIndex);
    }
}
