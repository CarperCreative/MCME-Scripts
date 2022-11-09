package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.target.IEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class CriterionCondition extends Condition {

    private final Criterion test;
    private final IEventTarget<?> target;

    public CriterionCondition(IEventTarget<?> target, Criterion test) {
        this.test = test;
        this.target = target;
    }

    @Override
    public boolean test(TriggerContext context) {
        int size = target.getTargets(context).size();
        context.getDescriptor()
                .addLine("Condition description: ").indent().add(getDescriptor()).outdent()
                .addLine("Selected quantity: "+size);
        boolean result = test.apply(size);
        context.getDescriptor().addLine("Test result: "+result).outdent();
        return result;
    }

    @Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().indent()
                .addLine("target: " + target.toString())
                .addLine("Criterion: "+test.getComparator()+test.getLimit())
                .outdent();
    }

}
