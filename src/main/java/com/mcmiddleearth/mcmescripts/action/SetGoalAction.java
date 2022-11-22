package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.EventGoalFactory;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;

public class SetGoalAction extends VirtualEntityTargetedAction {

    public SetGoalAction(VirtualEntityEventTarget target, EventGoalFactory eventGoalFactory) {
        super(target, (entity,context) -> {
            try {
                /*List<McmeEntity> goalTargets = goalTargetSelector.getTargets(context);
                if(goalTargets.isEmpty()) {
                    context.getDescriptor().addLine("Selected goals: --none--");
                } else {
                    context.getDescriptor().addLine("Selected goals:").indent();
                    goalTargets.forEach(goalTarget -> context.getDescriptor().addLine(goalTarget.getName()));
                    context.getDescriptor().outdent();
                }
                if (!goalTargets.isEmpty()) {
                    goalFactory.withTargetEntity(goalTargets.get(0));
                }*/
                entity.setGoal(eventGoalFactory.getFactory(context).build(entity));
            } catch (InvalidLocationException | InvalidDataException e) {
                DebugManager.warn(Modules.Action.execute(SetGoalAction.class), "Can't assign goal to "+entity.getName()+". "+e.getClass().getSimpleName()+" "+e.getMessage());
                e.printStackTrace();
            }
        });
        getDescriptor().indent()
                .addLine("Goal Factory: "+eventGoalFactory);
    }
}
