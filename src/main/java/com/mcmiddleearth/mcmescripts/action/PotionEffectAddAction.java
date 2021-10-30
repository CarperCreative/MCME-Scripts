package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import org.bukkit.potion.PotionEffect;

public class PotionEffectAddAction extends SelectingAction<McmeEntity> {

    //PotionEffect effect;

    public PotionEffectAddAction(PotionEffect effect, McmeEntitySelector selector){
        super(selector, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(SetGoalAction.class),"Effect: "+effect.getType());
            entity.addPotionEffect(effect);
        });
        //this.effect = effect;
        DebugManager.info(Modules.Action.create(this.getClass()),"Effect: "+effect.getType());
    }
}