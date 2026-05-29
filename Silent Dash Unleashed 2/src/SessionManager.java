import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionManager {

    private Language language;
    private Map<LocalDate, List<Session>> sessionsByDate;
    private int xp;
    LocalDate today = LocalDate.now();

    public SessionManager(Language language,int progress) {
        this.language = language;
        this.sessionsByDate = new HashMap<>();
    }

    public void logSession(Session session) {
        if (this.sessionsByDate.containsKey(session.getDate())) {
            this.sessionsByDate.get(session.getDate()).add(session);
        }
        else {
            this.sessionsByDate.put(session.getDate(), new ArrayList<>());
            this.sessionsByDate.get(session.getDate()).add(session);
        }

        //TODO log xp with coefficients





    }
    //TODO code ts maybe with current approach or maybe get the xp of each type and see how many are zero

    /*public float checkVariety(){
        int writingCount = 0;
        int speakingCount = 0;
        int newCount = 0;
        int grindingCount = 0;
        int listeningCount = 0;
        int readingCount = 0;

        for(Session session : getSessionsFromLastNDays(14)){
            switch(session.getActivityType()){
            }
        }

    }*/

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

    public int getTotalMinutes(ActivityType activityType) {
        int totalMinutes = 0;
        for (Map.Entry<LocalDate, List<Session>> entry : sessionsByDate.entrySet()) {
            for (Session session : entry.getValue()) {
                if (session.getActivityType() == activityType) {
                    totalMinutes += session.getMinutes();
                }
            }
        }
        return totalMinutes;
    }
    public List<Session> getSessionsFromLastNDays(int days){
        LocalDate cutoff = today.minusDays(days);
        List<Session> sessions = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Session>> entry : sessionsByDate.entrySet()) {

            LocalDate date = entry.getKey();
            for (Session session : entry.getValue()) {
                if (!date.isBefore(cutoff)) {
                    sessions.add(session);
                }
            }

        }
        return  sessions;
    }
    /*//TODO (maybe) code the progress one by using the get minutes ones to calculate the progress cos you're smart like that
    public int getProgress() {
        return progress;
    }

    public int getXp() {
        return xp;
    }*/
}