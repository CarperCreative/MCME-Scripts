package com.mcmiddleearth.mcmescripts.action;

import com.google.common.base.Joiner;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;

public class TalkAction extends VirtualEntityTargetedAction {

    public TalkAction(VirtualEntityEventTarget target, SpeechBalloonLayout layout) {
        super(target, (entity,context) -> {
            SpeechBalloonLayout tempLayout = layout.clone();
            if(context.getMessage()!=null) {
                tempLayout.withMessage(context.getMessage());
            }
            entity.say(tempLayout);
        });
        getDescriptor().indent()
                .addLine("Message: "+Joiner.on(" ").join(layout.getLines())).outdent();
    }
}
