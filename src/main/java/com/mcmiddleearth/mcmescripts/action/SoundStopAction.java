package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;

public class SoundStopAction extends PlayerTargetedAction {

    public SoundStopAction(PlayerEventTarget target, String sound, String category) {
        super(target, (player, context) -> {
            if(sound != null && category != null){
                player.stopSound(sound,SoundCategory.valueOf(category));
            }
            else if (sound != null){
                player.stopSound(sound);
            } else if (category != null){
                // No way to stop all sounds in category in Spigot, so I'm using commands
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound " + player.getName() + " " + category.toLowerCase());
            } else {
                // No way to stop all sounds in Spigot, so I'm using commands
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"stopsound " + player.getName());
            }
        });
        getDescriptor().indent()
                .addLine("Sound: "+sound)
                .addLine("Category: "+category)
                .outdent();
    }
}
