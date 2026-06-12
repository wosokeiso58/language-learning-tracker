import java.time.LocalDate;

public class Session {

    private final int sessionID;

    private int minutes;

    private ActivityType activityType;

    private int Xp;

    public LocalDate date;

    public Session(int sessionID, int minutes, ActivityType activityType, LocalDate date) {
        this.sessionID = sessionID;
        this.minutes = minutes;
        this.activityType = activityType;
        this.date = date;
        Xp = 0;
    }

    public int getMinutes() {
        return minutes;
    }
    public ActivityType getActivityType() {
        return activityType;
    }

    public int getSessionID() {
        return sessionID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
    public int getXp() {
        return Xp;
    }
    public void setXp(int Xp) {
        this.Xp = Xp;
    }
}
