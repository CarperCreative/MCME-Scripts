package com.mcmiddleearth.mcmescripts.trigger.virtual;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.events.events.goal.GoalFinishedEvent;
import com.mcmiddleearth.entities.events.handler.EntityEventHandler;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.EntitiesEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class GoalFinishedTrigger extends EntitiesEventTrigger {

    public GoalFinishedTrigger(Action action) {
        super(action);
        DebugManager.info(Modules.Trigger.create(this.getClass()),
                "Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    @SuppressWarnings("unused")
    @EntityEventHandler
    public void onGoalFinished(GoalFinishedEvent event) {
        TriggerContext context = new TriggerContext(this);
        if(event.getEntity() instanceof VirtualEntity) {
            context.withEntity((VirtualEntity) event.getEntity());
        }
        context.withGoal(event.getGoal());
        call(context);
        DebugManager.verbose(Modules.Trigger.call(this.getClass()),
                "Entity: "+context.getEntity() + " Goal: " + event.getGoal());
    }
}
