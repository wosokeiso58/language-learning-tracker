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

    public float checkVariety(){
        float speakingMinutes = 0;
        float grindingMinutes = 0;
        float listeningMinutes = 0;
        float totalMinutes = 0;
        float balanceCount = 0;

        for(Session session : getSessionsFromLastNDays(14)){
            switch(session.getActivityType().getMainCategory()){

                case SPEAKING -> speakingMinutes+=session.getMinutes();
                case GRINDING -> grindingMinutes+=session.getMinutes();
                case LISTENING ->  listeningMinutes+=session.getMinutes();
            }
            totalMinutes += session.getMinutes();
        }

        if(totalMinutes / listeningMinutes > 0.2){
            balanceCount +=1;
        }

        if(totalMinutes / speakingMinutes > 0.2){
            balanceCount +=5;
        }

        if(totalMinutes / grindingMinutes > 0.2){
            balanceCount +=10;
        }

        if (balanceCount == 16) {
            System.out.println("You have a healthy balance of activities. Well done! XP multiplier: x1.25");
            return 1.25F;
        }
        else if(balanceCount == 15){
            System.out.println("You have a solid balance of activities. You should be doing more listening. XP multiplier: x1.00");
            return 1F;
        }
        else if(balanceCount == 11){
            System.out.println("You have a solid balance of activities. You should be doing more speaking. XP multiplier: x1.00");
            return 1F;
        }
        else if(balanceCount == 6){
            System.out.println("You have a solid balance of activities. You should be doing more grinding. XP multiplier: x1.00");
            return 1F;
        }
        else if(balanceCount == 5){
            System.out.println("You have a weak balance of activities. You should be doing more grinding and listening. XP multiplier: x0.75");
            return 0.75F;
        }
        else if(balanceCount == 10){
            System.out.println("You have a weak balance of activities. You should be doing more speaking and listening. XP multiplier: x0.75");
            return 0.75F;
        }
        else if(balanceCount == 1){
            System.out.println("You have a weak balance of activities. You should be doing more speaking and grinding. XP multiplier: x0.75");
            return 0.75F;
        }
        else{
            System.out.println("error");
            return 0;
        }



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