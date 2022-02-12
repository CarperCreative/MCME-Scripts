package com.mcmiddleearth.mcmescripts.action;

import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.entities.EntitiesPlugin;
import com.mcmiddleearth.entities.PersistentDataKey;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.compiler.ItemCompiler;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.looting.ItemChoice;
import com.mcmiddleearth.mcmescripts.looting.LootTable;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.Set;

public class ItemGiveAction extends SelectingAction<McmeEntity> {

    private static final Random random = new Random();

    public ItemGiveAction(Selector<McmeEntity> selector, Set<ItemStack> items, Set<ItemChoice> weightChoices,
                          @Nullable EquipmentSlot slot, int slotId, int duration) {
        super(selector, (entity, context) -> {
            DebugManager.verbose(Modules.Action.execute(ItemGiveAction.class),"Selector: "+selector.getSelector()
                    + " Items: "+items.size()+ " Choices: "+ weightChoices.size()+" Slot: "+(slot!=null?slot.name():"null")+" "+slotId);
            items.forEach(item -> giveItem(entity, context, item, slot, slotId, duration));

            LootTable lootTable = new LootTable(weightChoices);
            lootTable.selectItems().forEach(item->giveItem(entity, context, item, slot, slotId, duration));
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Selector: "+selector.getSelector()
                + " item: "+items.size()+" Choices: "+ weightChoices.size()+" Slot: "+(slot!=null?slot.name():"null")+" "+slotId);
    }

    private static void giveItem(McmeEntity entity, TriggerContext context, ItemStack item, EquipmentSlot slot, int slotId, int duration) {
        ItemMeta meta = item.getItemMeta();
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
            /*Logger.getGlobal().info("type: "+item.getType());
            Logger.getGlobal().info("Lore: "+meta.lore().size());
            Logger.getGlobal().info("Enchant: "+meta.getEnchants().size());
            Logger.getGlobal().info("has cmd: "+meta.hasCustomModelData());*/
        }
        item.setItemMeta(meta);

        entity.addItem(item,slot,slotId);
    }

}
