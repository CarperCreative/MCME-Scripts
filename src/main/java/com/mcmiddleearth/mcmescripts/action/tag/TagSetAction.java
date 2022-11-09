package com.mcmiddleearth.mcmescripts.action.tag;

import com.mcmiddleearth.mcmescripts.ITagContainer;
import com.mcmiddleearth.mcmescripts.action.quest.QuestAction;
import com.mcmiddleearth.mcmescripts.quest.Quest;
import com.mcmiddleearth.mcmescripts.quest.tags.AbstractTag;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class TagSetAction extends TagAction {

    private final AbstractTag<?> tag;

    public TagSetAction(AbstractTag<?> tag) {
        this.tag = tag;
    }

    @Override
    protected void handler(ITagContainer tagContainer, TriggerContext context) {
        tagContainer.setTag(tag);
    }
}
