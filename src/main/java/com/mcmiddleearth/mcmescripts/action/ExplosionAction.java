package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.effect.Explosion;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.Collection;

public class ExplosionAction extends Action {

    private final Explosion explosion;
    private final EntityEventTarget unaffectedTarget, damagerTarget;

    public ExplosionAction(Explosion explosion, EntityEventTarget unaffectedTarget, EntityEventTarget damagerTarget) {
        this.explosion = explosion;
        this.unaffectedTarget = unaffectedTarget;
        this.damagerTarget = damagerTarget;
        getDescriptor().indent()
                .addLine("Center: "+explosion.getLocation())
                .addLine("Damage: "+explosion.getDamage())
                .addLine("Particle: "+(explosion.getParticle() != null ? explosion.getParticle().name() : "--none--"))
                .addLine("Velocity: "+explosion.getVelocity())
                .addLine("Radius: "+explosion.getRadius())
                .addLine("Knockback: "+explosion.getKnockback())
                .addLine("Damager: "+(damagerTarget != null ? damagerTarget.toString() : "--none--"))
                .addLine("Unaffected: "+(unaffectedTarget != null ? unaffectedTarget.toString() : "--none--")).outdent();
    }

    @Override
    protected void handler(TriggerContext context) {
        if(damagerTarget !=null) {
            McmeEntity damager = unaffectedTarget.getTargets(context).stream().findFirst().orElse(null);
            context.getDescriptor().addLine("Selected damager: "+(damager != null ? damager.getName() : "--none--"));
            explosion.setDamager(damager);
            if(damager!=null) {
                explosion.setLocation(damager.getLocation());
            }
        } else {
            context.getDescriptor().addLine("Selected damager: --none--");
            explosion.setDamager(null);
        }
        if(explosion.getLocation() != null) {
            if(unaffectedTarget !=null) {
                Collection<McmeEntity> unaffectedList = unaffectedTarget.getTargets(context);
                if(!unaffectedList.isEmpty()) {
                    context.getDescriptor().addLine("Unaffected: ").indent();
                    unaffectedList.forEach(unaffected -> {
                        explosion.addUnaffected(unaffected);
                        context.getDescriptor().addLine(unaffected.getName());
                    });
                    context.getDescriptor().outdent();
                } else {
                    context.getDescriptor().addLine("Unaffected: --none--");
                }
            } else {
                explosion.clearUnaffected();
            }
            explosion.explode();
        } else {
            DebugManager.warn(Modules.Action.execute(this.getClass()),"Can't trigger explosion. Missing location.");
        }
    }
}
