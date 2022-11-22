package com.mcmiddleearth.mcmescripts.action;

import com.google.common.base.Joiner;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.rotation.EventRotation;
import com.mcmiddleearth.mcmescripts.event.rotation.Rotation;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import lombok.extern.java.Log;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.Random;
import java.util.logging.Logger;

public class SprayParticlesAction extends Action {

    private final Particle particle;

    private final EventPosition spawnPosition;
    private final EventRotation spawnRotation;
    private final int duration;
    private final float speed;
    private final float spread;
    private final int amount;

    public SprayParticlesAction(EventPosition spawnPosition, EventRotation spawnRotation, Particle particle, int duration, float speed, float spread, int amount) {
        this.particle = particle;
        this.spawnPosition = spawnPosition;
        this.spawnRotation = spawnRotation;
        this.duration = duration;
        this.speed = speed;
        this.spread = spread;
        this.amount = amount;
    }

    @Override
    protected void handler(TriggerContext context) {
        if(spawnPosition != null && spawnRotation != null){
            new BukkitRunnable() {
                int counter = 0;
                @Override
                public void run() {
                    counter++;

                    Vector position = spawnPosition.getPosition(context);
                    Rotation rotation = spawnRotation.getRotation(context);

                    if(position != null && rotation != null){
                        Location location = new Location(context.getWorld(),position.getX(),position.getY(),position.getZ());


                        for(int i = 0; i < amount; i++){
                            Random random = new Random();
                            float spreadPitch = -spread+(random.nextFloat()*spread*2);
                            float spreadYaw = -spread+(random.nextFloat()*spread*2);

                            location.setPitch(rotation.getPitch()+spreadPitch);
                            location.setYaw(rotation.getYaw()+spreadYaw);
                            Vector direction = location.getDirection();
                            location.getWorld().spawnParticle(particle,location,0,direction.getX(), direction.getY(), direction.getZ(),speed,null,true);
                        }
                    }

                    if(counter > duration){
                        cancel();
                    }
                }
            }.runTaskTimer(MCMEScripts.getInstance(),0,1);
        }
    }
}
