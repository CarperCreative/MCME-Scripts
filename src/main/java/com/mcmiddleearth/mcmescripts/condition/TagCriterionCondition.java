package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.quest.tags.AbstractTag;
import com.mcmiddleearth.mcmescripts.quest.tags.IntegerTag;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class TagCriterionCondition extends Condition {

    private final Criterion test;
    private final String tagName;

    public TagCriterionCondition(String tagName, Criterion test) {
        this.test = test;
        this.tagName = tagName;
    }

    @Override
    public boolean test(TriggerContext context) {

        AbstractTag<?> tag = context.getTagContainer().getTag(tagName);

        if(tag == null){
            context.getDescriptor().addLine("Tag is null").outdent();
            return false;
        } else if(tag instanceof IntegerTag){
            int value = ((IntegerTag) tag).getValue();
            boolean result = test.apply(value);
            context.getDescriptor().addLine("Test result: "+result).outdent();
            return result;
        }
        return false;
    }

    @Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().indent()
                .addLine("Tag Name: " + tagName)
                .addLine("Criterion: "+test.getComparator()+test.getLimit())
                .outdent();
    }

}
