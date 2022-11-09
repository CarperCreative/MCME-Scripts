package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.EntityTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ItemRemoveAction extends EntityTargetedAction {

    public ItemRemoveAction(EntityEventTarget target, Set<ItemStack> items) {
        super(target, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(ItemRemoveAction.class),"Target: " + target.toString()
                    + " Items: "+items.size());
            items.forEach(entity::removeItem);
        });
        getDescriptor().indent();
        if(!items.isEmpty()) {
            getDescriptor().addLine("Items: ").indent();
            items.forEach(item -> getDescriptor().addLine(item.getType().name()));
            getDescriptor().outdent();
        } else {
            getDescriptor().addLine("Items: --none--");
        }
    }

}
