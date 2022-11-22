package com.mcmiddleearth.mcmescripts.condition.targeted;

import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.selector.PlayerSelector;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class PlayerTargetedCondition extends Condition {

    private boolean matchAllSelected = false;

    private final PlayerSelector selector;

    private final Function<Player,Boolean> test;

    public PlayerTargetedCondition(PlayerSelector selector, Function<Player, Boolean> test) {
        this.test = test;
        this.selector = selector;
    }

    @Override
    public boolean test(TriggerContext context) {
        boolean result = matchAllSelected;
        context.getDescriptor().add(super.getDescriptor()).indent();
        for(Player element : selector.select(context)) {
            context.getDescriptor().addLine("Testing player: "+element.getName());
            if(matchAllSelected && !test.apply(element)) {
                result = false;
                break;
            } else if(!matchAllSelected && test.apply(element)) {
                result = true;
                break;
            }
        }
        context.getDescriptor().addLine("Test result: "+result).outdent();
        return result;
    }

    public void setMatchAllSelected(boolean matchAllSelected) {
        this.matchAllSelected = matchAllSelected;
    }

    public Descriptor getDescriptor() {
        return super.getDescriptor().indent()
                .addLine("Selector: "+selector.getSelector())
                .addLine("Match All: "+matchAllSelected)
                .outdent();
    }

}
