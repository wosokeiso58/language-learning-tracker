import java.time.LocalDate;

public class Session {

    private final int sessionID;

    private int minutes;

    private ActivityType activityType;
    private double variety;
    private int Xp = 0;
    private int readingXP = 0;
    private int listeningXP = 0;
    private int speakingXP = 0;
    private int writingXP = 0;
    private int grindingXP = 0;

    public LocalDate date;
    //TODO add a gainedXP for each category and add a variety and stuff
    //TODO so we can use that to edit a session properly.

    public Session(int sessionID, int minutes, ActivityType activityType, LocalDate date) {
        this.sessionID = sessionID;
        this.minutes = minutes;
        this.activityType = activityType;
        this.date = date;
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
    public int getXp(ActivityCategory activityCategory) {
        return switch(activityCategory){
            case SPEAKING -> speakingXP;
            case GRINDING -> grindingXP;
            case WRITING -> writingXP;
            case READING ->  readingXP;
            case LISTENING ->  listeningXP;
        };
    }

    public void setXp(ActivityCategory activityCategory, int xp) {
        switch(activityCategory){
            case SPEAKING -> speakingXP = xp;
            case GRINDING -> grindingXP = xp;
            case WRITING -> writingXP = xp;
            case READING -> readingXP = xp;
            case LISTENING -> listeningXP = xp;
        }

    }

    public void setXp(int Xp) {
        this.Xp = Xp;
    }
    public void setVariety(Double variety){
        this.variety = variety;
    }
    public double getVariety() {
        return variety;
    }

    @Override
    public String toString() {
        return "Activity type: " + this.getActivityType() + "\nMinutes: " + this.getMinutes() + "\nXp gained: " + this.getXp() + "\n";
    }
}
