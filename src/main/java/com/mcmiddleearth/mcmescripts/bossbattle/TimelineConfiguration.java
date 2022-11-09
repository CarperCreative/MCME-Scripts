package com.mcmiddleearth.mcmescripts.bossbattle;

public class TimelineConfiguration {

    private final String timelineName;
    private final int timeslotIndex;

    public TimelineConfiguration(String timelineName, int timeslotIndex){
        this.timelineName = timelineName;
        this.timeslotIndex = timeslotIndex;
    }

    public String getTimelineName() {
        return timelineName;
    }

    public int getTimeslotIndex() {
        return timeslotIndex;
    }
}
