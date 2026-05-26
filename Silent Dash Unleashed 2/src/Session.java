public class Session {

    private int minutes;

    private ActivityType activityType;

    private Language language;

    public String date;

    public Session(int minutes, ActivityType activityType, Language language, String date) {
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
    public String getDate() {
        return date;
    }
}
