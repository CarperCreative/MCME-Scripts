package com.mcmiddleearth.mcmescripts.trigger.player;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.BukkitEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UseItemTrigger extends BukkitEventTrigger {

    private final String itemName;

    public UseItemTrigger(Action action, String itemName) {
        super(action);
        this.itemName = itemName;
        DebugManager.info(Modules.Trigger.create(this.getClass()),
                "Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {

        if(event.getItem() != null){
            if(event.getItem().displayName().examinableName().equals(itemName)){
                TriggerContext context = new TriggerContext(this)
                        .withPlayer(event.getPlayer())
                        .withContext("UseItemTrigger.PlayerWhoUsedItem",event.getPlayer());

                if(event.isAsynchronous()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            callInternal(context);
                        }
                    }.runTask(MCMEScripts.getInstance());
                } else {
                    callInternal(context);
                }
            }
        }
    }

    private void callInternal(TriggerContext context) {
        call(context);
        DebugManager.verbose(Modules.Trigger.call(this.getClass()),
                "Player: " + context.getPlayer().getName());
    }

}
