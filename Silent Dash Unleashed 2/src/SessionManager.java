import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionManager {

    private Language language;
    private Map<LocalDate, List<Session>> sessionsByDate;
    private int readingXp;
    private int grindingXp;
    private int listeningXp;
    private int speakingXp;
    private int writingXp;

    LocalDate today = LocalDate.now();

    public SessionManager(Language language,int progress) {
        this.language = language;
        this.sessionsByDate = new HashMap<>();

        readingXp = 0;
        grindingXp = 0;
        listeningXp = 0;
        speakingXp = 0;
        writingXp = 0;
    }

    public void logSession(Session session) {

        if (this.sessionsByDate.containsKey(session.getDate())) {
            this.sessionsByDate.get(session.getDate()).add(session);
        }
        else {
            this.sessionsByDate.put(session.getDate(), new ArrayList<>());
            this.sessionsByDate.get(session.getDate()).add(session);
        }

        double multiplier = checkVariety();

        int gainedXp = 0;


        int gainedGrindingXp = calculateXp(session,ActivityCategory.GRINDING,multiplier);
        int gainedListeningXp = calculateXp(session,ActivityCategory.LISTENING,multiplier);
        int gainedSpeakingXp = calculateXp(session,ActivityCategory.SPEAKING,multiplier);
        int gainedWritingXp = calculateXp(session,ActivityCategory.WRITING, multiplier);
        int gainedReadingXp = calculateXp(session,ActivityCategory.READING,multiplier);

        gainedXp += gainedGrindingXp;
        gainedXp += gainedListeningXp;
        gainedXp += gainedSpeakingXp;
        gainedXp += gainedWritingXp;
        gainedXp += gainedReadingXp;

        grindingXp += gainedGrindingXp;
        listeningXp += gainedListeningXp;
        speakingXp += gainedSpeakingXp;
        writingXp += gainedWritingXp;
        readingXp += gainedReadingXp;

        System.out.println("Gained Xp: " + gainedXp);


        System.out.println("Total grinding XP: "+ this.grindingXp);
        System.out.println("Total listening XP: "+ this.listeningXp);
        System.out.println("Total speaking XP: "+ this.speakingXp);
        System.out.println("Total writing XP: " + this.writingXp);
        System.out.println("Total reading XP: " + this.readingXp);
        System.out.println("\nTotal XP: " + this.getXp());

    }

    public int calculateXp(Session session, ActivityCategory activityCategory, double multiplier) {
        int xp = 0;
        ActivityType activityType = session.getActivityType();
        int minutes = session.getMinutes();
        double coefficient;

        switch (activityCategory) {
            case GRINDING:
                coefficient = activityType.getGrindingCoefficient();
                xp = (int) (minutes*multiplier*coefficient*100);
                System.out.println("Grinding XP gained: " + minutes + " (minutes) x " + multiplier +" (balance multiplier) x " + coefficient + " (grinding coefficient for " + activityType + ") x 100 = " + xp);
                break;
            case READING:
                coefficient =  activityType.getReadingCoefficient();
                xp = (int) (minutes*multiplier*coefficient*100);
                System.out.println("Reading XP gained: " + minutes + " (minutes) x " + multiplier +" (balance multiplier) x " + coefficient + " (reading coefficient for " + activityType + ") x 100 = " + xp);
                break;
            case SPEAKING:
                coefficient = activityType.getSpeakingCoefficient();
                xp = (int) (minutes*multiplier*coefficient*100);
                System.out.println("Speaking XP gained: " + minutes + " (minutes) x " + multiplier +" (balance multiplier) x " + coefficient + " (speaking coefficient for " + activityType + ") x 100 = " + xp);
                break;
            case WRITING:
                coefficient = activityType.getWritingCoefficient();
                xp = (int) (minutes*multiplier*coefficient*100);
                System.out.println("Writing XP gained: " + minutes + " (minutes) x " + multiplier +" (balance multiplier) x " + coefficient + " (writing coefficient for " + activityType + ") x 100 = " + xp);
                break;
            case LISTENING:
                coefficient = activityType.getListeningCoefficient();
                xp = (int) (minutes*multiplier*coefficient*100);
                System.out.println("Listening XP gained: " + minutes + " (minutes) x " + multiplier +" (balance multiplier) x " + coefficient + " (listening coefficient for " + activityType + ") x 100 = " + xp);
        }
        return xp;


    }

    public double checkVariety(){
        double speakingMinutes = 0;
        double grindingMinutes = 0;
        double listeningMinutes = 0;
        double totalMinutes = 0;
        int balanceCount = 0;

        for(Session session : getSessionsFromLastNDays(14)){
            switch(session.getActivityType().getMainCategory()){

                case SPEAKING -> speakingMinutes += session.getMinutes();
                case GRINDING -> grindingMinutes+=session.getMinutes();
                case LISTENING ->  listeningMinutes+=session.getMinutes();

            }
            totalMinutes += session.getMinutes();
        }

        if(listeningMinutes/totalMinutes > 0.2){
            balanceCount +=1;
        }

        if(speakingMinutes/totalMinutes > 0.2){
            balanceCount +=5;
        }

        if(grindingMinutes/totalMinutes > 0.2){
            balanceCount +=10;
        }

        if (balanceCount == 16) {
            System.out.println("You have a healthy balance of activities. Well done! Balance XP multiplier: x1.25");
            return 1.25;
        }
        else if(balanceCount == 15){
            System.out.println("You have a solid balance of activities. You should be doing more listening. Balance XP multiplier: x1.00");
            return 1;
        }
        else if(balanceCount == 11){
            System.out.println("You have a solid balance of activities. You should be doing more speaking. Balance XP multiplier: x1.00");
            return 1;
        }
        else if(balanceCount == 6){
            System.out.println("You have a solid balance of activities. You should be doing more grinding. Balance XP multiplier: x1.00");
            return 1;
        }
        else if(balanceCount == 5){
            System.out.println("You have a weak balance of activities. You should be doing more grinding and listening. Balance XP multiplier: x0.75");
            return 0.75;
        }
        else if(balanceCount == 10){
            System.out.println("You have a weak balance of activities. You should be doing more speaking and listening. Balance XP multiplier: x0.75");
            return 0.75;
        }
        else if(balanceCount == 1){
            System.out.println("You have a weak balance of activities. You should be doing more speaking and grinding. Balance XP multiplier: x0.75");
            return 0.75;
        }
        else{
            System.out.println("error");
            return 1;
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

    //TODO: code progress shnangles, specific to activity categories and also take into account consistency, breaks

    public void displayGeneralProgress(Level level) {
        int ceiling = language.getXp(level);
        int xp = getXp();
        double percentage = ((double) xp /ceiling) * 100;
        double roundedPercentage = Math.round(percentage * Math.pow(10, 3)) / Math.pow(10, 3);

        System.out.println("XP to "+ level.toString() + ": " + xp + "/" + ceiling +"\nProgress: " + roundedPercentage + "%");

    }

    public int getXp() {
        return readingXp+speakingXp+writingXp+listeningXp+grindingXp;
    }
}