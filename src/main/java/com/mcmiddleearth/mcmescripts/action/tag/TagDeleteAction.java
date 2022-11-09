package com.mcmiddleearth.mcmescripts.action.tag;

import com.mcmiddleearth.mcmescripts.ITagContainer;
import com.mcmiddleearth.mcmescripts.action.bossbattle.BossBattleAction;
import com.mcmiddleearth.mcmescripts.action.quest.QuestAction;
import com.mcmiddleearth.mcmescripts.quest.Quest;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class TagDeleteAction extends TagAction {

    private final String tagName;

    public TagDeleteAction(String tagName) {
        this.tagName = tagName;
    }

    @Override
    protected void handler(ITagContainer tagContainer, TriggerContext context) {
        tagContainer.deleteTag(tagName);
    }
}
