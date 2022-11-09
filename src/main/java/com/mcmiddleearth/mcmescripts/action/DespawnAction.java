package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;
import org.bukkit.Particle;
import org.bukkit.World;

public class DespawnAction extends VirtualEntityTargetedAction {

    final boolean particles;
    public DespawnAction(VirtualEntityEventTarget target, boolean particles) {
        super(target, (entity,context) -> {
            if(particles) context.getWorld().spawnParticle(Particle.CLOUD,entity.getLocation(),30,1.0f);
            EntitiesPlugin.getEntityServer().removeEntity(entity);
            context.getEntityContainer().removeEntity(entity);
        });
        this.particles = particles;
    }
}
