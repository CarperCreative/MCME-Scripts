package com.mcmiddleearth.mcmescripts;

import com.mcmiddleearth.mcmescripts.quest.tags.AbstractTag;

public interface ITagContainer {
    void setTag(AbstractTag<?> tag);

    AbstractTag<?> getTag(String name);

    void deleteTag(String name);

    boolean hasTag(String name);
}
