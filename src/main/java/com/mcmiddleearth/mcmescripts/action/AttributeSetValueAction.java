package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class AttributeSetValueAction extends VirtualEntityTargetedAction {

    public AttributeSetValueAction(VirtualEntityEventTarget target, Attribute attributeKey, double value) {
        super(target, (entity, context) -> {
            if(entity.getAttribute(attributeKey) == null)
                entity.registerAttribute(attributeKey);

            AttributeInstance attributeInstance = entity.getAttribute(attributeKey);
            assert attributeInstance != null;
            attributeInstance.setBaseValue(value);
        });
    }
}