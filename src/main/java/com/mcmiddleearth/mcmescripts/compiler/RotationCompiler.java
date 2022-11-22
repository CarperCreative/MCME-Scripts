package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.rotation.*;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;

public class RotationCompiler {

    private static final String
            KEY_TYPE                    = "type",
            KEY_PITCH                   = "pitch",
            KEY_YAW                     = "yaw",
            KEY_COPY_ENTITY             = "copy_entity",
            LOOK_AT_ENTITY              = "look_at_entity",
            KEY_CONTEXT_KEY             = "context_key",
            KEY_OFFSET_PITCH            = "offset_pitch",
            KEY_OFFSET_YAW              = "offset_yaw",
            VALUE_PITCH_YAW             = "pitch_yaw",
            VALUE_COPY_FROM_ENTITY      = "copy_from_entity",
            VALUE_COPY_FROM_ENTITY_HEAD = "copy_from_entity_head",
            VALUE_LOOK_AT_ENTITY        = "look_at_entity",
            VALUE_FROM_TRIGGER_CONTEXT  = "from_trigger_context";

    public static EventRotation compile(JsonObject jsonObject) {
        JsonElement typeJson = jsonObject.getAsJsonObject().get(KEY_TYPE);
        if(!(typeJson instanceof JsonPrimitive)) return null;

        float pitchOffset = 0;
        float yawOffset = 0;
        JsonElement pitchOffsetJson = jsonObject.getAsJsonObject().get(KEY_OFFSET_PITCH);
        if(pitchOffsetJson instanceof JsonPrimitive) pitchOffset = pitchOffsetJson.getAsFloat();
        JsonElement yawOffsetJson = jsonObject.getAsJsonObject().get(KEY_OFFSET_YAW);
        if(yawOffsetJson instanceof JsonPrimitive) yawOffset = yawOffsetJson.getAsFloat();

        switch (typeJson.getAsString()){
            case VALUE_PITCH_YAW -> {
                float pitch = 0;
                float yaw = 0;
                JsonElement pitchJson = jsonObject.getAsJsonObject().get(KEY_PITCH);
                if(pitchJson instanceof JsonPrimitive) pitch = pitchJson.getAsFloat();
                JsonElement yawJson = jsonObject.getAsJsonObject().get(KEY_YAW);
                if(yawJson instanceof JsonPrimitive) yaw = pitchJson.getAsFloat();

                if(!(pitchJson instanceof JsonPrimitive && yawJson instanceof JsonPrimitive)){
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile Rotation. Pitch and yaw not specified.");
                    return null;
                }
                return new PitchYawRotation(pitch,yaw);
            }
            case VALUE_COPY_FROM_ENTITY -> {
                JsonElement copyEntityJson = jsonObject.getAsJsonObject().get(KEY_COPY_ENTITY);
                if(!(copyEntityJson instanceof JsonObject)){
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile rotation. No entity to copy specified.");
                    return null;
                }
                EntityEventTarget target = TargetCompiler.compileEntityTarget(copyEntityJson.getAsJsonObject());
                return new EntityCopyRotation(target,pitchOffset,yawOffset);
            }
            case VALUE_COPY_FROM_ENTITY_HEAD -> {
                JsonElement copyEntityJson = jsonObject.getAsJsonObject().get(KEY_COPY_ENTITY);
                if(!(copyEntityJson instanceof JsonObject)){
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile rotation. No entity to copy specified.");
                    return null;
                }
                EntityEventTarget target = TargetCompiler.compileEntityTarget(copyEntityJson.getAsJsonObject());
                return new EntityCopyHeadRotation(target,pitchOffset,yawOffset);
            }
            case VALUE_LOOK_AT_ENTITY -> {
                JsonElement copyEntityJson = jsonObject.getAsJsonObject().get(KEY_COPY_ENTITY);
                if(!(copyEntityJson instanceof JsonObject)){
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile rotation. No entity to look at specified.");
                    return null;
                }
                EntityEventTarget target = TargetCompiler.compileEntityTarget(copyEntityJson.getAsJsonObject());
                return new EntityLookAtRotation(target,null,pitchOffset,yawOffset); //TODO: look at requires the position of the entity. What is the best way to do that?
            }
            case VALUE_FROM_TRIGGER_CONTEXT -> {
                JsonElement keyJson = jsonObject.getAsJsonObject().get(KEY_CONTEXT_KEY);
                if(!(keyJson instanceof JsonPrimitive)){
                    DebugManager.warn(Modules.Location.create(LocationCompiler.class),"Can't compile position. No context key specified.");
                    return null;
                }
                String key = keyJson.getAsString();
                return new TriggerContextRotation(key,pitchOffset,yawOffset);
            }
        }
        return null;
    }
}
