package com.mcmiddleearth.mcmescripts.event.rotation;

public class Rotation {
    private float pitch;
    private float yaw;

    public Rotation(float pitch, float yaw){
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
