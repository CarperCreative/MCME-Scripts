package com.mcmiddleearth.mcmescripts.action.tag;

import com.mcmiddleearth.mcmescripts.ITagContainer;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.quest.Quest;
import com.mcmiddleearth.mcmescripts.quest.Stage;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public abstract class TagAction extends Action {

    @Override
    protected final void handler(TriggerContext context) {
        if(context.getTriggerContainer() instanceof ITagContainer || context.getTriggerContainer().getBossBattle() != null) {
            handler(context.getTagContainer(), context);
        }
    }

    protected abstract void handler(ITagContainer tagContainer, TriggerContext context);
}
