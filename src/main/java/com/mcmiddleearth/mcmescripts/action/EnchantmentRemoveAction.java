package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.EntityTargetedAction;
import com.mcmiddleearth.mcmescripts.component.ItemFilter;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.eventTarget.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.looting.LootTable;
import com.mcmiddleearth.mcmescripts.looting.LootTableChoice;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchantmentRemoveAction extends EntityTargetedAction {

    public EnchantmentRemoveAction(EntityEventTarget target, Set<ItemFilter> itemFilters, Set<WrappedEnchantment> enchantments, Set<LootTableChoice<WrappedEnchantment>> enchantmentChoices, int quantity) {
        super(target, (entity, context) -> {
            int calculatedQuantity = quantity;
            List<ItemStack> applyItems = new ArrayList<>();
            LootTable<WrappedEnchantment> lootTable = new LootTable<>(enchantmentChoices);

            Inventory entityInventory = entity.getInventory();
            for (ItemStack entityItem : entityInventory) {
                if (itemFilters.isEmpty()) {
                    applyItems.add(entityItem);
                    calculatedQuantity -= 1;
                    break;
                }
                else {
                    for (ItemFilter filter : itemFilters) {
                        if (filter.match(entityItem, entityInventory)) {
                            applyItems.add(entityItem);
                            calculatedQuantity -= 1;
                            break;
                        }
                    }
                }
            }

            if (calculatedQuantity >= 0) {
                for (ItemStack applyItem : applyItems) {
                    Set<WrappedEnchantment> removeEnchantments = new HashSet<>(enchantments);
                    removeEnchantments.addAll(lootTable.select());
                    removeEnchantment(applyItem, removeEnchantments);
                    context.getDescriptor().addLine("Removing enchantment: "+applyItem.getType().name());
                }
            }
            else {
                List<ItemStack> limitedItems = new ArrayList<>(applyItems);
                Collections.shuffle(limitedItems);
                calculatedQuantity = quantity;
                for (ItemStack limitedItem : limitedItems) {
                    if (calculatedQuantity >= 0) {
                        break;
                    }
                    Set<WrappedEnchantment> removeEnchantments = new HashSet<>(enchantments);
                    removeEnchantments.addAll(lootTable.select());
                    removeEnchantment(limitedItem, removeEnchantments);
                    context.getDescriptor().addLine("Removing enchantment: "+limitedItem.getType().name());
                    calculatedQuantity -= 1;
                }
                context.getDescriptor().addLine("Stopping de-enchanting: Max quantity reached.");
            }
        });

        Descriptor descriptor = getDescriptor().indent();
        descriptor.addLine("Max. Quantity: "+quantity);
        if(!itemFilters.isEmpty()) {
            descriptor.addLine("Filters: ").indent();
            itemFilters.forEach(itemFilter -> descriptor.add(itemFilter.getDescriptor()));
            descriptor.outdent();
        } else {
            descriptor.addLine("Filters: --none--");
        }
        if(!enchantments.isEmpty()) {
            descriptor.addLine("Enchantments: ").indent();
            enchantments.forEach(enchantment -> descriptor.add(enchantment.getDescriptor()));
            descriptor.outdent();
        } else {
            descriptor.addLine("Enchantments: --none--");
        }
        /*if(!enchantmentChoices.isEmpty()) {
            descriptor.addLine("Enchantment choices: ").indent();
            enchantmentChoices.forEach(choice -> descriptor.add(choice.getDescriptor()));
            descriptor.outdent();
        } else {
            descriptor.addLine("Enchantments choices: --none--");
        }*/
        descriptor.outdent();
    }

    private static void removeEnchantment(ItemStack item, Set<WrappedEnchantment> enchantments) {
        ItemMeta meta = item.getItemMeta();

        for (WrappedEnchantment wrappedEnchantment : enchantments) {
            meta.removeEnchant(wrappedEnchantment.getEnchantment());
        }

        item.setItemMeta(meta);
    }

}
