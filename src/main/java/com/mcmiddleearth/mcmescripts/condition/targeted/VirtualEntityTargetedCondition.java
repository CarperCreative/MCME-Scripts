package com.mcmiddleearth.mcmescripts.condition.targeted;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.function.Function;

public class VirtualEntityTargetedCondition extends Condition {

    private boolean matchAllSelected = false;

    private final VirtualEntityEventTarget target;

    private final Function<VirtualEntity,Boolean> test;

    public VirtualEntityTargetedCondition(VirtualEntityEventTarget target, Function<VirtualEntity, Boolean> test) {
        this.test = test;
        this.target = target;
    }

    @Override
    public boolean test(TriggerContext context) {
        boolean result = matchAllSelected;
        context.getDescriptor().add(super.getDescriptor()).indent();
        for(VirtualEntity element : target.getTargets(context)) {
            context.getDescriptor().addLine("Testing virtual entity: "+element.getName());
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
                .addLine("Target: " + target.toString())
                .addLine("Match All: "+matchAllSelected)
                .outdent();
    }

}
