package com.mcmiddleearth.mcmescripts.selector;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.ai.goal.Goal;
import com.mcmiddleearth.entities.ai.goal.GoalType;
import com.mcmiddleearth.entities.api.McmeEntityType;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.entities.composite.SpeechBalloonEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Selector {

    private final String selector;

    protected SelectorType selectorType;
    protected int limit = Integer.MAX_VALUE;
    protected double x,y,z;
    protected boolean xRelative = true, yRelative = true, zRelative = true;
    protected double dx = -1, dy = -1, dz = -1;
    protected double minDistanceSquared = -1, maxDistanceSquared = Double.MAX_VALUE;
    protected McmeEntityType entityType;
    protected boolean excludeType;
    protected String name;
    protected boolean excludeName;
    protected GameMode gameMode;
    protected boolean excludeGameMode;
    protected float minPitch = -90, maxPitch = 90;
    protected float minYaw = -180, maxYaw = 180;
    protected GoalType goalType;
    protected boolean excludeGoalType;
    protected String talking;
    protected Set<String> tags = new HashSet<>();

    public Selector(String selector) throws IndexOutOfBoundsException {
        this.selector = selector;
        selector = selector.replace(" ","");
        switch (selector.charAt(1)) {
            case 'a' -> selectorType = SelectorType.ALL_PLAYERS;
            case 'e' -> selectorType = SelectorType.ALL_ENTITIES;
            case 'v' -> selectorType = SelectorType.VIRTUAL_ENTITIES;
            case 's' -> selectorType = SelectorType.TRIGGER_SELF;
            case 'r' -> selectorType = SelectorType.RANDOM_PLAYER;
            case 'p' -> selectorType = SelectorType.NEAREST_PLAYER;
            default -> selectorType = SelectorType.NEAREST_PLAYER;
        }
        if(selector.length()>2) {
            if(selector.charAt(2) != '[' || selector.charAt(selector.length()-1)!=']') return;
            String[] arguments = selector.substring(3,selector.length()-1).split(",");
            for(String argument : arguments) {
                String[] split = argument.split("=");
                switch(split[0]) {
                    case "limit":
                        limit = Integer.parseInt(split[1]);
                        break;
                    case "x":
                        if (split[1].startsWith("~")) {
                            split[1] = split[1].substring(1);
                        } else {
                            xRelative = false;
                        }
                        if (split[1].length() > 0) {
                            x = Double.parseDouble(split[1]);
                        }
                        break;
                    case "y":
                        if (split[1].startsWith("~")) {
                            split[1] = split[1].substring(1);
                        } else {
                            yRelative = false;
                        }
                        if (split[1].length() > 0) {
                            y = Double.parseDouble(split[1]);
                        }
                        break;
                    case "z":
                        if (split[1].startsWith("~")) {
                            split[1] = split[1].substring(1);
                        } else {
                            zRelative = false;
                        }
                        if (split[1].length() > 0) {
                            z = Double.parseDouble(split[1]);
                        }
                        break;
                    case "dx":
                        dx = Double.parseDouble(split[1]);
                        break;
                    case "dy":
                        dy = Double.parseDouble(split[1]);
                        break;
                    case "dz":
                        dz = Double.parseDouble(split[1]);
                        break;
                    case "distance":
                        String[] minMax = split[1].split("\\.\\.");
                        double minDistance = Double.parseDouble(minMax[0]);
                        minDistanceSquared = minDistance * minDistance;
                        if(minMax.length > 1) {
                            double maxDistance = Double.parseDouble(minMax[1]);
                            maxDistanceSquared = maxDistance * maxDistance;
                        } else {
                            maxDistanceSquared = minDistanceSquared;
                        }
                        break;
                    case "type":
                        if(split[1].startsWith("!")) {
                            entityType = McmeEntityType.valueOf(split[1].substring(1));
                            excludeType = true;
                        } else {
                            entityType = McmeEntityType.valueOf(split[1]);
                        }
                        break;
                    case "name":
                        if(split[1].startsWith("!")) {
                            name = split[1].substring(1);
                            excludeName = true;
                        } else {
                            name = split[1];
                        }
                        break;
                    case "gamemode":
                        if(split[1].startsWith("1")) {
                            gameMode = GameMode.valueOf(split[1].substring(1).toUpperCase());
                            excludeGameMode = true;
                        } else {
                            gameMode = GameMode.valueOf(split[1].toUpperCase());
                        }
                        break;
                    case "x_rotation":
                        minMax = split[1].split("\\.\\.");
                        minPitch = Float.parseFloat(minMax[0]);
                        if(minMax.length > 1) {
                            maxPitch = Float.parseFloat(minMax[1]);
                        } else {
                            maxPitch = minPitch;
                        }
                        break;
                    case "y_rotation":
                        minMax = split[1].split("\\.\\.");
                        minYaw = Float.parseFloat(minMax[0]);
                        if(minMax.length > 1) {
                            maxYaw = Float.parseFloat(minMax[1]);
                        } else {
                            maxYaw = minYaw;
                        }
                        break;
                    case "goal_type":
                        if(split[1].startsWith("!")) {
                            goalType = GoalType.valueOf(split[1].substring(1).toUpperCase());
                            excludeGoalType = true;
                        } else {
                            goalType = GoalType.valueOf(split[1].toUpperCase());
                        }
                        break;
                    case "talking":
                        talking = split[1];
                        break;
                    case "tag":
                        tags.add(split[1]);
                        break;
                }
            }
        }
    }

    public List<McmeEntity> selectAll(TriggerContext context) {
        List<McmeEntity> result = new ArrayList<>();
        switch (selectorType) {
            case ALL_ENTITIES -> {
                // Add virtual entities
                result.addAll(selectOnlyVirtualEntities(context));

                // Add players represented as virtual entities (As far as I can tell)
                selectOnlyPlayers(context).stream().map(EntitiesPlugin.getEntityServer().getPlayerProvider()::getOrCreateMcmePlayer)
                        .forEach(result::add);

                Vector eventPos = new Vector(0,0,0); // TODO: Rethink the concept of having an event position? I don't think its intuitive though. Maybe we can add a selector type in the editor with a position picker

                if (minDistanceSquared > 0 || maxDistanceSquared < Double.MAX_VALUE) {
                    eventPos = new Vector(getAbsolute(eventPos.getX(), xRelative, x),
                            getAbsolute(eventPos.getY(), yRelative, y),
                            getAbsolute(eventPos.getZ(), zRelative, z));

                    Location finalLoc = new Location(context.getWorld(), eventPos.getX(),eventPos.getY(),eventPos.getZ());
                    List<EntitySelectorElement<McmeEntity>> sort = result.stream().map(EntitySelectorElement<McmeEntity>::new)
                            .filter(element -> {
                                element.setValue(element.getContent().getLocation().distanceSquared(finalLoc));
                                return minDistanceSquared <= element.getValue()
                                        && element.getValue() <= maxDistanceSquared;
                            }).collect(Collectors.toList());
                    result = sort.stream().sorted((one, two) -> (Double.compare(two.getValue(), one.getValue()))).limit(limit)
                            .map(EntitySelectorElement::getContent).collect(Collectors.toList());
                }
            }
            case NEAREST_PLAYER, ALL_PLAYERS, RANDOM_PLAYER ->
                    selectOnlyPlayers(context).stream().map(EntitiesPlugin.getEntityServer().getPlayerProvider()::getOrCreateMcmePlayer)
                            .forEach(result::add);
            case VIRTUAL_ENTITIES -> result.addAll(selectOnlyVirtualEntities(context));
            case TRIGGER_SELF -> {
                if (context.getEntity() != null) result.add(context.getEntity());
                if (context.getPlayer() != null) {
                    McmeEntity realPlayer = EntitiesPlugin.getEntityServer().getPlayerProvider().getOrCreateMcmePlayer(context.getPlayer());
                    if (!realPlayer.equals(context.getEntity())) {
                        result.add(realPlayer);
                    }
                }
            }
            default -> DebugManager.warn(Modules.Selector.select(this.getClass()),
                    "Selector: " + getSelector()
                            + " Invalid McmeEntity selector type!");
        }
        return result;
    }

    public List<Player> selectOnlyPlayers(TriggerContext context) {
        Vector eventPos = new Vector(0,0,0); // TODO: Rethink the concept of having an event position? I don't think its intuitive though. Maybe we can add a selector type in the editor with a position picker
        List<Player> players = new ArrayList<>();
        switch (selectorType) {
            case TRIGGER_SELF -> {
                if (context.getPlayer() != null)
                    players.add(context.getPlayer());
            }
            case NEAREST_PLAYER, ALL_PLAYERS, ALL_ENTITIES, RANDOM_PLAYER -> {
                //if party is set in context, select players from that party only
                if (context.isQuestContext()) {
                    players.addAll(context.getParty().getOnlinePlayers());
                } else {
                    players.addAll(Bukkit.getOnlinePlayers());
                }
            }
            default -> {
                DebugManager.warn(Modules.Selector.select(this.getClass()),
                        "Selector: " + getSelector()
                                + " Invalid player selector type!");
                return Collections.emptyList();
            }
        }
        eventPos = new Vector(getAbsolute(eventPos.getX(), xRelative, x),
                getAbsolute(eventPos.getY(), yRelative, y),
                getAbsolute(eventPos.getZ(), zRelative, z));
        if (hasAreaLimit()) {
            World world = context.getWorld();
            double xMin = (dx < 0 ? Integer.MIN_VALUE : eventPos.getX() - dx);
            double xMax = (dx < 0 ? Integer.MAX_VALUE : eventPos.getX() + dx);
            double yMin = (dy < 0 ? Integer.MIN_VALUE : eventPos.getY() - dy);
            double yMax = (dy < 0 ? Integer.MAX_VALUE : eventPos.getY() + dy);
            double zMin = (dz < 0 ? Integer.MIN_VALUE : eventPos.getZ() - dz);
            double zMax = (dz < 0 ? Integer.MAX_VALUE : eventPos.getZ() + dz);
            players = players.stream().filter(player -> player.getLocation().getWorld().equals(world)
                            && xMin <= player.getLocation().getX()
                            && player.getLocation().getX() < xMax
                            && yMin <= player.getLocation().getY()
                            && player.getLocation().getY() < yMax
                            && zMin <= player.getLocation().getZ()
                            && player.getLocation().getZ() < zMax)
                    .collect(Collectors.toList());
        }
        if (name != null) {
            if (name.endsWith("*")) {
                players = players.stream().filter(player -> player.getName()
                                .startsWith(name.substring(0, name.length() - 1)) != excludeName)
                        .collect(Collectors.toList());
            } else {
                players = players.stream().filter(player -> player.getName().equals(name) != excludeName)
                        .collect(Collectors.toList());
            }
        }
        if (gameMode != null) {
            players = players.stream().filter(player -> player.getGameMode().equals(gameMode) != excludeGameMode)
                    .collect(Collectors.toList());
        }
        if (minPitch > -90 || maxPitch < 90) {
            players = players.stream().filter(player -> minPitch <= player.getLocation().getPitch()
                            && player.getLocation().getPitch() < maxPitch)
                    .collect(Collectors.toList());
        }
        if (minYaw > -180 || maxYaw < 180) {
            players = players.stream().filter(player -> minYaw <= player.getLocation().getPitch()
                    && player.getLocation().getPitch() < maxYaw).collect(Collectors.toList());
        }
        for(String tag: tags) {
            players = players.stream().filter(player -> EntitiesPlugin.getEntityServer().getOrCreateMcmePlayer(player).hasTag(tag))
                    .collect(Collectors.toList());
        }
        List<EntitySelectorElement<Player>> sort = players.stream().map(EntitySelectorElement<Player>::new)
                .collect(Collectors.toList());
        if (minDistanceSquared > 0 || maxDistanceSquared < Double.MAX_VALUE) {
            Location finalLoc = new Location(context.getWorld(),eventPos.getX(),eventPos.getY(),eventPos.getZ());
            sort = sort.stream()
                    .filter(element -> {
                        if (finalLoc.getWorld() == null
                                || !finalLoc.getWorld().equals(element.getContent().getWorld())) return false;
                        element.setValue(element.getContent().getLocation().distanceSquared(finalLoc));
                        return minDistanceSquared <= element.getValue()
                                && element.getValue() <= maxDistanceSquared;
                    }).collect(Collectors.toList());
        }
        List<Player> result = switch (selectorType) {
            case NEAREST_PLAYER ->
                    sort.stream().sorted((one, two) -> (Double.compare(two.getValue(), one.getValue()))).limit(1)
                            .map(EntitySelectorElement::getContent).collect(Collectors.toList());
            case RANDOM_PLAYER ->
                    Collections.singletonList(sort.get(new Random().nextInt(sort.size())).getContent());
            case ALL_PLAYERS, ALL_ENTITIES, TRIGGER_SELF ->
                    sort.stream().sorted((one, two) -> (Double.compare(two.getValue(), one.getValue()))).limit(limit)
                            .map(EntitySelectorElement::getContent).collect(Collectors.toList());
            default -> Collections.emptyList();
        };
        //DebugManager.verbose(Modules.Selector.select(this.getClass()),
        //        "Selector: " + getSelector()
        //                + " Selected: " + (result.size() > 0 ? result.get(0).getName() : null) + " and total of " + result.size());
        return result;
    }

    public List<VirtualEntity> selectOnlyVirtualEntities(TriggerContext context) {
        Location loc = new Location(context.getWorld(),0,0,0); // TODO: Rethink event pos

        loc = new Location(loc.getWorld(),getAbsolute(loc.getX(),xRelative,x),
                getAbsolute(loc.getY(),yRelative,y),
                getAbsolute(loc.getZ(),zRelative,z));

        List<VirtualEntity> entities = new ArrayList<>();
        switch(selectorType) {
            case TRIGGER_SELF:
                if(context.getEntity()!=null && (context.getEntity() instanceof VirtualEntity))
                    entities.add((VirtualEntity) context.getEntity());
                break;
            case VIRTUAL_ENTITIES:
            case ALL_ENTITIES:
                if(hasAreaLimit()) {
                    World world = loc.getWorld();
                    entities.addAll(EntitiesPlugin.getEntityServer().getEntitiesAt(loc,
                            (dx<0?Integer.MAX_VALUE:(int)dx),
                            (dy<0?Integer.MAX_VALUE:(int)dy),
                            (dz<0?Integer.MAX_VALUE:(int)dz))
                            .stream().filter(entity -> entity instanceof VirtualEntity
                                            && entity.getLocation().getWorld().equals(world))
                            .map(entity -> (VirtualEntity)entity).collect(Collectors.toSet()));
                } else {
                    entities.addAll(EntitiesPlugin.getEntityServer().getEntities(VirtualEntity.class)
                            .stream().map(entity -> {
                                return (VirtualEntity)entity;
                            }).collect(Collectors.toSet()));
                }
                break;
            default:
                DebugManager.warn(Modules.Selector.select(this.getClass()),
                        "Selector: "+getSelector()
                                +" Invalid virtual entity selector type!");

        }
        entities = entities.stream().filter(entity -> !(entity instanceof SpeechBalloonEntity)
                                                    && (entityType == null || entity.getType().equals(entityType) != excludeType))
                .collect(Collectors.toList());

        //filtering
        if(name!=null) {
            if(name.endsWith("*")) {
                entities = entities.stream().filter(entity -> {
                    return entity.getName()
                            .startsWith(name.substring(0,name.length()-1)) != excludeName;
                })
                        .collect(Collectors.toList());
            } else {
                entities = entities.stream().filter(entity -> {
                    return entity.getName() != null && entity.getName().equals(name) != excludeName;
                })
                        .collect(Collectors.toList());
            }
        }
        if(minPitch>-90 || maxPitch < 90) {
            entities = entities.stream().filter(entity -> minPitch <= entity.getPitch() && entity.getPitch() < maxPitch)
                    .collect(Collectors.toList());
        }
        if(minYaw>-180 || maxYaw < 180) {
            entities = entities.stream().filter(entity -> minYaw <= entity.getYaw() && entity.getYaw() < maxYaw)
                    .collect(Collectors.toList());
        }
        if(goalType!=null) {
            entities = entities.stream().filter(entity -> {
                Goal goal = entity.getGoal();
                return goal!=null && goal.getType().equals(goalType) != excludeGoalType;
            }).collect(Collectors.toList());
        }
        if(talking != null) {
            entities = entities.stream().filter(entity -> entity.isTalking() == talking.equals("true"))
                    .collect(Collectors.toList());
        }
        for(String tag: tags) {
            entities = entities.stream().filter(entity -> entity.hasTag(tag))
                    .collect(Collectors.toList());
        }

        //sorting
        List<EntitySelectorElement<VirtualEntity>> sort = entities.stream().map(EntitySelectorElement<VirtualEntity>::new)
                .collect(Collectors.toList());
        if(minDistanceSquared > 0 || maxDistanceSquared < Double.MAX_VALUE) {
            Location finalLoc = loc;
            sort = sort.stream()
                    .filter(element -> {
                        if(!element.getContent().getLocation().getWorld().equals(finalLoc.getWorld())) return false;
                        element.setValue(element.getContent().getLocation().distanceSquared(finalLoc));
                        return minDistanceSquared <= element.getValue()
                                && element.getValue() <= maxDistanceSquared;
                    }).collect(Collectors.toList());
        }
        List<VirtualEntity> result = sort.stream().sorted((one,two) -> (Double.compare(two.getValue(), one.getValue()))).limit(limit)
                .map(EntitySelectorElement::getContent).collect(Collectors.toList());
        return result;
    }

    public boolean hasAreaLimit() {
        return dx >= 0 || dy >=  0 || dz >= 0;
    }

    public double getAbsolute(double trigger, boolean relative, double selector) {
        if(relative) {
            return trigger+selector;
        } else {
            return selector;
        }
    }

    public String getSelector() {
        return selector;
    }

    public enum SelectorType {
        NEAREST_PLAYER,
        RANDOM_PLAYER,
        ALL_PLAYERS,
        VIRTUAL_ENTITIES,
        ALL_ENTITIES,
        TRIGGER_SELF
    }

    public static enum Order {
        NEAREST, FURTHEST, RANDOM, ARBITRARY;
    }

    public static class EntitySelectorElement<T> {
        private final T content;
        private double value;

        public EntitySelectorElement(T content) {
            this.content = content;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public T getContent() {
            return content;
        }
    }


}
