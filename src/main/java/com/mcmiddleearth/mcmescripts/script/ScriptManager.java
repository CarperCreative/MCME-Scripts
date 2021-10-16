package com.mcmiddleearth.mcmescripts.script;

import com.google.gson.JsonSyntaxException;
import com.mcmiddleearth.mcmescripts.ConfigKeys;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class ScriptManager {

    private final Map<String,Script> scripts = new HashMap<>();

    private final Map<UUID, PlayerScriptData> playerScriptData = new HashMap<>();

    private BukkitTask checker;

    private static final File scriptFolder = new File(MCMEScripts.getInstance().getDataFolder(),"scripts");

    public void readScripts() {
        if(!scriptFolder.exists()) {
            if(scriptFolder.mkdir()) {
                Logger.getLogger(MCMEScripts.class.getSimpleName()).info("Scripts folder created.");
            }
        }
        for(File file : scriptFolder.listFiles(((dir, name) -> name.endsWith(".json")))) {
            try {
                addScript(file);
            } catch (IOException | IllegalStateException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void addScript(File file) throws IOException {
        Script script = new Script(file);
        scripts.put(script.getName(), script);
    }

    public void removeScript(String name) {
        Script script = scripts.get(name);
        if(script!=null)
        {
            script.unload();
            scripts.remove(name);
        }
    }

    public void removeScripts() {
        Set<String> names = new HashSet<>(scripts.keySet());
        names.forEach(this::removeScript);
    }

    public void startChecker() {
        stopChecker();
        checker = new BukkitRunnable() {
            @Override
            public void run() {
                scripts.forEach((name,script) -> {
                    boolean isTriggered = script.isTriggered();
                    if (isTriggered && !script.isActive()) {
                        try {
//Logger.getGlobal().info("LoadScript: "+script.getName());
                            script.load();
                        } catch (IOException | IllegalStateException | JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    } else if(!isTriggered && script.isActive()) {
//Logger.getGlobal().info("UnloadScript: "+script.getName());
                        script.unload();
                    }
                });
            }
        }.runTaskTimer(MCMEScripts.getInstance(),MCMEScripts.getConfigInt(ConfigKeys.START_UP_DELAY,95),
                                                 MCMEScripts.getConfigInt(ConfigKeys.SCRIPT_CHECKER_PERIOD,100));
    }

    public void stopChecker() {
        if(checker!=null && !checker.isCancelled()) {
            checker.cancel();
        }
    }

    public  Map<String, Script> getScripts() {
        return scripts;
    }

    public static File getScriptFolder() {
        return scriptFolder;
    }
}
