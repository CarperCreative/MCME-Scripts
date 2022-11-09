package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class AttributeModifierCompiler {

    private static final String
    KEY_NAME            = "name",
    KEY_ATTRIBUTE       = "attribute",
    KEY_AMOUNT          = "amount",
    KEY_OPERATION       = "operation",
    KEY_SLOT            = "slot";

    public static AttributeModifier compile(JsonElement modJson) {
        try {
            JsonObject jsonObject = modJson.getAsJsonObject();
            int amount = PrimitiveCompiler.compileInteger(jsonObject.get(KEY_AMOUNT),1);
            EquipmentSlot slot = null;
            if(jsonObject.get(KEY_SLOT) != null) {
                slot = EquipmentSlot.valueOf(jsonObject.get(KEY_SLOT).getAsString().toUpperCase());
            }
            AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(jsonObject.get(KEY_OPERATION).getAsString().toUpperCase());
            String name = jsonObject.get(KEY_NAME).getAsString();
            UUID uuid = UUID.randomUUID();
            AttributeModifier mod;
            if(slot!=null) {
                mod = new AttributeModifier(uuid, name, amount, operation, slot);
            } else {
                mod = new AttributeModifier(uuid, name, amount, operation);
            }
            return mod;
        } catch(IllegalStateException | ClassCastException | NullPointerException | IllegalArgumentException ex) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Can't compile attribute modifier: "+ex.getMessage());
            return null;
        }
    }

    public static Attribute getAttribute(JsonElement modJson){
        return Attribute.valueOf(modJson.getAsJsonObject().get(KEY_ATTRIBUTE).getAsString().toUpperCase());
    }
}
