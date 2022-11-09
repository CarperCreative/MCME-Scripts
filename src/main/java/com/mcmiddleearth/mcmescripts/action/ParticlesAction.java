package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.rotation.EventRotation;
import com.mcmiddleearth.mcmescripts.event.rotation.Rotation;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ParticlesAction extends Action {

    private final Particle particle;

    private final EventPosition spawnPosition;
    private final int duration;
    private final float speed;
    private final Vector spread;
    private final int amount;

    public ParticlesAction(EventPosition spawnPosition, Particle particle, int duration, float speed, Vector spread, int amount) {
        this.particle = particle;
        this.spawnPosition = spawnPosition;
        this.duration = duration;
        this.speed = speed;
        this.spread = spread;
        this.amount = amount;
    }

    @Override
    protected void handler(TriggerContext context) {
        if(spawnPosition != null){
            new BukkitRunnable() {
                int counter = 0;
                @Override
                public void run() {
                    counter++;

                    Vector position = spawnPosition.getPosition(context);

                    if(position != null){
                        Location location = new Location(context.getWorld(),position.getX(),position.getY(),position.getZ());

                        context.getWorld().spawnParticle(particle,location,amount,spread.getX(), spread.getY(), spread.getZ(),speed);
                    }

                    if(counter > duration){
                        cancel();
                    }
                }
            }.runTaskTimer(MCMEScripts.getInstance(),0,1);
        }
    }
}
