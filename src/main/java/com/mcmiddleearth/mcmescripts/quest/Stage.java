package com.mcmiddleearth.mcmescripts.quest;

import com.google.gson.JsonObject;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.compiler.ConditionCompiler;
import com.mcmiddleearth.mcmescripts.compiler.StageCompiler;
import com.mcmiddleearth.mcmescripts.compiler.TriggerCompiler;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.script.Script;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import com.mcmiddleearth.mcmescripts.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Stage {

    private final String name;

    private final Quest quest;

    private final Set<Condition> conditions;
    private boolean metAllConditions = true;

    //flag to indicate if triggers and entities of this stage are loaded
    private boolean isActive;

    private final Set<Trigger> triggers = new HashSet<>();
    private final Set<McmeEntity> entities = new HashSet<>();

    public Stage(Quest quest, String name) throws IOException {
        this(quest, name, getJsonData(name, quest.getDataFile()));
    }

    public Stage(Quest quest, String name, JsonObject jsonData) {
        this.name = name;
        this.quest = quest;
        conditions = ConditionCompiler.compile(jsonData);
        if(!conditions.isEmpty()) metAllConditions = TriggerCompiler.getMetAllConditions(jsonData);
    }

    public boolean isTriggered() {
        if(conditions.isEmpty()) return true;
        TriggerContext context = new TriggerContext(new Trigger() {
            @Override
            public Script getScript() {
                return quest;
            }
            @Override
            public void call(TriggerContext context) {}
        });
        for(Condition condition: conditions) {
            boolean testResult = condition.test(context);
            if(metAllConditions && !testResult) {
                return false;
            } else if(!metAllConditions && testResult) {
                return true;
            }
        }
        return metAllConditions;
    }

    public boolean isActive() {
        return isActive;
    }

    public void load() {

    }

    public void unload() {

    }



    static JsonObject getJsonData(String name, File dataFile) throws IOException {
        JsonObject jsonData = JsonUtils.loadJsonData(dataFile);
        assert jsonData != null;
        jsonData = StageCompiler.getStageData(name, jsonData);
        assert  jsonData != null;
        return jsonData;
    }
}
