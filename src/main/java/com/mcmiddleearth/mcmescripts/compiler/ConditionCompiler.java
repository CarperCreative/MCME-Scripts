package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.ai.goal.GoalType;
import com.mcmiddleearth.mcmescripts.condition.*;
import com.mcmiddleearth.mcmescripts.condition.proximity.LocationProximityCondition;
import com.mcmiddleearth.mcmescripts.condition.proximity.PlayerProximityCondition;
import com.mcmiddleearth.mcmescripts.condition.proximity.VirtualEntityProximityCondition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ConditionCompiler {

    private static final String KEY_CONDITION           = "condition",
                                KEY_CONDITION_ARRAY     = "conditions",

                                KEY_CONDITION_TYPE      = "type",
                                KEY_CONSTRAIN           = "criterion",
                                KEY_CENTER              = "center",
                                KEY_MATCH_ALL_SELECTED  = "match_all",
                                KEY_GOAL_TYPE           = "goal_type",
                                KEY_EXCLUDE             = "negate",
                                KEY_CURRENT_ANIMATION   = "current_animation",
                                KEY_MANUAL_ANIMATION    = "manual_animation",
                                KEY_INSTANT_SWITCHING   = "instant_animation_switching",
                                KEY_MANUAL_OVERRIDE     = "manual_animation_override",
                                KEY_TAG_NAME            = "tag_name",
                                KEY_VALUE               = "value",
                                KEY_POSITION        = "position",
                                KEY_SCOREBOARD_OBJECTIVE = "scoreboard_objective",
                                KEY_SCOREBOARD_PLAYER    = "scoreboard_player",
                                KEY_START               = "start",
                                KEY_END                 = "end",
                                KEY_WORLD               = "world",
                                KEY_TARGET              = "target",
                                KEY_PLAYER_TARGET       = "player_target",
                                KEY_ENTITY_TARGET       = "entity_target",
                                VALUE_TALK                  = "talk",
                                VALUE_NO_TALK               = "no_talk",
                                VALUE_GOAL_TYPE             = "goal_type",
                                VALUE_PROXIMITY_LOCATION    = "location_proximity",
                                VALUE_PROXIMITY_PLAYER      = "player_proximity",
                                VALUE_PROXIMITY_ENTITY      = "entity_proximity",
                                VALUE_ANIMATION             = "animation",
                                VALUE_PLAYER_ONLINE         = "player_online",
                                VALUE_SERVER_DAYTIME        = "server_daytime",
                                VALUE_HAS_TAG_VALUE         = "tag_value",
                                VALUE_SCOREBOARD_VALUE      = "scoreboard_value";

    public static Set<Condition> compile(JsonObject jsonData) {
        JsonElement conditions = jsonData.get(KEY_CONDITION);
        Set<Condition> result = new HashSet<>(compileConditions(conditions));
        conditions = jsonData.get(KEY_CONDITION_ARRAY);
        result.addAll(compileConditions(conditions));
        return result;
    }

    private static Set<Condition> compileConditions(JsonElement conditionData) {
        Set<Condition> result = new HashSet<>();
        if(conditionData==null) return result;
        if(conditionData.isJsonArray()) {
            for(int i = 0; i< conditionData.getAsJsonArray().size(); i++) {
                compileCondition(conditionData.getAsJsonArray().get(i).getAsJsonObject()).ifPresent(result::add);
            }
        } else {
            compileCondition(conditionData.getAsJsonObject()).ifPresent(result::add);
        }
        return result;
    }

    private static Optional<Condition> compileCondition(JsonObject jsonObject) {
        JsonElement type = jsonObject.get(KEY_CONDITION_TYPE);
        if(type==null)  return Optional.empty();

        try {
            switch (type.getAsString()) {
                case VALUE_NO_TALK -> {
                    JsonObject targetJson = jsonObject.getAsJsonObject(KEY_TARGET);
                    VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(targetJson);
                    NoTalkCondition condition = new NoTalkCondition(target);
                    getMatchAll(jsonObject).ifPresent(condition::setMatchAllSelected);
                    return Optional.of(condition);
                }
                case VALUE_TALK -> {
                    JsonObject targetJson = jsonObject.getAsJsonObject(KEY_TARGET);
                    VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(targetJson);
                    TalkCondition condition = new TalkCondition(target);
                    getMatchAll(jsonObject).ifPresent(condition::setMatchAllSelected);
                    return Optional.of(condition);
                }
                case VALUE_GOAL_TYPE -> {
                    JsonObject targetJson = jsonObject.getAsJsonObject(KEY_TARGET);
                    VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(targetJson);

                    JsonElement goalTypeJson = jsonObject.get(KEY_GOAL_TYPE);
                    if(goalTypeJson instanceof JsonPrimitive) {
                        try {
                            GoalType goalType = GoalType.valueOf(goalTypeJson.getAsString().toUpperCase());
                            boolean exclude = jsonObject.has(KEY_EXCLUDE) && jsonObject.get(KEY_EXCLUDE).getAsBoolean();
                            GoalTypeCondition goalTypeCondition = new GoalTypeCondition(target,
                                    goalType, exclude);
                            getMatchAll(jsonObject).ifPresent(goalTypeCondition::setMatchAllSelected);
                            return Optional.of(goalTypeCondition);
                        } catch (IllegalArgumentException ex) {
                            DebugManager.warn(Modules.Condition.create(ConditionCompiler.class),"Can't compile "+VALUE_GOAL_TYPE+" condition. Illegal goal type.");
                        }
                    } else {
                        DebugManager.warn(Modules.Condition.create(ConditionCompiler.class),"Can't compile "+VALUE_GOAL_TYPE+" condition. Missing goal type.");
                    }
                    return Optional.empty();
                }
                case VALUE_PROXIMITY_LOCATION -> {
                    JsonObject targetJson = jsonObject.getAsJsonObject(KEY_TARGET);
                    EntityEventTarget target = TargetCompiler.compileEntityTarget(targetJson);
                    EventPosition position = PositionCompiler.compile(jsonObject.getAsJsonObject(KEY_POSITION));
                    if(position==null) {
                        DebugManager.warn(Modules.Condition.create(ConditionCompiler.class),"Can't compile "+VALUE_PROXIMITY_LOCATION+" condition. Missing center location.");
                        return Optional.empty();
                    }
                    return Optional.of(new LocationProximityCondition(target,position, compileDoubleCriterion(jsonObject)));
                }
                case VALUE_PROXIMITY_PLAYER -> {
                    JsonObject playerTargetJson = jsonObject.getAsJsonObject(KEY_PLAYER_TARGET);
                    PlayerEventTarget playerTarget = TargetCompiler.compilePlayerTarget(playerTargetJson);
                    JsonObject entityTargetJson = jsonObject.getAsJsonObject(KEY_ENTITY_TARGET);
                    VirtualEntityEventTarget entityTarget = TargetCompiler.compileVirtualEntityTarget(entityTargetJson);
                    return Optional.of(new PlayerProximityCondition(playerTarget, entityTarget, compileDoubleCriterion(jsonObject)));
                }
                case VALUE_PROXIMITY_ENTITY -> {
                    JsonObject targetJson = jsonObject.getAsJsonObject(KEY_TARGET);
                    EntityEventTarget target = TargetCompiler.compileEntityTarget(targetJson);
                    String entityName = getName(jsonObject.get(KEY_CENTER));
                    if(entityName==null) {
                        DebugManager.warn(Modules.Condition.create(ConditionCompiler.class),"Can't compile "+VALUE_PROXIMITY_ENTITY+" condition. Missing center entity name.");
                        return Optional.empty();
                    }
                    return Optional.of(new VirtualEntityProximityCondition(target, entityName, compileCriterion(jsonObject)));
                }
                case VALUE_ANIMATION -> {
                    JsonObject targetJson = jsonObject.getAsJsonObject(KEY_TARGET);
                    VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(targetJson);
                    String current = getName(jsonObject.get(KEY_CURRENT_ANIMATION));
                    Boolean manualAnimation = getBoolean(jsonObject.get(KEY_MANUAL_ANIMATION));
                    Boolean manualOverride = getBoolean(jsonObject.get(KEY_MANUAL_OVERRIDE));
                    Boolean instantSwitching = getBoolean(jsonObject.get(KEY_INSTANT_SWITCHING));
                    return Optional.of(new AnimationCondition(target, current, manualAnimation,
                                                                instantSwitching, manualOverride));
                }
                case VALUE_SERVER_DAYTIME -> {
                    boolean exclude = jsonObject.has(KEY_EXCLUDE) && jsonObject.get(KEY_EXCLUDE).getAsBoolean();
                    JsonElement worldJson = jsonObject.get(KEY_WORLD);
                    if(!((worldJson instanceof JsonPrimitive) && Bukkit.getWorld(worldJson.getAsString()) != null)) {
                        DebugManager.warn(Modules.Condition.create(ConditionCompiler.class),"Can't compile "+VALUE_SERVER_DAYTIME+" condition. Missing or invalid world name.");
                        return Optional.empty();
                    }
                    World world = Bukkit.getWorld(worldJson.getAsString());
                    long startTick = -1, endTick = -1;
                    try {
                        startTick = jsonObject.get(KEY_START).getAsInt();
                        endTick = jsonObject.get(KEY_END).getAsInt();
                    } catch(NullPointerException | NumberFormatException ignore) {}
                    if(startTick == -1 || endTick == -1 || startTick >= endTick) {
                        DebugManager.warn(Modules.Condition.create(ConditionCompiler.class),"Can't compile "+VALUE_SERVER_DAYTIME+" condition. Missing or invalid time period.");
                        return Optional.empty();
                    }
                    return Optional.of(new ServerDaytimeCondition(world, startTick, endTick, exclude));
                }
                case VALUE_PLAYER_ONLINE -> {
                    JsonObject targetJson = jsonObject.getAsJsonObject(KEY_TARGET);
                    PlayerEventTarget target = TargetCompiler.compilePlayerTarget(targetJson);
                    return Optional.of(new OnlinePlayerCondition(target, compileCriterion(jsonObject)));
                }
                case VALUE_HAS_TAG_VALUE -> {
                    String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_TAG_NAME),null);
                    return Optional.of(new TagCriterionCondition(name, compileCriterion(jsonObject)));
                }
                case VALUE_SCOREBOARD_VALUE -> {
                    String playerName = PrimitiveCompiler.compileString(jsonObject.get(KEY_SCOREBOARD_PLAYER),null);
                    String objectiveName = PrimitiveCompiler.compileString(jsonObject.get(KEY_SCOREBOARD_OBJECTIVE),null);
                    return Optional.of(new ScoreboardValueCondition(playerName,objectiveName, compileCriterion(jsonObject)));
                }
            }
        } catch(NullPointerException ignore) {}
        return Optional.empty();
    }

    private static String getName(JsonElement element) {
        if(!(element instanceof JsonPrimitive)) return null;
        return element.getAsString();
    }

    private static Boolean getBoolean(JsonElement element) {
        if(!(element instanceof JsonPrimitive)) return null;
        return element.getAsBoolean();
    }

    private static Optional<Boolean> getMatchAll(JsonObject jsonObject) {
        JsonElement selectorJson = jsonObject.get(KEY_MATCH_ALL_SELECTED);
        if(selectorJson == null || selectorJson.isJsonPrimitive()) return Optional.empty();
        return Optional.of(selectorJson.getAsBoolean());
    }

    private static Criterion compileCriterion(JsonObject jsonObject) {
        JsonElement constrainData = jsonObject.get(KEY_CONSTRAIN);
        if(constrainData!=null) {
            try {
                String constrain = constrainData.getAsString();
                String comparator;
                int limit;
                if (constrain.charAt(1) == '=' || constrain.charAt(1) == '<') {
                    comparator = constrain.substring(0, 2);
                    limit = Integer.parseInt(constrain.substring(2));
                } else {
                    comparator = constrain.substring(0, 1);
                    limit = Integer.parseInt(constrain.substring(1));
                }
                return new Criterion(comparator,limit);

            } catch(NumberFormatException ignore) {}
        }
        DebugManager.warn(Modules.CONDITION_CREATE.getModule(), "Invalid criterion! Condition will always be true!");
        return new Criterion("true",0);//a -> true;
    }

    private static DoubleCriterion compileDoubleCriterion(JsonObject jsonObject) {
        JsonElement constrainData = jsonObject.get(KEY_CONSTRAIN);
        if(constrainData!=null) {
            try {
                String constrain = constrainData.getAsString();
                String comparator;
                double limit;
                if (constrain.charAt(1) == '=' || constrain.charAt(1) == '<') {
                    comparator = constrain.substring(0, 2);
                    limit = Double.parseDouble(constrain.substring(2));
                } else {
                    comparator = constrain.substring(0, 1);
                    limit = Double.parseDouble(constrain.substring(1));
                }
                return new DoubleCriterion(comparator,limit);

            } catch(NumberFormatException ignore) {}
        }
        DebugManager.warn(Modules.CONDITION_CREATE.getModule(), "Invalid criterion! Condition will always be true!");
        return new DoubleCriterion("true",0.0);//a -> true;
    }
}
