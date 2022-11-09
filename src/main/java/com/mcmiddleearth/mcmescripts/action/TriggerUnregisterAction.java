package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.trigger.Trigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

import java.util.HashSet;
import java.util.Set;

public class TriggerUnregisterAction extends Action {

    private final Set<String> triggerNames = new HashSet<>();
    private final Set<Trigger> triggers = new HashSet<>();

    public TriggerUnregisterAction(String triggerName) {
        triggerNames.add(triggerName);
        buildDescriptor(triggerNames);
    }

    public TriggerUnregisterAction(Set<String> triggerNames) {
        this.triggerNames.addAll(triggerNames);
        buildDescriptor(triggerNames);
    }

    public TriggerUnregisterAction(Trigger trigger) {
        triggers.add(trigger);
        getDescriptor().indent()
                .addLine("Trigger: ").indent()
                .addLine(trigger.getClass().getSimpleName()+"- Name: "+trigger.getName()).outdent().outdent();
    }

    private void buildDescriptor(Set<String> triggerNames) {
        if(!triggerNames.isEmpty()) {
            getDescriptor().indent().addLine("Triggers: ").indent();
            triggerNames.forEach(trigger -> getDescriptor().addLine("Name: "+trigger));
            getDescriptor().outdent().outdent();
        }
    }

    @Override
    public void handler(TriggerContext context) {
        triggerNames.forEach(name-> context.getTriggerContainer().getTriggers(name).forEach(Trigger::unregister));
        triggers.forEach(Trigger::unregister);
    }

}