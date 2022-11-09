package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.ai.goal.Goal;
import com.mcmiddleearth.entities.api.VirtualEntityFactory;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.exception.InvalidDataException;
import com.mcmiddleearth.entities.exception.InvalidLocationException;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.EEntityContainer;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.rotation.EventRotation;
import com.mcmiddleearth.mcmescripts.event.rotation.Rotation;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class SpawnAction extends Action  {

    private final List<VirtualEntityFactory> factories;

    private final int lifespan;

    private final EventPosition spawnPosition;

    private final EventRotation spawnRotation;

    private final EEntityContainer container;

    private final boolean bukkitEntity;


    public SpawnAction(List<VirtualEntityFactory> factories, int lifespan, EventPosition spawnPosition, EventRotation spawnRotation, EEntityContainer container, boolean bukkitEntity) {
        this.factories = factories;
        this.lifespan = lifespan;
        this.bukkitEntity = bukkitEntity;
        this.spawnPosition = spawnPosition;
        this.spawnRotation = spawnRotation;
        this.container = container;

        getDescriptor().indent()
                .addLine("Lifespan: "+lifespan)
                .addLine("Is Bukkit Entity?: "+ bukkitEntity)
                .addLine("Entities: ").indent();
        if(!factories.isEmpty()) {
            factories.forEach(factory -> {
                getDescriptor().addLine("Name: "+factory.getName()).indent()
                        .addLine("Type: "+factory.getType().name())
                        .addLine("Location: "+factory.getLocation()).outdent();
            });
        }
        getDescriptor().outdent().outdent();
    }

    @Override
    protected void handler(TriggerContext context) {
        spawnEntity(context, factories, lifespan, spawnPosition, spawnRotation, container, bukkitEntity);
    }

    public static Set<McmeEntity> spawnEntity(TriggerContext context, List<VirtualEntityFactory> factories, int lifespan, EventPosition spawnPosition, EventRotation spawnRotation, EEntityContainer container, boolean bukkitEntity) {
        DebugManager.verbose(Modules.Action.execute(SpawnAction.class),"Spawned entity! Position: " + spawnPosition + ", Rotation: " + spawnRotation);
        Set<McmeEntity> entities = new HashSet<>();
        factories.forEach(factory -> {
            try {
                String name = context.getName();
                if(name!=null) factory.withDisplayName(name);

                if(spawnPosition != null) {
                    Vector position = spawnPosition.getPosition(context);
                    Location location = factory.getLocation();
                    location.set(position.getX(),position.getY(),position.getZ());
                    factory.withLocation(location);
                }
                if(spawnRotation != null) {
                    Rotation rotation = spawnRotation.getRotation(context);
                    Location location = factory.getLocation();
                    location.setPitch(rotation.getPitch());
                    location.setYaw(rotation.getYaw());
                    factory.withLocation(location);
                }

                if(bukkitEntity) {
                    Location location = factory.getLocation();
                    spawnRealEntity(factory,lifespan,location);
                } else {
                    McmeEntity entity = EntitiesPlugin.getEntityServer().spawnEntity(factory);
                    context.getEntityContainer().addEntity(entity);
                    context.withEntity(entity);
                    entities.add(entity);
                }
            } catch (InvalidLocationException | InvalidDataException e) {
                e.printStackTrace();
            }
        });
        if(lifespan>0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    entities.forEach(entity -> context.getEntityContainer().removeEntity(entity));
                    EntitiesPlugin.getEntityServer().removeEntity(entities);
                }
            }.runTaskLater(MCMEScripts.getInstance(), lifespan);
        }
        return entities;
    }

    public static void spawnRealEntity(VirtualEntityFactory factory, int lifespan, Location spawnLocation) {
        EntityType type = factory.getType().getBukkitEntityType();
        if(type != null && spawnLocation != null && spawnLocation.getWorld() !=null) {
            Entity entity = spawnLocation.getWorld().spawnEntity(spawnLocation,type, CreatureSpawnEvent.SpawnReason.CUSTOM);
            if(entity instanceof Horse) {
                Horse horse = (Horse) entity;
                horse.setTamed(true);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            }
            if(lifespan>0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        entity.remove();
                    }
                }.runTaskLater(MCMEScripts.getInstance(), lifespan);
            }
        }

    }

    public enum ESpawnLocations{
        FROM_FILE,
        CUSTOM
    }
}
