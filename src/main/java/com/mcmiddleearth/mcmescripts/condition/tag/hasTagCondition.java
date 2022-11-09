package com.mcmiddleearth.mcmescripts.condition.tag;

import com.mcmiddleearth.mcmescripts.ITagContainer;
import com.mcmiddleearth.mcmescripts.condition.quest.QuestCondition;
import com.mcmiddleearth.mcmescripts.quest.Quest;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class hasTagCondition extends TagCondition {

    private final String tagName;

    public hasTagCondition(String tagName) {
        this.tagName = tagName;
    }

    @Override
    protected boolean test(ITagContainer tagContainer, TriggerContext context) {
        return tagContainer.hasTag(tagName);
    }
}
