package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.bossbattle.TimelineConfiguration;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.looting.LootTableChoice;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LootTableCompiler {

    public static Optional<Set<LootTableChoice<ItemStack>>> compileItemChoices(JsonObject jsonObject) {
        Set<LootTableChoice<ItemStack>> itemWeightChoices = new HashSet<>();
        JsonElement itemChoicesJson = jsonObject.get(ActionCompiler.KEY_CHOICES);
        if(itemChoicesJson instanceof JsonArray) {
            itemChoicesJson.getAsJsonArray().forEach(element -> {
                if(element instanceof JsonObject) {
                    int weight = PrimitiveCompiler.compileInteger(element.getAsJsonObject().get(ActionCompiler.KEY_WEIGHT),10);
                    Set<ItemStack> choiceItems = ItemCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ITEM));
                    choiceItems.addAll(ItemCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ITEMS)));
                    itemWeightChoices.add(new LootTableChoice<>(weight,choiceItems));
                }
            });
        }
        if(itemWeightChoices.isEmpty()) return Optional.empty();
        return Optional.of(itemWeightChoices);
    }

    public static Optional<Set<LootTableChoice<WrappedEnchantment>>> compileEnchantmentChoices(JsonObject jsonObject) {
        Set<LootTableChoice<WrappedEnchantment>> enchantmentWeightChoices = new HashSet<>();
        JsonElement itemChoicesJson = jsonObject.get(ActionCompiler.KEY_CHOICES);
        if(itemChoicesJson instanceof JsonArray) {
            itemChoicesJson.getAsJsonArray().forEach(element -> {
                if(element instanceof JsonObject) {
                    int weight = PrimitiveCompiler.compileInteger(element.getAsJsonObject().get(ActionCompiler.KEY_WEIGHT),10);
                    Set<WrappedEnchantment> choiceEnchantments = EnchantmentCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ENCHANTMENT));
                    choiceEnchantments.addAll(EnchantmentCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_ENCHANTMENTS)));
                    enchantmentWeightChoices.add(new LootTableChoice<>(weight,choiceEnchantments));
                }
            });
        }
        if(enchantmentWeightChoices.isEmpty()) return Optional.empty();
        return Optional.of(enchantmentWeightChoices);
    }

    public static Optional<Set<LootTableChoice<TimelineConfiguration>>> compileTimelineChoices(JsonObject jsonObject) {
        Set<LootTableChoice<TimelineConfiguration>> timelineConfigurationWeightChoices = new HashSet<>();
        JsonElement itemChoicesJson = jsonObject.get(ActionCompiler.KEY_CHOICES);
        if(itemChoicesJson instanceof JsonArray) {
            itemChoicesJson.getAsJsonArray().forEach(element -> {
                if(element instanceof JsonObject) {
                    int weight = PrimitiveCompiler.compileInteger(element.getAsJsonObject().get(ActionCompiler.KEY_WEIGHT),10);
                    Set<TimelineConfiguration> timelineConfigurations = TimelineConfigurationCompiler.compile(element.getAsJsonObject().get(ActionCompiler.KEY_TIMELINE_CONFIGURATION));
                    timelineConfigurationWeightChoices.add(new LootTableChoice<>(weight,timelineConfigurations));
                }
            });
        }
        if(timelineConfigurationWeightChoices.isEmpty()) return Optional.empty();
        return Optional.of(timelineConfigurationWeightChoices);
    }
}
