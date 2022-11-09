package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.EntityTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import org.bukkit.potion.PotionEffect;

import java.util.Random;
import java.util.Set;

public class PotionEffectAddAction extends EntityTargetedAction {

    private static final Random random = new Random();

    public PotionEffectAddAction(EntityEventTarget target, Set<PotionEffect> effects, Set<PotionEffectChoice> choices){
        super(target, (entity, context) -> {
            if(!effects.isEmpty()) {
                effects.forEach(effect -> {
                    context.getDescriptor().addLine("Applying effect: "+effect.getType().getName());
                    entity.addPotionEffect(effect);
                });
            }
            int weightSum = 0;
            for(PotionEffectChoice choice: choices) {
                weightSum+=choice.getWeight();
            }
            int weightRandom = random.nextInt(weightSum+1);
            weightSum = 0;
            for(PotionEffectChoice choice: choices) {
                weightSum+=choice.getWeight();
                if(weightSum>=weightRandom) {
                    entity.addPotionEffect(choice.getEffect());
                    context.getDescriptor().addLine("Applying random effect: "+choice.getEffect().getType().getName());
                    break;
                }
            }

        });
        getDescriptor().indent()
                .addLine("Effects: ");

        effects.forEach(choice -> {
            addEffect(getDescriptor(),choice);
        });


        if(!choices.isEmpty()) {
            getDescriptor().addLine("Potion effect choices: ").indent();
            choices.forEach(choice -> {
                getDescriptor().addLine("Weight: "+choice.getWeight());
                addEffect(getDescriptor(),choice.getEffect());
            });
            getDescriptor().outdent();
        } else {
            getDescriptor().addLine("Potion effect choices: --none--");
        }
        getDescriptor().outdent();
    }

    public static void addEffect(Descriptor descriptor, PotionEffect effect) {
        descriptor.addLine("Effect: ").indent()
                .addLine("Type: "+effect.getType().getName())
                .addLine("Type: "+effect.getAmplifier())
                .addLine("Type: "+effect.getDuration())
                .addLine("Type: "+effect.isAmbient())
                .addLine("Type: "+effect.hasParticles())
                .addLine("Type: "+effect.hasIcon()).outdent();
    }

    // TODO: Use generic loot table for this
    public static class PotionEffectChoice {

        private final PotionEffect effect;
        private final int weight;

        public PotionEffectChoice(PotionEffect effect, int weight) {
            this.effect = effect;
            this.weight = weight;
        }

        public PotionEffect getEffect() {
            return effect;
        }

        public int getWeight() {
            return weight;
        }
    }
}
