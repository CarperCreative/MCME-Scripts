package com.mcmiddleearth.mcmescripts.action;

import com.craftmend.openaudiomc.api.interfaces.AudioApi;
import com.craftmend.openaudiomc.generic.media.objects.MediaOptions;
import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.util.Vector;

public class SoundPlayAction extends PlayerTargetedAction {

    public SoundPlayAction(PlayerEventTarget target, String sound, SoundCategory category, EventPosition eventPosition, float volume, float pitch) {
        super(target, (player, context) -> {

            Vector position = eventPosition.getPosition(context);
            Location location = new Location(context.getWorld(),position.getX(),position.getY(),position.getZ());

            player.playSound(location,sound,category,volume,pitch);
        });
        getDescriptor().indent()
                .addLine("Sound: "+sound)
                .addLine("Category: "+category.toString())
                .addLine("position: "+eventPosition)
                .addLine("volume: "+volume)
                .addLine("pitch: "+pitch)
                .outdent();
    }
}
