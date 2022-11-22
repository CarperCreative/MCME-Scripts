package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.eventTarget.VirtualEntityEventTarget;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class AttributeRemoveModifierAction extends VirtualEntityTargetedAction {

    public AttributeRemoveModifierAction(VirtualEntityEventTarget target, Attribute attributeKey, String attributeModifierName) {
        super(target, (entity, context) -> {
            if(entity.getAttribute(attributeKey) == null)
                entity.registerAttribute(attributeKey);

            AttributeInstance attributeInstance = entity.getAttribute(attributeKey);
            assert attributeInstance != null;
            attributeInstance.getModifiers().removeIf(m -> m.getName().equals(attributeModifierName));
        });
    }
}