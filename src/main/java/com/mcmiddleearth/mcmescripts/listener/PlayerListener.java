package com.mcmiddleearth.mcmescripts.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMessage(AsyncChatEvent event) {
        // NKH HP: mute all messages starting with !
        Pattern pattern = Pattern.compile("(?<=content=\")(.*)(?=\")");
        String fullMessage = event.message().toString();
        Matcher matcher = pattern.matcher(fullMessage);
        if(matcher.find()) {
            String message = matcher.group(0);
            if (message.startsWith("!")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //TODO load PlayerScriptData in ScriptManager
        /*AttributeInstance attrib = event.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if(attrib != null) {
            Logger.getGlobal().info("Modiiers: "+attrib.getModifiers().size());
            attrib.getModifiers().forEach(attrib::removeModifier);
        }*/
        event.getPlayer().setWalkSpeed(0.2f);
        event.getPlayer().setFlySpeed(0.1f);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        //TODO remove PlayerScriptData from ScriptManager
    }
}
