package com.mcmiddleearth.mcmescripts.looting;

import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class LootTableChoice<T> {

    private final int weight;

    private final Set<T> items;

    public LootTableChoice(int weight, Set<T> items) {
        this.weight = weight;
        this.items = items;
    }

    public int getWeight() {
        return weight;
    }

    public Set<T> getItems() {
        return items;
    }
}
