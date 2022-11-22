package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;

public class BossBarAddAction extends PlayerTargetedAction {

    public BossBarAddAction(PlayerEventTarget selector, BossBar bossBar) {
        super(selector, (player, context) -> {
            bossBar.addPlayer(player);
        });
        getDescriptor().indent().addLine("Title: "+bossBar.getTitle())
                .addLine("Color: "+bossBar.getColor().name())
                .addLine("Style: "+bossBar.getStyle().name())
                .addLine("Fog: "+bossBar.hasFlag(BarFlag.CREATE_FOG))
                .addLine("Darken: "+bossBar.hasFlag(BarFlag.DARKEN_SKY))
                .addLine("Music: "+bossBar.hasFlag(BarFlag.PLAY_BOSS_MUSIC))
                .addLine("Visible: "+bossBar.isVisible())
                .addLine("Progress: "+bossBar.getProgress()).outdent();
    }
}