package com.mcmiddleearth.mcmescripts.compiler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.selector.Selector;

public class SelectorCompiler {

    private static final String KEY_SELECTOR           = "select";


    public static Selector compileSelector(JsonObject jsonObject) {
        String selectorData = getSelectorData(jsonObject, KEY_SELECTOR);
        return new Selector(selectorData);
    }

    public static Selector compileSelector(JsonObject jsonObject, String key) {
        String selectorData = getSelectorData(jsonObject, key);
        return new Selector(selectorData);
    }

    private static String getSelectorData(JsonObject jsonObject, String key) {
        JsonElement selectorJson = jsonObject.get(key);
        if(selectorJson == null) return "@s";
        return selectorJson.getAsString();
    }
}
