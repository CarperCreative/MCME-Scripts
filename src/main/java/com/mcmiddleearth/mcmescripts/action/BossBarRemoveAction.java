package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;

public class BossBarRemoveAction extends PlayerTargetedAction {

    public BossBarRemoveAction(PlayerEventTarget target, NamespacedKey barKey) {
        super(target, (player, context) -> {
            BossBar bar = Bukkit.getBossBar(barKey);
            if(bar!=null) {
                bar.removePlayer(player);
            } else {
                DebugManager.warn(Modules.Action.execute(BossBarRemoveAction.class), "Expected Boss bar does not exist: "+barKey.asString());
            }
        });
        getDescriptor().indent().addLine("Bar key: "+barKey).outdent();
    }
}