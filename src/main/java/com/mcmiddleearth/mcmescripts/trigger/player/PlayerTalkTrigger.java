package com.mcmiddleearth.mcmescripts.trigger.player;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.BukkitEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.platform.facet.Facet;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerTalkTrigger extends BukkitEventTrigger {

    private final String command;

    public PlayerTalkTrigger(Action action, String command) {
        super(action);
        this.command = command;
        DebugManager.info(Modules.Trigger.create(this.getClass()),
                "Action: " + (action!=null?action.getClass().getSimpleName():null));
    }

    @EventHandler
    public void onPlayerTalk(AsyncChatEvent event) {

        Pattern pattern = Pattern.compile("(?<=content=\")(.*)(?=\")");
        String fullMessage = event.message().toString();
        Matcher matcher = pattern.matcher(fullMessage);
        if(matcher.find()){
            String message = matcher.group(0);
            Logger.getGlobal().warning(message);

            if(!message.startsWith("!") || // Begin with !
                    (!command.equals("") && // Command is not an empty string (match command)
                    !message.split(" ")[0].substring(1,message.length()).equals(command))) // Match command but allow room for arguments
                return;

            event.setCancelled(true);

            TriggerContext context = new TriggerContext(this)
                    .withPlayer(event.getPlayer())
                    .withMessage(message)
                    .withContext("PlayerTalkTrigger.PlayerWhoTalked",event.getPlayer());
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

    private void callInternal(TriggerContext context) {
        call(context);
        DebugManager.verbose(Modules.Trigger.call(this.getClass()),
                "Player: " + context.getPlayer().getName() + " Message: " + context.getMessage());
    }

}
