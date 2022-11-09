package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;

public class StopTalkAction extends VirtualEntityTargetedAction {

    public StopTalkAction(VirtualEntityEventTarget target) {
        super(target, (entity,context)-> {
            entity.stopTalking();
        });
    }

}
