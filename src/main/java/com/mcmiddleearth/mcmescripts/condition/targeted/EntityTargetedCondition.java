package com.mcmiddleearth.mcmescripts.condition.targeted;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.function.Function;

public class EntityTargetedCondition extends Condition {

    private boolean matchAllSelected = false;

    private final Selector selector;

    private final Function<McmeEntity,Boolean> test;

    public EntityTargetedCondition(Selector selector, Function<McmeEntity, Boolean> test) {
        this.test = test;
        this.selector = selector;
    }

    @Override
    public boolean test(TriggerContext context) {
        boolean result = matchAllSelected;
        context.getDescriptor().add(super.getDescriptor()).indent();
        for(McmeEntity element : selector.selectAll(context)) {
            context.getDescriptor().addLine("Testing McmeEntity: "+(element.getName()));

            if(matchAllSelected && !test.apply(element)) {
                result = false;
                break;
            } else if(!matchAllSelected && test.apply(element)) {
                result = true;
                break;
            }
        }
        context.getDescriptor().addLine("Test result: "+result).outdent();
        return result;
    }

    public void setMatchAllSelected(boolean matchAllSelected) {
        this.matchAllSelected = matchAllSelected;
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().indent()
                .addLine("Selector: "+selector.getSelector())
                .addLine("Match All: "+matchAllSelected)
                .outdent();
    }

}
