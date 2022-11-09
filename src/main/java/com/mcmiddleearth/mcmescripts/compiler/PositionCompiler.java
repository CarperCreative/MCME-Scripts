package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.mcmescripts.event.position.*;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

import java.util.logging.Logger;

public class PositionCompiler {

    private static final String
            KEY_TYPE                    = "type",
            KEY_COORDINATES             = "coordinates",
            KEY_COPY_ENTITY             = "copy_entity",
            KEY_CONTEXT_KEY             = "context_key",
            KEY_OFFSET_TYPE             = "offset_type",
            KEY_OFFSET                  = "offset",
            VALUE_COORDINATES           = "coordinates",
            VALUE_COPY_FROM_ENTITY      = "copy_from_entity",
            VALUE_COPY_FROM_ENTITY_HEAD = "copy_from_entity_head",
            VALUE_FROM_TRIGGER_CONTEXT  = "from_trigger_context";

    public static EventPosition compile(JsonObject jsonObject) {
        JsonElement typeJson = jsonObject.getAsJsonObject().get(KEY_TYPE);
        if(!(typeJson instanceof JsonPrimitive)) return null;

        EEventPositionOffsetType offsetType = EEventPositionOffsetType.ALONG_WORLD_AXIS;
        Vector offset = new Vector(0,0,0);

        JsonElement offsetTypeJson = jsonObject.getAsJsonObject().get(KEY_OFFSET_TYPE);
        if(offsetTypeJson instanceof JsonPrimitive)
            offsetType = EEventPositionOffsetType.valueOf(offsetTypeJson.getAsString());


        JsonElement offsetJson = jsonObject.getAsJsonObject().get(KEY_OFFSET);
        if(offsetJson instanceof JsonPrimitive){
            String[] split = offsetJson.getAsString().replace(" ","").split(",");
            if(split.length<3) {
                DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile offset for position. Too few coordinates.");
                return null;
            }
            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);
            double z = Double.parseDouble(split[2]);
            offset = new Vector(x,y,z);
        }

        switch (typeJson.getAsString()){
            case VALUE_COORDINATES -> {
                JsonElement coordinateJson = jsonObject.getAsJsonObject().get(KEY_COORDINATES);
                if(!(coordinateJson instanceof JsonPrimitive)){
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile position. No coordinates specified.");
                    return null;
                }
                String[] split = coordinateJson.getAsString().replace(" ","").split(",");
                if(split.length<3) {
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile position. Too few coordinates.");
                    return null;
                }
                double x = Double.parseDouble(split[0]);
                double y = Double.parseDouble(split[1]);
                double z = Double.parseDouble(split[2]);
                return new CoordinatePosition(new Vector(x,y,z),offset,offsetType);
            }
            case VALUE_COPY_FROM_ENTITY -> {
                JsonElement copyEntityJson = jsonObject.getAsJsonObject().get(KEY_COPY_ENTITY);
                if(!(copyEntityJson instanceof JsonObject)){
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile position. No entity to copy specified.");
                    return null;
                }
                EntityEventTarget target = TargetCompiler.compileEntityTarget(copyEntityJson.getAsJsonObject());
                return new EntityCopyPosition(target,offset,offsetType);
            }
            case VALUE_COPY_FROM_ENTITY_HEAD -> {
                JsonElement copyEntityJson = jsonObject.getAsJsonObject().get(KEY_COPY_ENTITY);
                if(!(copyEntityJson instanceof JsonObject)){
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile position. No entity to copy specified.");
                    return null;
                }
                EntityEventTarget target = TargetCompiler.compileEntityTarget(copyEntityJson.getAsJsonObject());
                return new EntityCopyHeadPosition(target,offset,offsetType);
            }
            case VALUE_FROM_TRIGGER_CONTEXT -> {
                JsonElement keyJson = jsonObject.getAsJsonObject().get(KEY_CONTEXT_KEY);
                if(!(keyJson instanceof JsonPrimitive)){
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile position. No context key specified.");
                    return null;
                }
                String key = keyJson.getAsString();
                return new TriggerContextPosition(key,offset,offsetType);
            }
        }
        return null;
    }
}
