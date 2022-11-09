package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class AttributeAddModifierAction extends VirtualEntityTargetedAction {

    public AttributeAddModifierAction(VirtualEntityEventTarget target, Attribute attributeKey, AttributeModifier attributeModifier) {
        super(target, (entity, context) -> {
            if(entity.getAttribute(attributeKey) == null)
                entity.registerAttribute(attributeKey);

            AttributeInstance attributeInstance = entity.getAttribute(attributeKey);
            assert attributeInstance != null;
            attributeInstance.addModifier(attributeModifier);
        });
    }
}