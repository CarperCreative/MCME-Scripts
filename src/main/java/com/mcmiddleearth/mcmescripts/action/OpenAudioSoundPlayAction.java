package com.mcmiddleearth.mcmescripts.action;

import com.craftmend.openaudiomc.api.interfaces.AudioApi;
import com.craftmend.openaudiomc.generic.media.objects.MediaOptions;
import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;
import org.bukkit.Bukkit;

public class OpenAudioSoundPlayAction extends PlayerTargetedAction {

    private static String url = "http://media.mcmiddleearth.com/media/sounds/";

    public OpenAudioSoundPlayAction(PlayerEventTarget target, String soundFile, String soundId) {
        super(target, (player, context) -> {
            if(OpenAudioSoundPlayAction.hasOpenAudio()) {
                AudioApi audioApi = AudioApi.getInstance();
                MediaOptions options = new MediaOptions();
                options.setId(soundFile);
                audioApi.getMediaApi().playMedia(audioApi.getClient(player.getUniqueId()), url + soundId, options);
            } else {
                DebugManager.warn(Modules.Action.execute(OpenAudioSoundPlayAction.class),"OpenAudioMc plugin not found!");
            }
        });
        getDescriptor().indent()
                .addLine("Sound file: "+soundFile)
                .addLine("Sound id: "+soundId).outdent();
    }

    public static boolean hasOpenAudio() {
        return Bukkit.getPluginManager().getPlugin("OpenAudioMc") != null;
    }
}
