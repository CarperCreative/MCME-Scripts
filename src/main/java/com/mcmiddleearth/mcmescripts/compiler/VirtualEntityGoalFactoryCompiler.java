package com.mcmiddleearth.mcmescripts.compiler;

import com.craftmend.thirdparty.iosocket.global.Global;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.ai.goal.GoalType;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.EventGoalFactory;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.rotation.EventRotation;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import org.bukkit.Location;
import org.w3c.dom.events.EventTarget;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.logging.Logger;

public class VirtualEntityGoalFactoryCompiler {

    private static final String KEY_GOAL        = "goal",

                                KEY_TYPE               = "goal_type",
                                KEY_TARGET             = "target",
                                KEY_TARGET_LOCATION    = "target_location", // Should be called target position I think but not a great time to change it right now
                                KEY_TARGET_ROTATION    = "target_rotation",
                                KEY_CHECKPOINTS        = "checkpoints"
    ;

    public static Optional<VirtualEntityGoalFactory> compile(JsonObject jsonObject) {
        VirtualEntityGoalFactory result = null;
        JsonElement element = jsonObject.get(KEY_GOAL);
        if (element == null) {
            DebugManager.warn(Modules.Trigger.create(VirtualEntityGoalFactoryCompiler.class),"Can't compile VirtualEntityGoalFactory. Missing goal data.");
            return Optional.empty();
        }

        Gson gson = EntitiesPlugin.getEntitiesGsonBuilder().create();
        if(element.isJsonPrimitive()) {
            File file = new File(EntitiesPlugin.getEntitiesFolder(),element.getAsString()+".json");
            try (JsonReader reader = gson.newJsonReader(new FileReader(file))) {
                reader.beginArray();
                VirtualEntityFactory factory = gson.fromJson(reader, VirtualEntityFactory.class);
                if(factory.getGoalFactory()!=null) {
                    result = factory.getGoalFactory();
                }
            } catch (IOException e) {
                DebugManager.warn(Modules.Trigger.create(VirtualEntityGoalFactoryCompiler.class),"Can't compile VirtualEntityGoalFactory. Invalid goal data in external file.");
            }
        } else if(element.isJsonObject()) {
            result = compileObject(gson, element.getAsJsonObject());
        }

        if(result == null) {
            DebugManager.warn(Modules.Trigger.create(VirtualEntityGoalFactoryCompiler.class),"Can't compile VirtualEntityGoalFactory. Unknown error.");
            return Optional.empty();
        }

        return Optional.of(result);
    }

    private static VirtualEntityGoalFactory compileObject(Gson gson, JsonObject jsonObject) {
        VirtualEntityGoalFactory factory
                = gson.fromJson(new JsonReader(new StringReader(jsonObject.toString())),VirtualEntityGoalFactory.class);
        return factory;
    }

    public static Optional<EventGoalFactory> compileFromEditor(JsonObject jsonObject) {

        JsonElement type = jsonObject.get(KEY_TYPE);
        if(!(type instanceof JsonPrimitive)) {
            DebugManager.warn(Modules.Trigger.create(TriggerCompiler.class),"Can't compile goal. Missing goal type.");
            return Optional.empty();
        }

        GoalType goalType = GoalType.valueOf(type.getAsString().toUpperCase());
        VirtualEntityGoalFactory factory = new VirtualEntityGoalFactory(goalType);

        EntityEventTarget target = null;
        JsonElement targetJson = jsonObject.get(KEY_TARGET);
        if(targetJson instanceof JsonObject) {
            target = TargetCompiler.compileEntityTarget(targetJson.getAsJsonObject());
        }

        EventPosition position = null;
        JsonElement positionJson = jsonObject.get(KEY_TARGET_LOCATION);
        if(positionJson instanceof JsonObject) {
            position = PositionCompiler.compile(positionJson.getAsJsonObject());
        }

        EventRotation rotation = null;
        JsonElement rotationJson = jsonObject.get(KEY_TARGET_ROTATION);
        if(rotationJson instanceof JsonObject) {
            rotation = RotationCompiler.compile(rotationJson.getAsJsonObject());
        }

        List<EventPosition> checkpoints = null;
        switch (goalType){
            case FOLLOW_CHECKPOINTS, RANDOM_CHECKPOINTS -> {
                JsonElement checkPointsJson = jsonObject.get(KEY_CHECKPOINTS);
                if(!(checkPointsJson instanceof JsonArray)) {
                    DebugManager.warn(Modules.Trigger.create(TriggerCompiler.class),"Can't compile goal. Missing checkpoints.");
                    return Optional.empty();
                }
                JsonArray checkpointsArray = checkPointsJson.getAsJsonArray();

                checkpoints = new ArrayList<>();

                for (JsonElement checkpoint : checkpointsArray) {
                    checkpoints.add(PositionCompiler.compile(checkpoint.getAsJsonObject()));
                }
            }
        }
        return Optional.of(new EventGoalFactory(factory, target, position, rotation, checkpoints));
    }
}
