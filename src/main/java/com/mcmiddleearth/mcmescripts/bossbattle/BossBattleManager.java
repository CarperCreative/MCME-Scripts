package com.mcmiddleearth.mcmescripts.bossbattle;

import com.google.gson.JsonSyntaxException;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcmescripts.ConfigKeys;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.quest.Quest;
import com.mcmiddleearth.mcmescripts.quest.QuestData;
import com.mcmiddleearth.mcmescripts.quest.QuestLoader;
import com.mcmiddleearth.mcmescripts.quest.party.Party;
import com.mcmiddleearth.mcmescripts.quest.party.PartyManager;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Manages creation, deletion, loading and unloading of quests.
 */
public class BossBattleManager {

    /**
     * Periodically check:tLoaders
     * - creation of quest instances
     * - loading and unloading of quest stages
     */
    //private static BukkitTask checker;

    /**
     * Timestamp of last lifetime check.
     */
    //private static long lastQuestLifetimeCheck = 0L;

    /**
     * Period of quest lifetime checks.
     */
    //private static long questLifetimeCheckPeriod;

    /**
     * Map of all quest loaders, one for each quest.
     * mapping: Quest name -> Quest loader
     */
    private static final Map<String, BossBattleLoader> bossBattleLoaders = new HashMap<>();

    /**
     * Map of all quest instance of all loaded parties
     * mapping: quest -> Set of all quests this party is doing.
     */
    private static final List<BossBattle> activeBossBattles = new ArrayList<>();

    /**
     * Folder for storing quest information (entities and events of all stages)
     */
    private static final File bossBattlesFolder = new File(MCMEScripts.getInstance().getDataFolder(),"bossbatles");

    public static void readBossBattles() {
        removeBossBattles();
        //questLifetimeCheckPeriod = MCMEScripts.getConfigLong(ConfigKeys.QUEST_LIFETIME, 1000*3600*24);
        if(!bossBattlesFolder.exists()) {
            if(bossBattlesFolder.mkdir()) {
                Logger.getLogger(MCMEScripts.class.getSimpleName()).info("Boss battle folder created.");
            }
        }
        for(File file : Objects.requireNonNull(bossBattlesFolder.listFiles(((dir, name) -> name.endsWith(".json"))))) {
            try {
                Logger.getGlobal().info("create quest loader: "+file.getName());
                BossBattleLoader bossBattleLoader = new BossBattleLoader(file);
                bossBattleLoaders.put(bossBattleLoader.getBossBattleName().toLowerCase(), bossBattleLoader);
            } catch (NullPointerException | IOException | IllegalStateException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createBossBattle(String name, CommandContext<McmeCommandSender> context){

        if(bossBattleLoaders.containsKey(name.toLowerCase())){
            activeBossBattles.add(bossBattleLoaders.get(name.toLowerCase()).getBossBattle());
            context.getSource().sendMessage("Boss battle '" + name + "' started.");
        } else {
            context.getSource().sendMessage("No boss battle with the name '" + name + "' has been loaded.");
        }
    }

    public static void removeBossBattle(String name) {
        BossBattle bossBattleToRemove = null;
        for(BossBattle bossBattle : activeBossBattles){
            if (bossBattle.getName().equalsIgnoreCase(name)){
                bossBattleToRemove = bossBattle;
            }
        }
        if(bossBattleToRemove != null) {
            bossBattleToRemove.stop();
            activeBossBattles.remove(bossBattleToRemove);
        }
    }

    public static void removeBossBattles() {
        unloadActiveBossBattles();
        bossBattleLoaders.clear();
    }

    public static void unloadActiveBossBattles() {
        activeBossBattles.forEach(BossBattle::stop);
        activeBossBattles.clear();
    }

    public static List<BossBattle> getAllActiveBossBattles() {
        return activeBossBattles;
    }
    public static Map<String,BossBattleLoader> getAllBossBattleLoaders() {
        return bossBattleLoaders;
    }
}
