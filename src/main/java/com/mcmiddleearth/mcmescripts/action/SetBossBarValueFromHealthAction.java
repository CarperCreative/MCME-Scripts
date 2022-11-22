package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;

import java.util.List;

public class SetBossBarValueFromHealthAction extends VirtualEntityTargetedAction {

    public SetBossBarValueFromHealthAction(VirtualEntityEventTarget target, NamespacedKey barKey, double maxHealth) {
        super(target, (entity, context)  -> {
            BossBar bar = Bukkit.getBossBar(barKey);

            List<VirtualEntity> bossBarTargets = target.getTargets(context);
            if(bossBarTargets.size() > 0 && bar != null){
                bar.setProgress(bossBarTargets.get(0).getHealth() / maxHealth);
            }
        });
    }
}
