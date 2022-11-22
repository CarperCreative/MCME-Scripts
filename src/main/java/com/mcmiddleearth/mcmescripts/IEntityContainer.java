package com.mcmiddleearth.mcmescripts;

import com.mcmiddleearth.entities.entities.McmeEntity;

import java.util.Set;

public interface IEntityContainer {

    void addEntity(McmeEntity entity);

    void removeEntity(McmeEntity entity);

    Set<McmeEntity> getEntities();
}
