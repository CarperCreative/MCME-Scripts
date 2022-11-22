package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.entities.ai.goal.GoalType;
import com.mcmiddleearth.mcmescripts.condition.targeted.VirtualEntityTargetedCondition;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;

public class GoalTypeCondition extends VirtualEntityTargetedCondition {

    private final GoalType goalType;
    private final boolean exclude;

    public GoalTypeCondition(VirtualEntityEventTarget target, GoalType goalType, boolean exclude) {
        super(target, entity -> (entity.getGoal() != null ? entity.getGoal().getType().equals(goalType) != exclude :
                                                              (goalType == null) != exclude));
        this.goalType = goalType;
        this.exclude = exclude;
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().indent()
                .addLine("Goal Type: "+goalType.name()).addLine("Negate: "+exclude)
                .outdent();
    }

}

