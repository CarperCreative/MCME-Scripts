package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.util.Vector;

public class EntityVelocityAction extends SelectingAction<McmeEntity> {

    public EntityVelocityAction(Selector<McmeEntity> selector, Vector velocity, boolean shouldOverride) {
        super(selector, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(EntityVelocityAction.class), "Velocity added to entity: " + entity.getEntityId() + " " + velocity);

            if (shouldOverride)
                //Completely override the velocity
                entity.setVelocity(velocity);
            else
                //Add the provided velocity to the entities current velocity
                entity.setVelocity(entity.getVelocity().add(velocity));
        });
        getDescriptor().indent().addLine("Velocity: " + velocity).outdent();
        //DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()+" Velocity: "+velocity);
    }
}
