package org.example;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Session {


    private final int minutes;
    private final ActivityType activityType;
    private final int xp;

    public LocalDate date;

    public Session(@JsonProperty("minutes")int minutes,
                   @JsonProperty("activityType")ActivityType activityType,
                   @JsonProperty("date")LocalDate date,
                   @JsonProperty("xp")int xp) {
        this.minutes = minutes;
        this.activityType = activityType;
        this.date = date;
        this.xp = xp;
    }

    public int getMinutes() {
        return minutes;
    }
    public ActivityType getActivityType() {
        return activityType;
    }

    public LocalDate getDate() {
        return date;
    }

    @JsonIgnore
    public int getXp() {
        return xp;
    }


    @Override
    public String toString() {
        return "Activity type: " + this.getActivityType() + "\nMinutes: " + this.getMinutes() + "\nXp gained: " + this.getXp() + "\n";
    }
}
