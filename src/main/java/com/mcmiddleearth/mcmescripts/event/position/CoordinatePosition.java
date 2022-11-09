package com.mcmiddleearth.mcmescripts.event.position;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CoordinatePosition extends EventPosition {

    private final Vector coordinates;

    public CoordinatePosition(Vector coordinates, Vector offset, EEventPositionOffsetType offsetType) {
        super(offset,offsetType);
        this.coordinates = coordinates;
    }

    @Override
    public Vector getPosition(TriggerContext context) {
        return new Vector(coordinates.getX(),coordinates.getY(),coordinates.getZ());
    }
}
