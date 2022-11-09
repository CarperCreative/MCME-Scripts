package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.mcmescripts.event.target.*;
import com.mcmiddleearth.mcmescripts.event.target.context.EntityContextTarget;
import com.mcmiddleearth.mcmescripts.event.target.context.PlayerContextTarget;
import com.mcmiddleearth.mcmescripts.event.target.context.VirtualEntityContextTarget;
import com.mcmiddleearth.mcmescripts.event.target.selector.EntitySelectorTarget;
import com.mcmiddleearth.mcmescripts.event.target.selector.PlayerSelectorTarget;
import com.mcmiddleearth.mcmescripts.event.target.selector.VirtualEntitySelectorTarget;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class TargetCompiler {

    private static final String
            KEY_FROM              = "from",
            KEY_SELECTOR          = "selector",
            KEY_CONTEXT           = "context_key";

    public static PlayerEventTarget compilePlayerTarget(JsonObject jsonObject) {
        JsonElement fromJson = jsonObject.getAsJsonObject().get(KEY_FROM);
        if(!(fromJson instanceof JsonPrimitive)) return null;

        switch (ETargetFrom.valueOf(fromJson.getAsString())){
            case FROM_SELECTOR -> {
                Selector selector = SelectorCompiler.compileSelector(jsonObject,"selector");
                return new PlayerSelectorTarget(selector);
            }
            case FROM_CONTEXT -> {
                JsonElement keyJson = jsonObject.getAsJsonObject().get(KEY_CONTEXT);
                return new PlayerContextTarget(keyJson.getAsString());
            }
        }
        return null;
    }
    public static EntityEventTarget compileEntityTarget(JsonObject jsonObject) {
        JsonElement typeJson = jsonObject.getAsJsonObject().get(KEY_FROM);
        if(!(typeJson instanceof JsonPrimitive)) return null;

        switch (ETargetFrom.valueOf(typeJson.getAsString())){
            case FROM_SELECTOR -> {
                Selector selector = SelectorCompiler.compileSelector(jsonObject,"selector");
                return new EntitySelectorTarget(selector);
            }
            case FROM_CONTEXT -> {
                JsonElement keyJson = jsonObject.getAsJsonObject().get(KEY_CONTEXT);
                return new EntityContextTarget(keyJson.getAsString());
            }
        }
        return null;
    }
    public static VirtualEntityEventTarget compileVirtualEntityTarget(JsonObject jsonObject) {
        JsonElement typeJson = jsonObject.getAsJsonObject().get(KEY_FROM);
        if(!(typeJson instanceof JsonPrimitive)) return null;

        switch (ETargetFrom.valueOf(typeJson.getAsString())){
            case FROM_SELECTOR -> {
                Selector selector = SelectorCompiler.compileSelector(jsonObject,"selector");
                return new VirtualEntitySelectorTarget(selector);
            }
            case FROM_CONTEXT -> {
                JsonElement keyJson = jsonObject.getAsJsonObject().get(KEY_CONTEXT);
                return new VirtualEntityContextTarget(keyJson.getAsString());
            }
        }
        return null;
    }
}
