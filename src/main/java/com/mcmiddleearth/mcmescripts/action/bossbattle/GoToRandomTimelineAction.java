package com.mcmiddleearth.mcmescripts.action.bossbattle;

import com.mcmiddleearth.mcmescripts.bossbattle.BossBattle;
import com.mcmiddleearth.mcmescripts.bossbattle.TimelineConfiguration;
import com.mcmiddleearth.mcmescripts.looting.LootTableChoice;
import com.mcmiddleearth.mcmescripts.looting.LootTable;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.List;
import java.util.Set;

public class GoToRandomTimelineAction extends BossBattleAction{

    Set<LootTableChoice<TimelineConfiguration>> weightedChoices;

    public GoToRandomTimelineAction(Set<LootTableChoice<TimelineConfiguration>> weightedChoices) {
        this.weightedChoices = weightedChoices;
    }

    @Override
    protected void handler(BossBattle instance, TriggerContext context) {
        LootTable<TimelineConfiguration> lootTable = new LootTable<>(weightedChoices);
        lootTable.select().stream().findFirst().ifPresent(timelineConfiguration -> instance.updateActiveTimeline(timelineConfiguration.getTimelineName(),timelineConfiguration.getTimeslotIndex()));
    }
}
