package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.EntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import org.bukkit.potion.PotionEffect;

import java.util.Set;

public class PotionEffectRemoveAction extends EntityTargetedAction {

    Set<PotionEffect> effects;

    public PotionEffectRemoveAction(EntityEventTarget target, Set<PotionEffect> effects){
        super(target, (entity, context) -> {
            effects.forEach(entity::removePotionEffect);
        });
        this.effects = effects;
        getDescriptor().indent();
        effects.forEach(effect -> PotionEffectAddAction.addEffect(getDescriptor(),effect));
    }
}
