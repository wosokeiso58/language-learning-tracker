import java.time.LocalDate;

public class Session {

    private int minutes;

    private ActivityType activityType;

    private Language language;

    public LocalDate date;

    public Session(int minutes, ActivityType activityType, Language language, LocalDate date) {
        this.minutes = minutes;
        this.activityType = activityType;
        this.language = language;
        this.date = date;
    }

    public int getMinutes() {
        return minutes;
    }
    public ActivityType getActivityType() {
        return activityType;
    }
    public Language getLanguage() {
        return language;
    }
    public LocalDate getDate() {
        return date;
    }
}
