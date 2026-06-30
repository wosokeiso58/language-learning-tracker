import java.time.LocalDate;

public class Session {


    private final int minutes;
    private final int xp;
    private final ActivityType activityType;
    private final double variety;

    public LocalDate date;

    public Session(int minutes, ActivityType activityType, LocalDate date, int xp, double variety) {
        this.minutes = minutes;
        this.activityType = activityType;
        this.date = date;
        this.xp = xp;
        this.variety = variety;
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

    public int getXp() {
        return xp;
    }

    public double getVariety() {
        return variety;
    }

    @Override
    public String toString() {
        return "Activity type: " + this.getActivityType() + "\nMinutes: " + this.getMinutes() + "\nXp gained: " + this.getXp() + "\n";
    }
}
