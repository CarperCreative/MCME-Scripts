package com.mcmiddleearth.mcmescripts.action;

import com.craftmend.openaudiomc.api.interfaces.AudioApi;
import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;

public class OpenAudioSoundStopAction extends PlayerTargetedAction {

    public OpenAudioSoundStopAction(PlayerEventTarget target, String soundID) {
        super(target, (player, context) -> {
            if(OpenAudioSoundPlayAction.hasOpenAudio()) {
                AudioApi audioApi = AudioApi.getInstance();
                if (soundID == null || soundID.equalsIgnoreCase("")) {
                    audioApi.getMediaApi().stopMedia(audioApi.getClient(player.getUniqueId()));
                } else {
                    audioApi.getMediaApi().stopMedia(audioApi.getClient(player.getUniqueId()), soundID);
                }
            } else {
                DebugManager.warn(Modules.Action.execute(OpenAudioSoundPlayAction.class),"OpenAudioMc plugin not found!");
            }
        });
        getDescriptor().indent()
                .addLine("Sound id: "+soundID).outdent();
    }
}
