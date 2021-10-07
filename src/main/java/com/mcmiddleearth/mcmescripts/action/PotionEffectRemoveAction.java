package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.potion.PotionEffect;

public class PotionEffectRemoveAction extends SelectingAction<McmeEntitySelector> {

    PotionEffect effect;

    public PotionEffectRemoveAction(PotionEffect effect, McmeEntitySelector selector){
        super(selector, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(SetGoalAction.class),"Effect: "+effect.getType());
            entity.removePotionEffect(effect);
        });
        this.effect = effect;
        DebugManager.info(Modules.Action.create(this.getClass()),"Effect: "+effect.getType());
    }
}
