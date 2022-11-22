package com.mcmiddleearth.mcmescripts.action;

import com.google.common.base.Joiner;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.PersistentDataKey;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.action.targeted.EntityTargetedAction;
import com.mcmiddleearth.mcmescripts.compiler.ItemCompiler;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.looting.LootTableChoice;
import com.mcmiddleearth.mcmescripts.looting.LootTable;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public class ItemGiveAction extends EntityTargetedAction {

    public ItemGiveAction(EntityEventTarget target, Set<ItemStack> items, Set<LootTableChoice<ItemStack>> weightChoices,
                          @Nullable EquipmentSlot slot, int slotId, int duration) {
        super(target, (entity, context) -> {
            items.forEach(item -> giveItem(entity, context, item, slot, slotId, duration));

            LootTable<ItemStack> lootTable = new LootTable<>(weightChoices);
            lootTable.select().forEach(item->giveItem(entity, context, item, slot, slotId, duration));
        });
        getDescriptor().indent()
                .addLine("Duration: "+duration)
                .addLine("Slot no.: "+slotId)
                .addLine("Slot: "+slot);
        if(!items.isEmpty()) {
            getDescriptor().addLine("Items: ").indent();
            items.forEach(item -> getDescriptor().addLine(item.getType().name()));
            getDescriptor().outdent();
        } else {
            getDescriptor().addLine("Items: --none--");
        }
        if(!weightChoices.isEmpty()) {
            getDescriptor().addLine("Item choices: ").indent();
            weightChoices.forEach(choice -> {
                getDescriptor().addLine("Weight: "+choice.getWeight()).indent();
                choice.getItems().forEach(item -> getDescriptor().addLine(item.getType().name()));
                getDescriptor().outdent();
            });
            getDescriptor().outdent();
        } else {
            getDescriptor().addLine("Item choices: --none--");
        }
        getDescriptor().outdent();
    }

    private static void giveItem(McmeEntity entity, TriggerContext context, ItemStack item, EquipmentSlot slot, int slotId, int duration) {
        ItemMeta meta = item.getItemMeta();
        context.getDescriptor().addLine("Give item: "+item.getType().name());
        if(duration>0) {
            NamespacedKey key = EntitiesPlugin.getInstance().getPersistentDataKey(PersistentDataKey.ITEM_REMOVAL_TIME);
            meta.getPersistentDataContainer().set(key, PersistentDataType.PrimitivePersistentDataType.LONG,
                    EntitiesPlugin.getEntityServer().getCurrentTick() + duration);
        }
        if(context.getMessage()!=null) {
            if(context.getName() != null) {
                ItemCompiler.addLore(meta, new JsonPrimitive(context.getName()+": "+context.getMessage()));
            } else {
                ItemCompiler.addLore(meta, new JsonPrimitive(context.getMessage()));
            }
            context.getDescriptor().addLine("Adding lore: "+ Joiner.on(" ").join(Objects.requireNonNull(meta.getLore())));
        }
        item.setItemMeta(meta);

        entity.addItem(item,slot,slotId);
    }

}
