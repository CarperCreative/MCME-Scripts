package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;
import net.kyori.adventure.text.Component;

public class ActionBarAction extends PlayerTargetedAction {

    public ActionBarAction(PlayerEventTarget target, String title) {
        super(target, (player, context) -> {
            player.sendActionBar(Component.text(title));
        });
        getDescriptor().indent().addLine("Title: "+title).outdent();
    }

}