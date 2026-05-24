public class Session {
    private int minutes;
    private ActivityType activityType;
    private String Language;
    public String date;
    public Session(int minutes, ActivityType activityType, String language, String date) {
        this.minutes = minutes;
        this.activityType = activityType;
        this.Language = language;
        this.date = date;
    }
}
