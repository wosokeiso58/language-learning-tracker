import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionManager {

    private Language language;
    private Map<LocalDate, List<Session>> sessionsByDate;
    private int progress;

    public SessionManager(Language language,int progress) {
        this.language = language;
        this.sessionsByDate = new HashMap<>();
        this.progress = progress;
    }

    public void logSession(Session session) {
        if (this.sessionsByDate.containsKey(session.getDate())) {
            this.sessionsByDate.get(session.getDate()).add(session);
        }
        else {
            this.sessionsByDate.put(session.getDate(), new ArrayList<>());
            this.sessionsByDate.get(session.getDate()).add(session);
        }

        //TODO: Add to log session the progress stuff

    }

    public int getTotalMinutes() {
        int totalMinutes = 0;
        for (Map.Entry<LocalDate, List<Session>> entry : sessionsByDate.entrySet()) {
            for (Session session : entry.getValue()) {
                totalMinutes += session.getMinutes();
            }
        }
        return totalMinutes;
    }
    public int getTotalMinutes(LocalDate date) {
        int totalMinutes = 0;
        for( Session session : this.sessionsByDate.get(date)) {
            totalMinutes += session.getMinutes();
        }
        return totalMinutes;
    }
    //TODO code ts
    public int getTotalMinutes(ActivityType activityType) {

        return 0;
    }
    //TODO code the progress one by using the get minutes ones to calculate the progress cos you're smart like that
    public int getProgress() {
        return progress;
    }
}