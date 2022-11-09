package com.mcmiddleearth.mcmescripts.bossbattle;

import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.compiler.BossBattleCompiler;
import com.mcmiddleearth.mcmescripts.compiler.ScriptCompiler;
import com.mcmiddleearth.mcmescripts.compiler.StageCompiler;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.quest.QuestManager;
import com.mcmiddleearth.mcmescripts.quest.StageAccess;
import com.mcmiddleearth.mcmescripts.quest.party.PartyManager;
import com.mcmiddleearth.mcmescripts.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Manages creation of new instances of a quest.
 * A new instance is created when a party triggers one of the quests access stages.
 */
public class BossBattleLoader {

    /**
     * Quest name
     */
    private final String bossBattleName;

    /**
     * Data file of this quest containing information about entities and events of all stages.
     */
    private final File dataFile;


    public BossBattleLoader(File file) throws IOException {
        dataFile = file;
        JsonObject jsonData = JsonUtils.loadJsonData(dataFile);
        assert jsonData != null;
        bossBattleName = BossBattleCompiler.getName(jsonData).orElse(System.currentTimeMillis()+"_"+Math.random()).toLowerCase().replace(' ','_');
        DebugManager.info(Modules.QuestLoader.create(this.getClass()), getDescriptor().print(""));
    }

    /*public void checkQuestCreation() {
        //check for each party if it triggers this quest, then create quest object and add to QuestManager
        Logger.getGlobal().info("Check quest creation: "+PartyManager.getParties().size()+ " parties.");
        PartyManager.getParties().stream()
                .filter(party->!QuestManager.hasActiveQuest(party,questName))
                                                .forEach(party -> {
            for(StageAccess stage: accessStages) {
                Logger.getGlobal().info("Access stage: "+stage.getName()+" triggerd: "+stage.isTriggered(party));
                if (stage.isTriggered(party)) {
                    QuestManager.addQuest(questName, stage.getName(), party);
                    break;
                }
            }
        });
    }*/

    public String getBossBattleName() {
        return bossBattleName;
    }

    public BossBattle getBossBattle() {
        try{
            JsonObject jsonData = JsonUtils.loadJsonData(dataFile);
            assert jsonData != null;
            return BossBattleCompiler.compile(jsonData);
        } catch (IOException exception){
            Logger.getGlobal().warning("Unable to load boss battle from file. Are you sure it exists?");
            return null;
        }
    }

    public Descriptor getDescriptor() {
        Descriptor descriptor = new Descriptor("Boss battle: "+bossBattleName)
                .indent()
                .addLine("Data file: "+dataFile.getName());
        descriptor.outdent();
        return descriptor;
    }


}
