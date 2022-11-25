package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;

import java.util.logging.Logger;

public class TitleAction extends PlayerTargetedAction {

    public TitleAction(PlayerEventTarget target, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        super(target, (player, context) -> {
            DebugManager.verbose(Modules.Action.execute(TitleAction.class),"Title for player: "+player.getName() + " " + title);
            String finalTitle = title;
            String finalSubtitle = subtitle;
            if(context.getMessage()!=null) {
                finalTitle = finalTitle.replace("*message*", context.getMessage());
                finalSubtitle = finalSubtitle.replace("*message*", context.getMessage());
            }
            if(context.getName()!=null) {
                finalTitle = finalTitle.replace("*name*", context.getName());
                finalSubtitle = finalSubtitle.replace("*name*", context.getName());
            }
            player.sendTitle(finalTitle,finalSubtitle,fadeIn,stay,fadeOut);
        });
        Logger.getGlobal().info("Displaying Title: " + title + ", " + subtitle + " to " + target.toString());
        getDescriptor().indent()
                .addLine("Title: "+title)
                .addLine("Subtitle: "+subtitle)
                .addLine("Times: "+fadeIn+" "+stay+" "+fadeOut);
    }
}
