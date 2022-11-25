package com.mcmiddleearth.mcmescripts.bossbattle;

import com.google.gson.JsonSyntaxException;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mojang.brigadier.context.CommandContext;

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
    private final Map<String, BossBattleLoader> bossBattleLoaders = new HashMap<>();

    /**
     * Map of all quest instance of all loaded parties
     * mapping: quest -> Set of all quests this party is doing.
     */
    private final List<BossBattle> activeBossBattles = new ArrayList<>();

    /**
     * Folder for storing quest information (entities and events of all stages)
     */
    private final File bossBattlesFolder = new File(MCMEScripts.getInstance().getDataFolder(),"bossbatles");

    public void readBossBattles() {
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

    public void startBossBattle(String name, String donor, String message, String amount){

        if(bossBattleLoaders.containsKey(name.toLowerCase())){

            BossBattle bossBattle = bossBattleLoaders.get(name.toLowerCase()).getBossBattle();
            bossBattle.setDonation(donor,message,amount);
            activeBossBattles.add(bossBattle);
        }
    }

    public void startBossBattle(String name, CommandContext<McmeCommandSender> context){

        if(bossBattleLoaders.containsKey(name.toLowerCase())){
            activeBossBattles.add(bossBattleLoaders.get(name.toLowerCase()).getBossBattle());
            if (context != null) context.getSource().sendMessage("Boss battle '" + name + "' started.");
        } else {
            if (context != null) context.getSource().sendMessage("No boss battle with the name '" + name + "' has been loaded.");
        }
    }

    public void removeBossBattle(String name) {
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

    public void removeBossBattles() {
        unloadActiveBossBattles();
        bossBattleLoaders.clear();
    }

    public void unloadActiveBossBattles() {
        activeBossBattles.forEach(BossBattle::stop);
        activeBossBattles.clear();
    }

    public List<BossBattle> getAllActiveBossBattles() {
        return activeBossBattles;
    }
    public Map<String,BossBattleLoader> getAllBossBattleLoaders() {
        return bossBattleLoaders;
    }
}
