package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class ServerTimeAction extends Action{

    private final long serverTimeTicks;

    public ServerTimeAction(long serverTimeTicks) {
        this.serverTimeTicks = serverTimeTicks;
        Logger.getGlobal().info("Setting server time: " + serverTimeTicks);
        getDescriptor().indent().addLine("Server time ticks: "+serverTimeTicks).outdent();
    }

    @Override
    protected void handler(TriggerContext context) {
        Bukkit.getWorlds().forEach(world -> world.setFullTime(serverTimeTicks));
    }
}
