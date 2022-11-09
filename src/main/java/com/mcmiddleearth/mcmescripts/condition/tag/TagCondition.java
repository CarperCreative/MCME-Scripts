package com.mcmiddleearth.mcmescripts.condition.tag;

import com.mcmiddleearth.mcmescripts.ITagContainer;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.quest.Quest;
import com.mcmiddleearth.mcmescripts.quest.Stage;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public abstract class TagCondition extends Condition {

    @Override
    public final boolean test(TriggerContext context) {
        if(context.getTriggerContainer() instanceof Stage) {
            return test(context.getTagContainer(),context);
        }
        return false;
    }

    protected abstract boolean test(ITagContainer tagContainer, TriggerContext context);

}
