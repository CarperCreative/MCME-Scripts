package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.script.Script;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;

import java.util.Optional;
import java.util.Set;

public class ScriptCompiler {

    private static final String KEY_NAME = "name";

    public static void load(JsonObject jsonData, Script script) {
        Set<Trigger> triggers = EntityCompiler.compile(jsonData);
        triggers.forEach(trigger -> trigger.register(script));
        triggers = TriggerCompiler.compile(jsonData);
        triggers.forEach(trigger -> trigger.register(script));
        triggers = QuestCompiler.compile(jsonData);
        triggers.forEach(trigger -> trigger.register(script));
    }

    public static Optional<String> getName(JsonObject jsonData) {
        JsonElement element = jsonData.get(KEY_NAME);
        if(element!=null && element.isJsonPrimitive()) {
            return Optional.of(element.getAsString());
        } else {
            return Optional.empty();
        }
    }
}
