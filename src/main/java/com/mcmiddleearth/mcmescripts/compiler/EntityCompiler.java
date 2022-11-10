package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.mcmescripts.ConfigKeys;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.*;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.condition.Criterion;
import com.mcmiddleearth.mcmescripts.condition.proximity.LocationProximityCondition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.selector.VirtualEntitySelector;
import com.mcmiddleearth.mcmescripts.trigger.DecisionTreeTrigger;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.timed.PeriodicServerTimeTrigger;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class EntityCompiler {

    private static final String KEY_ENTITY            = "entity",
                                KEY_ENTITY_ARRAY      = "entities",

                                KEY_SPAWN_DISTANCE    = "spawn_range",
                                KEY_VIEW_DISTANCE     = "view_distance";

    private static final int DEFAULT_SPAWN_DISTANCE   = 64;

    public static Set<Trigger> compile(JsonObject jsonData) {
        Collection<Condition> conditions = ConditionCompiler.compile(jsonData);
        JsonElement entities = jsonData.get(KEY_ENTITY);

        Set<Trigger> triggers = new HashSet<>(compileEntities(entities, conditions));
        entities = jsonData.get(KEY_ENTITY_ARRAY);

        triggers.addAll(compileEntities(entities, conditions));
        return triggers;
    }

    private static Set<Trigger> compileEntities(JsonElement entities, Collection<Condition> conditions) {
        Set<Trigger> triggers = new HashSet<>();
        if(entities == null) return triggers;
        if(entities.isJsonArray()) {
            for(int i = 0; i< entities.getAsJsonArray().size(); i++) {
                compileEntity(entities.getAsJsonArray().get(i).getAsJsonObject(), conditions).ifPresent(triggers::add);
            }
        } else {
            compileEntity(entities.getAsJsonObject(), conditions).ifPresent(triggers::add);
        }
        return triggers;
    }

    private static Optional<Trigger> compileEntity(JsonObject jsonObject, Collection<Condition> conditions) {
        List<VirtualEntityFactory> factories = VirtualEntityFactoryCompiler.compile(jsonObject);//.get(KEY_SPAWN_DATA));
        if(factories.isEmpty())  return Optional.empty();

        JsonElement spawnDistanceData = jsonObject.get(KEY_SPAWN_DISTANCE);
        int spawnDistance = DEFAULT_SPAWN_DISTANCE;
        if(spawnDistanceData != null) {
            spawnDistance = spawnDistanceData.getAsInt();
        }

        Set<Trigger> triggers = TriggerCompiler.compile(jsonObject);
        DecisionTreeTrigger spawnTrigger = new PeriodicServerTimeTrigger(null, MCMEScripts.getConfigInt(ConfigKeys.TRIGGER_CHECKER_PERIOD,10));
        DecisionTreeTrigger despawnTrigger = new PeriodicServerTimeTrigger(null, MCMEScripts.getConfigInt(ConfigKeys.TRIGGER_CHECKER_PERIOD,10));

        JsonElement viewDistanceJson = jsonObject.get(KEY_VIEW_DISTANCE);
        if(viewDistanceJson instanceof JsonPrimitive) {
            try {
                int viewDistance = viewDistanceJson.getAsInt();
                for (VirtualEntityFactory factory : factories) {
                    factory.withViewDistance((int) (viewDistance));
                }
            } catch(ClassCastException ex) {
                DebugManager.warn(Modules.Trigger.create(EntityCompiler.class),"Can't read view distance. Number format exception.");
            }
        }

        Set<Action> spawnActions = new HashSet<>();
        spawnActions.add(new SpawnAction(factories, -1,false));
        triggers.forEach(trigger -> spawnActions.add(new TriggerRegisterAction(trigger)));
        spawnActions.add(new TriggerRegisterAction(despawnTrigger));
        spawnActions.add(new TriggerUnregisterAction(spawnTrigger));
        DecisionTreeTrigger.DecisionNode spawnNode = new DecisionTreeTrigger.DecisionNode(spawnActions);

        if (conditions != null && !conditions.isEmpty()) {
            conditions.forEach(spawnNode::addCondition);
        } else {
            spawnNode.addCondition(new LocationProximityCondition(factories.get(0).getLocation(),new PlayerSelector("@a[distance=0.."+spawnDistance+"]"),
                new Criterion(">",0)));
        }
        spawnTrigger.setDecisionNode(spawnNode);

        Set<Action> despawnActions = new HashSet<>();
        despawnActions.add(new DespawnAction(new VirtualEntitySelector("@e[name="+VirtualEntityFactoryCompiler.getGroupName(factories)+"*]")));
        triggers.forEach(trigger -> despawnActions.add(new TriggerUnregisterAction(trigger)));
        despawnActions.add(new TriggerRegisterAction(spawnTrigger));
        despawnActions.add(new TriggerUnregisterAction(despawnTrigger));
        DecisionTreeTrigger.DecisionNode despawnNode = new DecisionTreeTrigger.DecisionNode(despawnActions);
        if (conditions != null && !conditions.isEmpty()) {
            for (Condition condition : conditions) {
                if (condition instanceof LocationProximityCondition) {
                    LocationProximityCondition proximityCondition = (LocationProximityCondition) condition;

                    despawnNode.addCondition(
                        new LocationProximityCondition(
                            proximityCondition.getLocation(),
                            proximityCondition.getSelector(),
                            new Criterion("==",0))
                    );
                } else {
                    despawnNode.addCondition(condition);
                }
            }

            conditions.forEach(spawnNode::addCondition);
        } else {
            despawnNode.addCondition(new LocationProximityCondition(factories.get(0).getLocation(),new PlayerSelector("@a[distance=0.."+spawnDistance+"]"),
                new Criterion("==",0)));
        }

        despawnTrigger.setDecisionNode(despawnNode);

        return Optional.of(spawnTrigger);
    }

}
