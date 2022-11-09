package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.bossbattle.TimelineConfiguration;
import com.mcmiddleearth.mcmescripts.component.WrappedEnchantment;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TimelineConfigurationCompiler {

    private static final String
            KEY_TIMELINE_NAME            = "timeline_name",
            KEY_TIMESLOT_INDEX           = "timeslot_index";

    public static Set<TimelineConfiguration> compile(JsonElement jsonElement) {
        Set<TimelineConfiguration> result = new HashSet<>();
        if(jsonElement == null) return result;

        compileTimeslotConfiguration(jsonElement.getAsJsonObject()).ifPresent(result::add);

        return result;
    }

    public static Optional<TimelineConfiguration> compileTimeslotConfiguration(JsonObject jsonObject) {
        try {
            JsonObject enchantJson = jsonObject.getAsJsonObject();
            String timelineName = PrimitiveCompiler.compileString(enchantJson.get(KEY_TIMELINE_NAME),null);
            int timeslotIndex = PrimitiveCompiler.compileInteger(enchantJson.get(KEY_TIMESLOT_INDEX),0);
            return Optional.of(new TimelineConfiguration(timelineName,timeslotIndex));
        } catch(IllegalStateException | ClassCastException | NullPointerException ex) {
            DebugManager.warn(Modules.Item.create(ItemCompiler.class), "Can't compile timeline configuration: " + ex.getMessage());
        }
        return Optional.empty();
    }
}
