package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.ai.goal.GoalJockey;
import com.mcmiddleearth.entities.ai.goal.GoalType;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.api.VirtualEntityGoalFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.Placeholder;
import com.mcmiddleearth.mcmescripts.action.targeted.EntityTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.EEntityContainer;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.*;

public class SpawnRelativeAction extends EntityTargetedAction {

    //@SuppressWarnings("All")
    public SpawnRelativeAction(EntityEventTarget target, List<VirtualEntityFactory> factories, int lifespan, boolean onGround,
                               EntityEventTarget goalTargets, VirtualEntityGoalFactory goalFactory,
                               Location location, Location[] waypoints, boolean serverSide, int quantity, int xEdge, int spread) {
        super(target, (entity,context) -> {
            DebugManager.verbose(Modules.Action.execute(SpawnRelativeAction.class),"Selected entity: "+entity.getName());

            McmeEntity goalTarget = goalTargets.getTargets(context).stream().findFirst().orElse(null);

            for(int j = 0; j< quantity; j++) {
                Location finalLocation;
                if(location!=null) {
                    finalLocation =location.clone().add((j % xEdge)*spread,0, (j / xEdge) *spread);
                } else {
                    finalLocation = null;
                }
                context.getDescriptor().addLine("Spawning: ").indent();
                factories.forEach(factory -> {
                    if (finalLocation != null) {
                        Location loc = entity.getLocation().clone().add(rotate(finalLocation.toVector(), entity));
                        factory.withLocation(findSafe(loc, onGround));
                    }
                    VirtualEntityGoalFactory tempGoalFactory = goalFactory;
                    if (tempGoalFactory == null || (factory.getGoalFactory()!=null && factory.getGoalFactory().getGoalType().equals(GoalType.JOCKEY))) {
                        tempGoalFactory = factory.getGoalFactory();
                    }
                    if (tempGoalFactory != null && goalTarget != null && !tempGoalFactory.getGoalType().equals(GoalType.JOCKEY)) {
                        tempGoalFactory.withTargetEntity(goalTarget);
                    }
                    if (tempGoalFactory != null && waypoints != null) {
                        Location[] checkpoints = new Location[waypoints.length];
                        for (int i = 0; i < waypoints.length; i++) {
                            checkpoints[i] = findSafe(entity.getLocation().clone()
                                    .add(rotate(waypoints[i].toVector(), entity)), onGround);
                        }
                        tempGoalFactory.withCheckpoints(checkpoints);
                    }
                    context.getDescriptor().addLine("Type: "+factory.getType().name())
                            .addLine("Location: "+factory.getLocation())
                            .addLine("Goal : "+(tempGoalFactory!=null && tempGoalFactory.getGoalType()!=null?
                                    tempGoalFactory.getGoalType().name():"--none--"));
                    factory.withGoalFactory(tempGoalFactory);
                });
                context.getDescriptor().outdent();
                Set<McmeEntity> entities = SpawnAction.spawnEntity(context, factories, lifespan, null,null, EEntityContainer.GLOBAL, serverSide);
                new HashSet<>(entities).stream().filter(jockey->jockey.getGoal() !=null && jockey.getGoal() instanceof GoalJockey)
                        .forEach(jockey -> {
                            GoalJockey goal = (GoalJockey)jockey.getGoal();
                            McmeEntity placeholder = goal.getSteed();
                            if(placeholder instanceof Placeholder) {
                                UUID uuid = placeholder.getUniqueId();
                                entities.stream().filter(steed -> steed.getUniqueId().equals(uuid)).findFirst()
                                        .ifPresent(steed -> {
                                            goal.setSteed(steed);
                                            context.getDescriptor().addLine("Mounting "+jockey.getName()+" on "+steed.getName());
                                        });
                            }
                        });
            }
        });
        getDescriptor().indent()
                .addLine("Lifespan: "+lifespan)
                .addLine("On ground: "+onGround)
                .addLine("Location: "+location)
                .addLine("Server side: "+serverSide)
                .addLine("Quantity: "+quantity)
                .addLine("Edge length: "+xEdge)
                .addLine("Spread: "+spread)
                .addLine("Goal: "+(goalFactory!=null && goalFactory.getGoalType()!=null?goalFactory.getGoalType().name():"--none--"))
                .addLine("Goal target selector: "+(goalTargets != null ? goalTargets.toString() : "--none--"));
        if(waypoints!=null && waypoints.length>0) {
            getDescriptor().addLine("Waypoints: ").indent();
            Arrays.stream(waypoints).forEach(waypoint -> getDescriptor().addLine(""+waypoint));
            getDescriptor().outdent();
        } else {
            getDescriptor().addLine("Waypoints: --none--");
        }
        if(!factories.isEmpty()) {
            getDescriptor().addLine("Enitities: ").indent();
            factories.forEach(factory -> getDescriptor().addLine(factory.getType().name()).indent()
                    .addLine("Relative position: "+factory.getLocation()).outdent());
            getDescriptor().outdent();
        }
        getDescriptor().outdent();

    }

    private static Location findSafe(Location location, boolean onGround) {
        Block block = location.getBlock();
        while(!(isSafe(block) && isSafe(block.getRelative(BlockFace.UP))) && block.getY()-location.getBlockY()<10) {
            block = block.getRelative(BlockFace.UP);
        }
        if(onGround) {
            while ((isSafe(block) && isSafe(block.getRelative(BlockFace.DOWN))) && block.getY() - location.getBlockY() > -10) {
                block = block.getRelative(BlockFace.DOWN);
            }
        }
        return block.getLocation().add(new Vector(0.5,0,0.5));
    }

    private static boolean isSafe(Block block) {
        return block.isPassable() && !block.isLiquid();
    }

    private static Vector rotate(Vector vector, McmeEntity entity) {
        float yaw = entity.getYaw();
        while(yaw < -180) yaw += 360; while(yaw > 180) yaw -= 360;
        if(yaw < -135 || yaw > 135) {
            return new Vector(-vector.getX(),vector.getY(),-vector.getZ());
        } else if(yaw < -45) {
            return new Vector(vector.getZ(),vector.getY(),-vector.getX());
        } else if(yaw > 45) {
            return new Vector(-vector.getZ(),vector.getY(),vector.getX());
        } else {
            return vector;
        }
    }
}
