package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.api.MovementType;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.effect.Explosion;
import com.mcmiddleearth.entities.entities.composite.bones.SpeechBalloonLayout;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.*;
import com.mcmiddleearth.mcmescripts.action.bossbattle.GoToRandomTimelineAction;
import com.mcmiddleearth.mcmescripts.action.bossbattle.RestartTimelineAction;
import com.mcmiddleearth.mcmescripts.action.bossbattle.GoToTimelineAction;
import com.mcmiddleearth.mcmescripts.action.quest.StageDisableAction;
import com.mcmiddleearth.mcmescripts.action.quest.StageEnableAction;
import com.mcmiddleearth.mcmescripts.action.tag.TagAddToAction;
import com.mcmiddleearth.mcmescripts.action.tag.TagDeleteAction;
import com.mcmiddleearth.mcmescripts.action.tag.TagSetAction;
import com.mcmiddleearth.mcmescripts.bossbattle.TimelineConfiguration;
import com.mcmiddleearth.mcmescripts.component.ItemFilter;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.EEntityContainer;
import com.mcmiddleearth.mcmescripts.event.EventGoalFactory;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.rotation.EventRotation;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;
import com.mcmiddleearth.mcmescripts.event.target.selector.PlayerSelectorTarget;
import com.mcmiddleearth.mcmescripts.looting.LootTableChoice;
import com.mcmiddleearth.mcmescripts.quest.tags.IntegerTag;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ActionCompiler {

    public static final String  KEY_ACTION          = "action",
                                KEY_ACTION_ARRAY    = "actions",
                                KEY_TARGET          = "target",

                                KEY_ACTION_TYPE     = "type",
                                KEY_DELAY           = "delay",
                                KEY_GOAL_TARGET     = "goal_target",
                                KEY_GOAL            = "goal",
                                KEY_MOVEMENT_TYPE   = "movement_type",
                                KEY_TRIGGER_NAME    = "name",
                                KEY_TELEPORT_SPREAD = "spread",
                                KEY_PARTICLE_DURATION = "duration",
                                KEY_PARTICLE_SPREAD = "spread",
                                KEY_PARTICLE_SPREAD_X = "spread_x",
                                KEY_PARTICLE_SPREAD_Y = "spread_y",
                                KEY_PARTICLE_SPREAD_Z = "spread_z",
                                KEY_PARTICLE_SPEED  = "speed",
                                KEY_PARTICLE_AMOUNT = "amount",
                                KEY_PARTICLE        = "particle",
                                KEY_POTION_EFFECT   = "potion_effect",
                                KEY_POTION_EFFECTS  = "potion_effects",
                                KEY_TIME            = "time",
                                KEY_STATE           = "state",
                                KEY_ANIMATION       = "animation",
                                KEY_OVERRIDE        = "override",
                                KEY_ITEM            = "item",
                                KEY_ITEMS           = "items",
                                KEY_ITEM_FILTER     = "item_filter",
                                KEY_ITEM_FILTERS    = "item_filters",
                                KEY_ENCHANTMENT     = "enchantment",
                                KEY_ENCHANTMENTS    = "enchantments",
                                KEY_SLOT            = "slot",
                                KEY_SLOT_ID         = "slot_id",
                                KEY_DURATION        = "duration",
                                KEY_COMMAND         = "command",
                                KEY_PROBABILITY     = "probability",
                                KEY_GROUP           = "group",
                                KEY_RADIUS          = "radius",
                                KEY_QUANTITY        = "quantity",
                                KEY_CHOICES         = "choices",
                                KEY_WEIGHT          = "weight",
                                KEY_CENTER          = "center",
                                KEY_SOUND_FILE      = "sound_file",
                                KEY_SOUND_ID        = "sound_id",
                                KEY_SOUND           = "sound",
                                KEY_VOLUME          = "volume",
                                KEY_PITCH           = "pitch",
                                KEY_CATEGORY        = "category",
                                KEY_LIFESPAN        = "lifespan",
                                KEY_SPAWN_POSITION  = "spawn_position",
                                KEY_SPAWN_ROTATION  = "spawn_rotation",
                                KEY_OVERRIDE_NAME   = "override_name",
                                KEY_SPAWN_LOCATION  = "spawn_location",
                                KEY_CONTAINER       = "container",
                                KEY_DROP_HEIGHT     = "drop_height",
                                KEY_TITLE           = "title",
                                KEY_SUBTITLE        = "subtitle",
                                KEY_FADE_IN         = "fade_in",
                                KEY_STAY            = "stay",
                                KEY_FADE_OUT        = "fade_out",
                                KEY_ON_GROUND       = "on_ground",
                                KEY_NAME            = "name",
                                KEY_INDEX           = "index",
                                KEY_VISIBLE         = "visible",
                                KEY_STYLE           = "style",
                                KEY_COLOR           = "color",
                                KEY_FOG             = "fog",
                                KEY_DARKEN          = "darken",
                                KEY_MUSIC           = "music",
                                KEY_PROGRESS        = "progress",
                                KEY_LOCATION        = "location",
                                KEY_CHECKPOINTS     = "checkpoints",
                                KEY_BUKKIT_ENTITY   = "server_side",
                                KEY_ENEMIES         = "enemies",
                                KEY_X_EDGE          = "x_edge",
                                KEY_SPREAD          = "spread",
                                KEY_PARTICLES       = "particles",
                                KEY_ATTRIBUTE       = "attribute",
                                KEY_ATTRIBUTE_VALUE = "attribute_value",
                                KEY_MODIFIER        = "modifier",
                                KEY_MODIFIER_NAME   = "modifier_name",
                                KEY_GOAL_CONTROLLED = "goal_controlled",
                                KEY_TURNING_SPEED   = "turning_speed",
                                KEY_TICKS           = "ticks",

                                KEY_TIMELINE_CONFIGURATION         = "timeline_configuration",
                                KEY_TIMELINE_CONFIGURATION_CHOICES = "timeline_configuration_choices",

                                KEY_TAG_VALUE       = "value",
                                KEY_POSITION        = "position",
                                KEY_ROTATION        = "rotation",

                                VALUE_ADD_ATTRIBUTE_MODIFIER         = "add_attribute_modifier",
                                VALUE_REMOVE_ATTRIBUTE_MODIFIER      = "remove_attribute_modifier",
                                VALUE_SET_ATTRIBUTE_VALUE            = "set_attribute_value",
                                VALUE_SET_GOAL_CONTROLS_HEAD         = "set_goal_controls_head",
                                VALUE_TURNING_SPEED                  = "set_turning_speed",

                                VALUE_REGISTER_TRIGGER      = "register_event",
                                VALUE_UNREGISTER_TRIGGER    = "unregister_event",

                                VALUE_SET_GOAL              = "set_goal",
                                VALUE_SET_MOVEMENT_TYPE     = "set_movement_type",
                                VALUE_SET_ENEMIES           = "set_enemies",
                                VALUE_SPAWN                 = "spawn",
                                VALUE_SPAWN_RELATIVE        = "spawn_relative",
                                VALUE_DESPAWN               = "despawn",
                                VALUE_STOP_TALK             = "stop_talk",
                                VALUE_TALK                  = "talk",
                                VALUE_TELEPORT              = "teleport",
                                VALUE_ADD_POTION_EFFECT     = "add_potion_effect",
                                VALUE_REMOVE_POTION_EFFECT  = "remove_potion_effect",
                                VALUE_SET_SERVER_TIME       = "set_server_time",
                                VALUE_ENTITY_STATE          = "entity_state",
                                VALUE_ANIMATION             = "animation",
                                VALUE_GIVE_ITEM             = "give_item",
                                VALUE_REMOVE_ITEM           = "remove_item",
                                VALUE_EYE_EFFECT            = "eye_effect",
                                VALUE_EXECUTE_COMMAND       = "execute_command",
                                VALUE_FIREWORK              = "firework",
                                VALUE_EXPLOSION             = "explosion",
                                VALUE_RANDOM_SPAWN          = "random_spawn",
                                VALUE_MCME_SOUND_START      = "start_mcme_sound",
                                VALUE_MCME_SOUND_STOP       = "stop_mcme_sound",
                                VALUE_SOUND_START           = "start_sound",
                                VALUE_SOUND_STOP            = "stop_sound",
                                VALUE_GIVE_CHEST            = "give_chest",
                                VALUE_RAIN_ITEM             = "rain_item",
                                VALUE_TITLE                 = "title",
                                VALUE_ACTION_BAR            = "action_bar",
                                VALUE_ADD_ENCHANTMENT       = "add_enchantment",
                                VALUE_REMOVE_ENCHANTMENT    = "remove_enchantment",
                                VALUE_BOSS_BAR_ADD          = "boss_bar_add",
                                VALUE_BOSS_BAR_REMOVE       = "boss_bar_remove",
                                VALUE_BOSS_BAR_EDIT         = "boss_bar_edit",

                                VALUE_STAGE_ENABLE          = "enable_stage",
                                VALUE_STAGE_DISABLE         = "disable_stage",
                                VALUE_TAG_SET               = "set_tag",
                                VALUE_TAG_ADD_TO            = "add_to_tag",
                                VALUE_TAG_DELETE            = "delete_tag",
                                VALUE_GO_TO_TIMELINE        = "go_to_timeline",
                                VALUE_GO_TO_RANDOM_TIMELINE = "go_to_random_timeline",
                                VALUE_RESTART_TIMELINE      = "restart_timeline",
                                VALUE_SPRAY_PARTICLES       = "spray_particles",
                                VALUE_PARTICLES             = "particles",
                                VALUE_SET_ON_FIRE           = "set_on_fire";


    public static Collection<Action> compile(JsonObject jsonData) {
        JsonElement actionData = jsonData.get(KEY_ACTION);
        Set<Action> result = new HashSet<>(compileActions(actionData));
        actionData = jsonData.get(KEY_ACTION_ARRAY);
        result.addAll(compileActions(actionData));
        return result;
    }

    private static Set<Action> compileActions(JsonElement actionData) {
        Set<Action> result = new HashSet<>();
        if(actionData == null) return result;
        if(actionData.isJsonArray()) {
            for(int i = 0; i< actionData.getAsJsonArray().size(); i++) {
                compileAction(actionData.getAsJsonArray().get(i).getAsJsonObject()).ifPresent(result::add);
            }
        } else {
            compileAction(actionData.getAsJsonObject()).ifPresent(result::add);
        }
        return result;
    }

    private static Optional<Action> compileAction(JsonObject jsonObject) {
        Action action;
        JsonElement type = jsonObject.get(KEY_ACTION_TYPE);
        if (type == null) {
            DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile action. Missing: "+KEY_ACTION_TYPE);
            return Optional.empty();
        }

        switch (type.getAsString()){
            case VALUE_REGISTER_TRIGGER -> {
                Set<Trigger> triggers = TriggerCompiler.compile(jsonObject);
                if(triggers.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_REGISTER_TRIGGER+" action. Missing event.");
                    return Optional.empty();
                }
                action = new TriggerRegisterAction(triggers);
            }
            case VALUE_UNREGISTER_TRIGGER -> {
                Set<String> triggerNames = compileTriggerNames(jsonObject);
                if(triggerNames.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_UNREGISTER_TRIGGER+" action. Missing events.");
                    return Optional.empty();
                }
                action = new TriggerUnregisterAction(triggerNames);
            }
            case VALUE_SET_GOAL -> {
                Optional<VirtualEntityGoalFactory> goalFactoryOpt = VirtualEntityGoalFactoryCompiler.compile(jsonObject);
                if(goalFactoryOpt.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_SET_GOAL+" action. Missing goal.");
                    return Optional.empty();
                }
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));

                EventGoalFactory eventGoalFactory = VirtualEntityGoalFactoryCompiler.compileFromEditor(jsonObject.getAsJsonObject(KEY_GOAL)).orElse(null);

                action = new SetGoalAction(target, eventGoalFactory);
            }
            case VALUE_SET_MOVEMENT_TYPE -> {
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));

                MovementType movementType = MovementType.valueOf(jsonObject.get(KEY_MOVEMENT_TYPE).getAsString());

                action = new SetMovementTypeAction(target, movementType);
            }
            case VALUE_SET_ENEMIES -> {
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                EntityEventTarget enemyTarget = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_ENEMIES));
                action = new SetEnemyAction(target, enemyTarget);
            }
            case VALUE_SPRAY_PARTICLES -> {
                EventPosition position = PositionCompiler.compile(jsonObject.getAsJsonObject(KEY_POSITION));
                EventRotation rotation = RotationCompiler.compile(jsonObject.getAsJsonObject(KEY_ROTATION));
                double spread = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PARTICLE_SPREAD),0.5);
                double speed = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PARTICLE_SPEED),1);
                int duration = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_PARTICLE_DURATION),40);
                int amount = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_PARTICLE_AMOUNT),5);

                Particle particleType = Particle.valueOf(jsonObject.get(KEY_PARTICLE).getAsString());

                action = new SprayParticlesAction(position,rotation,particleType,duration,(float)speed,(float)spread, amount);
            }
            case VALUE_PARTICLES -> {
                EventPosition position = PositionCompiler.compile(jsonObject.getAsJsonObject(KEY_POSITION));
                double spreadX = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PARTICLE_SPREAD_X),0.5);
                double spreadY = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PARTICLE_SPREAD_Y),0.5);
                double spreadZ = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PARTICLE_SPREAD_Z),0.5);
                double speed = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PARTICLE_SPEED),1);
                int duration = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_PARTICLE_DURATION),40);
                int amount = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_PARTICLE_AMOUNT),5);

                Particle particleType = Particle.valueOf(jsonObject.get(KEY_PARTICLE).getAsString());

                action = new ParticlesAction(position,particleType,duration,(float)speed,new Vector(spreadX,spreadY,spreadZ), amount);
            }
            case VALUE_SPAWN -> {
                List<VirtualEntityFactory> factories = VirtualEntityFactoryCompiler.compile(jsonObject);
                if(factories.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_SPAWN+" action. Missing entity factory.");
                    return Optional.empty();
                }
                String overrideName = PrimitiveCompiler.compileString(jsonObject.get(KEY_OVERRIDE_NAME),"");
                if(!overrideName.equals("")){
                    factories = factories.stream().map(f -> f.withDisplayName(overrideName)).collect(Collectors.toList());
                }

                SpawnAction.ESpawnLocations spawnLocation = SpawnAction.ESpawnLocations.valueOf(PrimitiveCompiler.compileString(jsonObject.get(KEY_SPAWN_LOCATION),"from_file").toUpperCase());
                EEntityContainer container = EEntityContainer.valueOf(PrimitiveCompiler.compileString(jsonObject.get(KEY_CONTAINER),"global").toUpperCase());
                int lifespan = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_LIFESPAN),-1);
                boolean serverSide = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_BUKKIT_ENTITY),false);


                EventPosition position = null;
                EventRotation rotation = null;
                if (spawnLocation == SpawnAction.ESpawnLocations.CUSTOM) {
                    JsonElement positionJson = jsonObject.get(KEY_SPAWN_POSITION);
                    if (!(positionJson instanceof JsonObject)) {
                        DebugManager.warn(Modules.Action.create(ActionCompiler.class), "Can't compile " + VALUE_SPAWN + " action. Missing spawn position.");
                        return Optional.empty();
                    }
                    position = PositionCompiler.compile(positionJson.getAsJsonObject());

                    JsonElement rotationJson = jsonObject.get(KEY_SPAWN_ROTATION);
                    if (!(rotationJson instanceof JsonObject)) {
                        DebugManager.warn(Modules.Action.create(ActionCompiler.class), "Can't compile " + VALUE_SPAWN + " action. Missing spawn rotation.");
                        return Optional.empty();
                    }
                    rotation = RotationCompiler.compile(rotationJson.getAsJsonObject());
                }
                action = new SpawnAction(factories, lifespan, position, rotation, container, serverSide);
                //TODO: optional - set Goal
            }
            case VALUE_SPAWN_RELATIVE -> {
                List<VirtualEntityFactory> factories = VirtualEntityFactoryCompiler.compile(jsonObject);
                if(factories.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_SPAWN_RELATIVE+" action. Missing entity factory.");
                    return Optional.empty();
                }
                int lifespan = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_LIFESPAN),-1);
                Location location = LocationCompiler.compile(jsonObject.get(KEY_LOCATION)).orElse(null);
                JsonElement checkpointJson = jsonObject.get(KEY_CHECKPOINTS);
                boolean onGround = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_ON_GROUND),true);
                VirtualEntityGoalFactory goalFactory = VirtualEntityGoalFactoryCompiler.compile(jsonObject).orElse(null);
                Location[] checkpoints = null;
                if(goalFactory!=null) {
                    List<Location> waypoints = new ArrayList<>();
                    if (checkpointJson instanceof JsonArray) {
                        for (JsonElement element : checkpointJson.getAsJsonArray()) {
                            LocationCompiler.compile(element).ifPresent(waypoints::add);
                        }
                        if (waypoints.isEmpty()) {
                            DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Warning while parsing "+VALUE_SPAWN_RELATIVE
                                    +" action. Found empty checkpoint array.");
                        } else {
                            checkpoints = waypoints.toArray(new Location[0]);
                        }
                    }
                }
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                EntityEventTarget goalTarget = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_GOAL_TARGET));
                int quantity = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_QUANTITY),1);
                boolean serverSide = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_BUKKIT_ENTITY),false);

                int spreading = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_SPREAD),1);
                int xEdge = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_X_EDGE),(int) Math.sqrt(quantity));
                action = new SpawnRelativeAction(target, factories, lifespan, onGround,
                        goalTarget, goalFactory, location, checkpoints, serverSide,
                        quantity,xEdge, spreading);
            }
            case VALUE_DESPAWN -> {
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                boolean particles = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_PARTICLES),false);
                action = new DespawnAction(target,particles);
            }
            case VALUE_STOP_TALK -> {
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                action = new StopTalkAction(target);
            }
            case VALUE_TALK -> {
                SpeechBalloonLayout layout = SpeechBalloonLayoutCompiler.compile(jsonObject);
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                action = new TalkAction(target,layout);
            }
            case VALUE_TELEPORT -> {
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                EventPosition position = PositionCompiler.compile(jsonObject.getAsJsonObject(KEY_POSITION));
                EventRotation rotation = RotationCompiler.compile(jsonObject.getAsJsonObject(KEY_ROTATION));
                double spread = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_TELEPORT_SPREAD),0);
                action = new TeleportAction(target, position,rotation,spread);
            }
            case VALUE_ADD_POTION_EFFECT -> {
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));

                Set<PotionEffect> effects = new HashSet<>();

                JsonElement effectJson = jsonObject.get(KEY_POTION_EFFECT);
                if(effectJson instanceof JsonObject){
                    effects.add(PotionEffectCompiler.compile(effectJson));
                }

                JsonElement effectsJson = jsonObject.get(KEY_POTION_EFFECTS);
                if(effectsJson instanceof JsonArray){
                    effectsJson.getAsJsonArray().forEach(element -> {
                        if(element instanceof JsonObject) {
                            effects.add(PotionEffectCompiler.compile(element));
                        }
                    });
                }

                Set<PotionEffectAddAction.PotionEffectChoice> potionEffectChoices = new HashSet<>();
                JsonElement effectChoicesJson = jsonObject.get(KEY_CHOICES);
                if(effectChoicesJson instanceof JsonArray) {
                    effectChoicesJson.getAsJsonArray().forEach(element -> {
                        if(element instanceof JsonObject) {
                            int weight = PrimitiveCompiler.compileInteger(element.getAsJsonObject().get(KEY_WEIGHT),10);
                            PotionEffect choiceEffect = PotionEffectCompiler.compile(element.getAsJsonObject().get(KEY_POTION_EFFECT));
                            if(choiceEffect!=null) {
                                potionEffectChoices.add(new PotionEffectAddAction.PotionEffectChoice(choiceEffect, weight));
                            } else {
                                DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't parse potion effect in random choice.");
                            }
                        }
                    });
                }
                if(effects.isEmpty() && potionEffectChoices.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_ADD_POTION_EFFECT+" action. Missing potion effect.");
                    return Optional.empty();
                }
                action = new PotionEffectAddAction(target, effects, potionEffectChoices);
            }
            case VALUE_REMOVE_POTION_EFFECT -> {
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));

                Set<PotionEffect> effects = new HashSet<>();
                JsonElement effectJson = jsonObject.get(KEY_POTION_EFFECT);
                if(effectJson instanceof JsonObject){
                    effects.add(PotionEffectCompiler.compile(effectJson));
                }

                JsonElement effectsJson = jsonObject.get(KEY_POTION_EFFECTS);
                if(effectsJson instanceof JsonArray){
                    effectsJson.getAsJsonArray().forEach(element -> {
                        if(element instanceof JsonObject) {
                            effects.add(PotionEffectCompiler.compile(element));
                        }
                    });
                }

                if(effects.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_REMOVE_POTION_EFFECT+" action. Missing potion effect.");
                    return Optional.empty();
                }
                action = new PotionEffectRemoveAction(target, effects);
            }
            case VALUE_SET_SERVER_TIME -> {
                JsonElement timeJson = jsonObject.get(KEY_TIME);
                if(! (timeJson instanceof JsonPrimitive)) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_SET_SERVER_TIME+" action. Missing server time.");
                    return Optional.empty();
                }
                long serverTime = timeJson.getAsLong();
                action = new ServerTimeAction(serverTime);
            }
            case VALUE_ENTITY_STATE -> {
                JsonElement stateJson = jsonObject.get(KEY_STATE);
                if(! (stateJson instanceof JsonPrimitive)) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_ENTITY_STATE+" action. Missing entity state.");
                    return Optional.empty();
                }
                String state = stateJson.getAsString();
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                action = new EntityStateAction(target, state);
            }
            case VALUE_ANIMATION -> {
                JsonElement animationJson = jsonObject.get(KEY_ANIMATION);
                if(! (animationJson instanceof JsonPrimitive)) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_ANIMATION+" action. Missing animation name.");
                    return Optional.empty();
                }
                String animationName = animationJson.getAsString();
                JsonElement overrideJson = jsonObject.get(KEY_OVERRIDE);
                boolean override = true;
                if(overrideJson instanceof JsonPrimitive) {
                    override = overrideJson.getAsBoolean();
                }
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                action = new AnimationAction(target, animationName, override);
            }
            case VALUE_GIVE_ITEM -> {
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                Set<ItemStack> items = ItemCompiler.compile(jsonObject.get(KEY_ITEM));
                items.addAll(ItemCompiler.compile(jsonObject.get(KEY_ITEMS)));
                Set<LootTableChoice<ItemStack>> lootTableChoices = LootTableCompiler.compileItemChoices(jsonObject).orElse(new HashSet<>());
                if(items.isEmpty() && lootTableChoices.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class), "No items found for "+VALUE_GIVE_ITEM+" action.");
                    return Optional.empty();
                }
                EquipmentSlot slot = null;
                JsonElement slotJson = jsonObject.get(KEY_SLOT);
                if(slotJson instanceof JsonPrimitive) {
                    try {
                        slot = EquipmentSlot.valueOf(slotJson.getAsString().toUpperCase());
                    } catch (IllegalArgumentException ex) {
                        DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Illegal equipment slot for "+VALUE_GIVE_ITEM+" action. Using main hand slot.");
                        slot = EquipmentSlot.HAND;
                    }
                }
                int slotId = -1;
                JsonElement slotIdJson = jsonObject.get(KEY_SLOT_ID);
                if(slotIdJson instanceof JsonPrimitive) {
                    try {
                        slotId = slotIdJson.getAsInt();
                    } catch(NumberFormatException ex) {
                        DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't parse slot id for action "+VALUE_GIVE_ITEM+".");
                    }
                }
                int duration = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_DURATION),-1);
                action = new ItemGiveAction(target, items, lootTableChoices, slot, slotId, duration);
            }
            case VALUE_ADD_ENCHANTMENT -> {
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                Set<WrappedEnchantment> enchantments = EnchantmentCompiler.compile(jsonObject.get(KEY_ENCHANTMENT));
                enchantments.addAll(EnchantmentCompiler.compile(jsonObject.get(KEY_ENCHANTMENTS)));
                Set<LootTableChoice<WrappedEnchantment>> enchantmentChoices = LootTableCompiler.compileEnchantmentChoices(jsonObject).orElse(new HashSet<>());
                if(enchantments.isEmpty() && enchantmentChoices.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_ADD_ENCHANTMENT+". Missing enchantment.");
                    return Optional.empty();
                }

                Set<ItemFilter> itemFilters = ItemFilterCompiler.compile(jsonObject.get(KEY_ITEM_FILTER));
                itemFilters.addAll(ItemFilterCompiler.compile(jsonObject.get(KEY_ITEM_FILTERS)));

                int quantity = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_QUANTITY),-1);
                int duration = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_DURATION),-1);

                action = new EnchantmentAddAction(target, itemFilters, enchantments, enchantmentChoices, quantity, duration);
            }
            case VALUE_REMOVE_ENCHANTMENT -> {
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                Set<WrappedEnchantment> enchantments = EnchantmentCompiler.compile(jsonObject.get(KEY_ENCHANTMENT));
                enchantments.addAll(EnchantmentCompiler.compile(jsonObject.get(KEY_ENCHANTMENTS)));
                Set<LootTableChoice<WrappedEnchantment>> enchantmentChoices = LootTableCompiler.compileEnchantmentChoices(jsonObject).orElse(new HashSet<>());
                if(enchantments.isEmpty() && enchantmentChoices.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_REMOVE_ENCHANTMENT+". Missing enchantment.");
                    return Optional.empty();
                }

                Set<ItemFilter> itemFilters = ItemFilterCompiler.compile(jsonObject.get(KEY_ITEM_FILTER));
                itemFilters.addAll(ItemFilterCompiler.compile(jsonObject.get(KEY_ITEM_FILTERS)));

                int quantity = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_QUANTITY),-1);
                action = new EnchantmentRemoveAction(target, itemFilters, enchantments, enchantmentChoices, quantity);
            }
            case VALUE_REMOVE_ITEM -> {
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                Set<ItemStack> items = ItemCompiler.compile(jsonObject.get(KEY_ITEM));
                items.addAll(ItemCompiler.compile(jsonObject.get(KEY_ITEMS)));
                if(items.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_REMOVE_ITEM+". Missing item.");
                    return Optional.empty();
                }
                action = new ItemRemoveAction(target, items);
            }
            case VALUE_EYE_EFFECT -> {
                PlayerEventTarget target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                int duration = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_DURATION),200);
                action = new EyeEffectAction(target, duration);
            }
            case VALUE_EXECUTE_COMMAND -> {
                JsonElement commandJson = jsonObject.get(KEY_COMMAND);
                if(!(commandJson instanceof JsonPrimitive)) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_EXECUTE_COMMAND+" action. Missing command line.");
                    return Optional.empty();
                }
                String command = commandJson.getAsString();

                EventPosition executePosition = null;
                JsonElement executePositionJson = jsonObject.get(KEY_POSITION);
                if(executePositionJson instanceof JsonObject) {
                    executePosition = PositionCompiler.compile(executePositionJson.getAsJsonObject());
                }
                EventRotation executeRotation = null;
                JsonElement executeRotationJson = jsonObject.get(KEY_ROTATION);
                if(executeRotationJson instanceof JsonObject) {
                    executeRotation = RotationCompiler.compile(executeRotationJson.getAsJsonObject());
                }
                action = new ExecuteCommandAction(command, executePosition, executeRotation);
            }
            case VALUE_FIREWORK -> {
                Location location = LocationCompiler.compile(jsonObject.get(KEY_LOCATION)).orElse(null);
                FireworkMeta fireworkMeta = FireworkMetaCompiler.compile(jsonObject);
                /*if(fireworkMeta == null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_FIREWORK+" action. Missing firework meta.");
                    return Optional.empty();
                }*/
                action = new FireworkAction(location, fireworkMeta);
            }
            case VALUE_EXPLOSION -> {
                Explosion explosion = ExplosionCompiler.compile(jsonObject);
                if(explosion == null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_EXPLOSION+" action. Missing explosion data.");
                    return Optional.empty();
                }
                EntityEventTarget unaffected = ExplosionCompiler.getUnaffectedTarget(jsonObject);
                EntityEventTarget damager = ExplosionCompiler.getDamagerTarget(jsonObject);
                action = new ExplosionAction(explosion, unaffected, damager);
            }
            case VALUE_RANDOM_SPAWN -> {
                JsonElement choicesJson = jsonObject.get(KEY_CHOICES);
                if(!(choicesJson instanceof JsonArray)) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_RANDOM_SPAWN+". Missing entity.");
                    return Optional.empty();
                }
                List<SpawnRandomSelectionAction.Choice> choices = new ArrayList<>();
                for(JsonElement choiceJson: choicesJson.getAsJsonArray()) {
                    List<VirtualEntityFactory> factories = VirtualEntityFactoryCompiler.compile(choiceJson.getAsJsonObject());
                    int weight = PrimitiveCompiler.compileInteger(choiceJson.getAsJsonObject().get(KEY_WEIGHT), 10);
                    choices.add(new SpawnRandomSelectionAction.Choice(weight, factories));
                }
                double probability = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PROBABILITY),1);
                boolean group = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_GROUP),true);
                int minRadius = PrimitiveCompiler.compileLowerInt(jsonObject.get(KEY_RADIUS),5);
                int maxRadius = PrimitiveCompiler.compileUpperInt(jsonObject.get(KEY_RADIUS),10);
                int minQuantity = PrimitiveCompiler.compileLowerInt(jsonObject.get(KEY_QUANTITY),2);
                int maxQuantity = PrimitiveCompiler.compileUpperInt(jsonObject.get(KEY_QUANTITY),5);
                boolean serverSide = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_BUKKIT_ENTITY),false);
                SpawnRandomSelectionAction.RandomSpawnData randomSpawnData = new SpawnRandomSelectionAction.RandomSpawnData(choices, serverSide)
                        .withMinQuantity(minQuantity).withMaxQuantity(maxQuantity)
                        .withMinRadius(minRadius).withMaxRadius(maxRadius)
                        .withProbability(probability).withGroup(group);
                JsonElement goalTargetJson = jsonObject.get(KEY_GOAL_TARGET);
                if(goalTargetJson instanceof JsonPrimitive) {
                    randomSpawnData.withGoalTargetSelector(new Selector(goalTargetJson.getAsString()));
                }
                VirtualEntityGoalFactoryCompiler.compile(jsonObject).ifPresent(randomSpawnData::withGoalFactory);
                int lifespan = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_LIFESPAN),-1);
                Location center = LocationCompiler.compile(jsonObject.get(KEY_CENTER)).orElse(null);
                if(center != null) {
                    action = new SpawnRandomLocationAction(center, randomSpawnData, lifespan);
                } else {
                    EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                    action = new SpawnRandomSelectionAction(target,randomSpawnData, lifespan);
                }
            }
            case VALUE_MCME_SOUND_START -> {
                PlayerEventTarget target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                String musicFile = PrimitiveCompiler.compileString(jsonObject.get(KEY_SOUND_FILE),null);
                if(musicFile == null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+ VALUE_MCME_SOUND_START +". Missing music file.");
                    return Optional.empty();
                }
                String musicId = PrimitiveCompiler.compileString(jsonObject.get(KEY_SOUND_ID),null);
                action = new OpenAudioSoundPlayAction(target,musicFile, musicId);
            }
            case VALUE_MCME_SOUND_STOP -> {
                PlayerEventTarget target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                String musicId = PrimitiveCompiler.compileString(jsonObject.get(KEY_SOUND_ID),null);
                action = new OpenAudioSoundStopAction(target, musicId);
            }
            case VALUE_SOUND_START -> {
                PlayerEventTarget target;
                if(jsonObject.get(KEY_TARGET) != null){
                    target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                }
                else {
                    target = new PlayerSelectorTarget(new Selector("@a"));
                }

                String sound = PrimitiveCompiler.compileString(jsonObject.get(KEY_SOUND),null);
                String category = PrimitiveCompiler.compileString(jsonObject.get(KEY_CATEGORY),null);

                double pitch = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PITCH),null);
                double volume = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_VOLUME),null);

                EventPosition position = PositionCompiler.compile(jsonObject.getAsJsonObject(KEY_POSITION));
                action = new SoundPlayAction(target,sound, SoundCategory.valueOf(category),position,(float) volume,(float) pitch);
            }
            case VALUE_SOUND_STOP -> {
                PlayerEventTarget target;
                if(jsonObject.get(KEY_TARGET) != null){
                    target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                }
                else {
                    target = new PlayerSelectorTarget(new Selector("@a"));
                }

                String sound = PrimitiveCompiler.compileString(jsonObject.get(KEY_SOUND),null);
                String category = PrimitiveCompiler.compileString(jsonObject.get(KEY_CATEGORY),null);

                action = new SoundStopAction(target,sound, category);
            }
            case VALUE_GIVE_CHEST -> {
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                int duration = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_DURATION),1200);
                Set<ItemStack> items = ItemCompiler.compile(jsonObject.get(KEY_ITEM));
                items.addAll(ItemCompiler.compile(jsonObject.get(KEY_ITEMS)));
                Set<LootTableChoice<ItemStack>> lootTableChoices = LootTableCompiler.compileItemChoices(jsonObject).orElse(new HashSet<>());
                if(items.isEmpty() && lootTableChoices.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_GIVE_CHEST+". Missing items.");
                    return Optional.empty();
                }
                action = new GiveChestAction(target,items, lootTableChoices,duration);
            }
            case VALUE_RAIN_ITEM -> {
                Set<ItemStack> items = ItemCompiler.compile(jsonObject.get(KEY_ITEM));
                items.addAll(ItemCompiler.compile(jsonObject.get(KEY_ITEMS)));
                if(items.isEmpty()) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_RAIN_ITEM+". Missing item.");
                    return Optional.empty();
                }
                EntityEventTarget target = TargetCompiler.compileEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                int duration = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_DURATION),200);
                double probability = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PROBABILITY),0.5);
                int radius = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_RADIUS),10);
                int drop_height = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_DROP_HEIGHT),5);
                action = new ItemRainAction(target,items,radius,drop_height,probability,duration);
            }
            case VALUE_TITLE -> {
                PlayerEventTarget target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                String title = PrimitiveCompiler.compileString(jsonObject.get(KEY_TITLE),"").replace('&','§').replace('#','§');
                String subtitle = PrimitiveCompiler.compileString(jsonObject.get(KEY_SUBTITLE),"").replace('&','§').replace('#','§');
                if(title.equals("") && subtitle.equals("")) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_TITLE+" action. Missing title and subtitle.");
                    return Optional.empty();
                }
                int fadeIn = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_FADE_IN),10);
                int stay = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_STAY),70);
                int fadeout = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_FADE_OUT),20);
                action = new TitleAction(target,title,subtitle,fadeIn,stay,fadeout);
            }
            case VALUE_ACTION_BAR -> {
                PlayerEventTarget target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                String title = PrimitiveCompiler.compileString(jsonObject.get(KEY_TITLE),"").replace('&','§').replace('#','§');
                if(title.equals("")) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),
                            "Can't compile "+VALUE_ACTION_BAR+" action. Missing title.");
                    return Optional.empty();
                }
                action = new ActionBarAction(target,title);
            }
            case VALUE_BOSS_BAR_ADD -> {
                PlayerEventTarget target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_NAME),null);
                if(name==null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_BOSS_BAR_ADD+" action. Missing bar name.");
                    return Optional.empty();
                }
                NamespacedKey barKey = new NamespacedKey(MCMEScripts.getInstance(),name);
                BossBar bar = Bukkit.getBossBar(barKey);
                String title = PrimitiveCompiler.compileString(jsonObject.get(KEY_TITLE),null).replace('&','§').replace('#','§');
                Boolean fog = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_FOG),null);
                Boolean dark = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_DARKEN),null);
                Boolean music = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_MUSIC),null);
                Double progress = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PROGRESS),null);
                Boolean visible = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_VISIBLE),null);
                BarColor color = BossBarCompiler.compileBarColor(jsonObject.get(KEY_COLOR));
                BarStyle style = BossBarCompiler.compileBarStyle(jsonObject.get(KEY_STYLE));
                if(bar == null) {
                    BarFlag[] flags = BossBarCompiler.compileBarFlags(fog,dark,music);
                    if(color == null) color = BarColor.RED;
                    if(style == null) style = BarStyle.SOLID;
                    bar = Bukkit.createBossBar(barKey, title, color, style, flags);
                }
                BossBarEditAction.editBar(bar,title,style,color,fog,dark,music,progress,visible);
                action = new BossBarAddAction(target, bar);
            }
            case VALUE_BOSS_BAR_EDIT -> {
                String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_NAME),null);
                if(name==null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_BOSS_BAR_EDIT+" action. Missing bar name.");
                    return Optional.empty();
                }
                action = new BossBarEditAction(new NamespacedKey(MCMEScripts.getInstance(),name),
                        PrimitiveCompiler.compileString(jsonObject.get(KEY_TITLE),null),
                        BossBarCompiler.compileBarColor(jsonObject.get(KEY_COLOR)),
                        BossBarCompiler.compileBarStyle(jsonObject.get(KEY_STYLE)),
                        PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_FOG),null),
                        PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_DARKEN),null),
                        PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_MUSIC),null),
                        PrimitiveCompiler.compileDouble(jsonObject.get(KEY_PROGRESS),null),
                        PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_VISIBLE),null));
            }
            case VALUE_BOSS_BAR_REMOVE -> {
                String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_NAME),null);
                if(name==null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_BOSS_BAR_REMOVE+" action. Missing bar name.");
                    return Optional.empty();
                }
                PlayerEventTarget target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                action = new BossBarRemoveAction(target, new NamespacedKey(MCMEScripts.getInstance(),name));
            }
            case VALUE_STAGE_ENABLE -> {
                String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_NAME),null);
                if(name==null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_STAGE_ENABLE+" action. Missing stage name.");
                    return Optional.empty();
                }
                action = new StageEnableAction(name);
            }
            case VALUE_STAGE_DISABLE -> {
                String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_NAME),null);
                if(name==null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_STAGE_DISABLE+" action. Missing stage name.");
                    return Optional.empty();
                }
                action = new StageDisableAction(name);
            }
            case VALUE_TAG_SET -> {
                String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_NAME),null);
                int value = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_TAG_VALUE), 0);
                if(name==null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_TAG_SET+" action. Missing tag name.");
                    return Optional.empty();
                }
                action = new TagSetAction(new IntegerTag(name,value));
            }
            case VALUE_TAG_ADD_TO -> {
                String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_NAME),null);
                int value = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_TAG_VALUE), 0);
                if(name==null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_TAG_ADD_TO+" action. Missing tag name.");
                    return Optional.empty();
                }
                action = new TagAddToAction(name,value);
            }
            case VALUE_TAG_DELETE -> {
                String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_NAME),null);
                if(name==null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_TAG_DELETE+" action. Missing tag name.");
                    return Optional.empty();
                }
                action = new TagDeleteAction(name);
            }
            case VALUE_GO_TO_TIMELINE -> {
                String name = PrimitiveCompiler.compileString(jsonObject.get(KEY_NAME),null);
                int index = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_INDEX),0);
                if(name==null) {
                    DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Can't compile "+VALUE_GO_TO_TIMELINE+" action. Missing timeline name.");
                    return Optional.empty();
                }
                action = new GoToTimelineAction(new TimelineConfiguration(name,index));
            }
            case VALUE_GO_TO_RANDOM_TIMELINE -> {
                Set<LootTableChoice<TimelineConfiguration>> timelineConfigurationChoices = LootTableCompiler.compileTimelineChoices(jsonObject).orElse(new HashSet<>());

                action = new GoToRandomTimelineAction(timelineConfigurationChoices);
            }
            case VALUE_RESTART_TIMELINE -> {
                action = new RestartTimelineAction();
            }
            case VALUE_ADD_ATTRIBUTE_MODIFIER -> {
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));

                String attribute = PrimitiveCompiler.compileString(jsonObject.get(KEY_ATTRIBUTE),null);
                AttributeModifier modifier = AttributeModifierCompiler.compile(jsonObject.get(KEY_MODIFIER));

                action = new AttributeAddModifierAction(target, Attribute.valueOf(attribute),modifier);
            }
            case VALUE_REMOVE_ATTRIBUTE_MODIFIER -> {
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));

                String attribute = PrimitiveCompiler.compileString(jsonObject.get(KEY_ATTRIBUTE),null);
                String modifierName = PrimitiveCompiler.compileString(jsonObject.get(KEY_MODIFIER_NAME),null);

                action = new AttributeRemoveModifierAction(target,Attribute.valueOf(attribute),modifierName);
            }
            case VALUE_SET_ATTRIBUTE_VALUE -> {
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));

                String attribute = PrimitiveCompiler.compileString(jsonObject.get(KEY_ATTRIBUTE),null);
                double value = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_ATTRIBUTE_VALUE),null);

                action = new AttributeSetValueAction(target,Attribute.valueOf(attribute),value);
            }
            case VALUE_SET_GOAL_CONTROLS_HEAD -> {
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));

                boolean control = PrimitiveCompiler.compileBoolean(jsonObject.get(KEY_GOAL_CONTROLLED),null);

                action = new SetHeadControlledByGoalAction(target,control);
            }
            case VALUE_TURNING_SPEED -> {
                VirtualEntityEventTarget target = TargetCompiler.compileVirtualEntityTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                double turningSpeed = PrimitiveCompiler.compileDouble(jsonObject.get(KEY_TURNING_SPEED),null);


                action = new SetTurningSpeedAction(target,(float)turningSpeed);
            }
            case VALUE_SET_ON_FIRE -> {
                PlayerEventTarget target = TargetCompiler.compilePlayerTarget(jsonObject.getAsJsonObject(KEY_TARGET));
                int ticks = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_TICKS),20);

                action = new SetOnFireAction(target,ticks);
            }
            default -> {
                DebugManager.severe(Modules.Action.create(ActionCompiler.class),"Can't compile action. Unsupported action type.");
                return Optional.empty();
            }
        }
        JsonElement delayJson = jsonObject.get(KEY_DELAY);
        if(delayJson instanceof JsonPrimitive) {
            try {
                action.setDelay(delayJson.getAsInt());
            } catch(ClassCastException ex) {
                DebugManager.warn(Modules.Action.create(ActionCompiler.class),"Invalid numeric data for action delay. Using delay 0!");
            }
        }
        return Optional.of(action);
    }

    private static Set<String> compileTriggerNames(JsonObject jsonObject) {
        JsonElement nameJson = jsonObject.get(KEY_TRIGGER_NAME);
        if(nameJson != null) {
            if (nameJson.isJsonPrimitive()) {
                return Collections.singleton(nameJson.getAsString());
            } else if (nameJson.isJsonArray()) {
                Set<String> result = new HashSet<>();
                nameJson.getAsJsonArray().forEach(element -> result.add(element.getAsString()));
                return result;
            }
        }
        return Collections.emptySet();
    }

}
