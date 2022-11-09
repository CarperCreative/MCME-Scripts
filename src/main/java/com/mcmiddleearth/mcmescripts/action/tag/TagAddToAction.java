package com.mcmiddleearth.mcmescripts.action.tag;

import com.mcmiddleearth.mcmescripts.ITagContainer;
import com.mcmiddleearth.mcmescripts.quest.tags.AbstractTag;
import com.mcmiddleearth.mcmescripts.quest.tags.IntegerTag;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.logging.Logger;

public class TagAddToAction extends TagAction {

    private final String tagName;
    private final int tagValue;

    public TagAddToAction(String name, int value) {
        this.tagName = name;
        this.tagValue = value;
    }

    @Override
    protected void handler(ITagContainer tagContainer, TriggerContext context) {

        AbstractTag<?> tag = tagContainer.getTag(tagName);

        if(tag == null){
            tagContainer.setTag(new IntegerTag(tagName,tagValue));
        } else if(tag instanceof IntegerTag){
            ((IntegerTag)tag).setValue(((IntegerTag) tag).getValue() + tagValue);
        }
    }
}
