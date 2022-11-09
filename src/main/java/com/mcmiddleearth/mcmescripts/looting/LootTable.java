package com.mcmiddleearth.mcmescripts.looting;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class LootTable<T> {

    private final Set<LootTableChoice<T>> choices;

    private static final Random random = new Random();

    public LootTable(Set<LootTableChoice<T>> choices) {
        this.choices = choices;
    }

    public Set<T> select() {
        if(choices == null) return Collections.emptySet();

        int weightSum = 0;
        for(LootTableChoice<T> choice : choices) {
            weightSum+=choice.getWeight();
        }

        int weightRandom = random.nextInt(weightSum+1);
        weightSum = 0;

        for(LootTableChoice<T> choice : choices) {
            weightSum+=choice.getWeight();
            if(weightSum>=weightRandom) {
                return choice.getItems();
            }
        }
        return Collections.emptySet();
    }

    /*public Descriptor getDescriptor() {
        Descriptor descriptor = new Descriptor();
        if(!choices.isEmpty()) {
            descriptor.addLine("Loot table choices: ").indent();
            choices.forEach(choice -> descriptor.add(choice.getDescriptor()));
            descriptor.outdent();
        } else {
            descriptor.addLine("Loot table choices: --none--");
        }
        return descriptor;
    }*/
}
