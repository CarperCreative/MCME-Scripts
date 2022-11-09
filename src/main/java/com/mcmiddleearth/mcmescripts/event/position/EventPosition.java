package com.mcmiddleearth.mcmescripts.event.position;

import com.craftmend.thirdparty.iosocket.global.Global;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import lombok.extern.java.Log;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.logging.Logger;

public abstract class EventPosition {

    private final Vector offset;
    private final EEventPositionOffsetType offsetType;

    public EventPosition(Vector offset, EEventPositionOffsetType offsetType){
        this.offset = offset;
        this.offsetType = offsetType;
    }

    public abstract Vector getPosition(TriggerContext context);


    protected Vector getOffset(float pitch, float yaw) {
        switch (offsetType){
            case ALONG_WORLD_AXIS -> {
                return offset;
            }
            case DIRECTIONAL_Y -> {

            }
            case DIRECTIONAL -> {
                // I've never thought this hard about anything in my life

                Logger.getGlobal().warning("pitch: " + pitch + ", yaw: " + yaw);
                double forward_x = Math.cos(Math.toRadians(yaw-90))*Math.cos(Math.toRadians(pitch));
                double forward_y = Math.sin(Math.toRadians(pitch));
                double forward_z = Math.sin(Math.toRadians(yaw-90))*Math.cos(Math.toRadians(pitch));
                Vector forward = new Vector(-forward_x,-forward_y,-forward_z);
                Logger.getGlobal().warning("forward: " + forward);

                double right_x = Math.cos(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch));
                double right_y = Math.sin(Math.toRadians(pitch));
                double right_z = Math.sin(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch));
                Vector right = new Vector(right_x,right_y,right_z);
                Logger.getGlobal().warning("right: " + right);

                double up_x = Math.cos(Math.toRadians(yaw+90))*Math.cos(Math.toRadians(pitch+90));
                double up_y = Math.sin(Math.toRadians(pitch+90));
                double up_z = Math.sin(Math.toRadians(yaw+90))*Math.cos(Math.toRadians(pitch+90));
                Vector up = new Vector(-up_x,up_y,-up_z);
                Logger.getGlobal().warning("up: " + up);

                return forward.multiply(offset.getZ()).add(right.multiply(offset.getX())).add(up.multiply(offset.getY()));
            }
        }
        return null;
    }

    protected Vector getOffset(){
        return getOffset(0,0);
    }
}
