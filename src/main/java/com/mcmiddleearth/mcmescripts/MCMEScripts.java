package com.mcmiddleearth.mcmescripts;

import com.mcmiddleearth.mcmescripts.command.PartyCommandHandler;
import com.mcmiddleearth.mcmescripts.command.ScriptsCommandHandler;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.listener.ChestListener;
import com.mcmiddleearth.mcmescripts.listener.PlayerListener;
import com.mcmiddleearth.mcmescripts.listener.WandItemListener;
import com.mcmiddleearth.mcmescripts.quest.QuestManager;
import com.mcmiddleearth.mcmescripts.quest.party.PartyListener;
import com.mcmiddleearth.mcmescripts.script.ScriptManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCMEScripts extends JavaPlugin {

    private static TimedTriggerManager timedTriggerManager;
    private static ExternalTriggerManager externalTriggerManager;
    private static ScriptManager scriptManager;
    private static MCMEScripts instance;

    private static long ticks = 0;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        DebugManager.open();
        scriptManager = new ScriptManager();
        timedTriggerManager = new TimedTriggerManager();
        externalTriggerManager = new ExternalTriggerManager();
        Bukkit.getPluginManager().registerEvents(new PartyListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
        Bukkit.getPluginManager().registerEvents(new WandItemListener(),this);
        Bukkit.getPluginManager().registerEvents(new ChestListener(),this);
        //BukkitAudiences.create(this);
        setExecutor("scripts", new ScriptsCommandHandler("scripts"));
        setExecutor("party", new PartyCommandHandler("party"));
        Bukkit.getScheduler().runTaskLater(this, this::enableScripts,
                                            MCMEScripts.getConfigInt(ConfigKeys.START_UP_DELAY,95));
        Bukkit.getScheduler().runTaskTimer(this, MCMEScripts::incrementTick, 1L, 1L);
    }

    @Override
    public void onDisable() {
        disableScripts();
        DebugManager.close();
    }

    public void enableScripts() {
        scriptManager.readScripts();
        scriptManager.startChecker();
        timedTriggerManager.startChecker();
        QuestManager.readQuests();
        QuestManager.startChecker();
    }

    public void disableScripts() {
        QuestManager.stopChecker();
        QuestManager.removeQuests();
        timedTriggerManager.stopChecker();
        scriptManager.stopChecker();
        scriptManager.removeScripts();
    }

    public static ScriptManager getScriptManager() {
        return scriptManager;
    }

    public static ExternalTriggerManager getExternalTriggerManager() {
        return externalTriggerManager;
    }

    public static MCMEScripts getInstance() {
        return instance;
    }

    public static TimedTriggerManager getTimedTriggerChecker() {
        return timedTriggerManager;
    }

    public static synchronized int getConfigInt(ConfigKeys key, int defaultValue) {
        return instance.getConfig().getInt(key.getKey(),defaultValue);
    }

    public static synchronized long getConfigLong(ConfigKeys key, long defaultValue) {
        return instance.getConfig().getLong(key.getKey(),defaultValue);
    }

    public static synchronized String getConfigString(ConfigKeys key, String defaultValue) {
        return instance.getConfig().getString(key.getKey(),defaultValue);
    }

    public static synchronized void setConfigString(ConfigKeys key, String value) {
        instance.getConfig().set(key.getKey(),value);
        instance.saveConfig();
    }

    public static synchronized double getConfigValueDouble(ConfigKeys key, double defaultValue) {
        return instance.getConfig().getDouble(key.getKey(),defaultValue);
    }

    private void setExecutor(String command, CommandExecutor executor) {
        PluginCommand pluginCommand = Bukkit.getServer().getPluginCommand(command);
        if(pluginCommand!=null) {
            pluginCommand.setExecutor(executor);
            if (executor instanceof TabCompleter)
                pluginCommand.setTabCompleter((TabCompleter) executor);
        }
    }

    private static void incrementTick() {
        ticks++;
    }

    /**
     * Returns the current tick. Workaround for Magma, which doesn't support {@link Bukkit#getCurrentTick()}.
     */
    public static long getCurrentTick() {
        return ticks;
    }


}
