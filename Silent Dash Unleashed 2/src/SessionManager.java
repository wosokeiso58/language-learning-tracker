import java.time.LocalDate;
import java.util.*;

public class SessionManager {

    private final Language language;
    private final Map<LocalDate, List<Session>> sessionsByDate;

    private int ID = 0;
    private int activeStreak;
    private int inactiveStreak;
    private LocalDate lastStreakUpdate;

    private static final double BASE_RETENTION = 1000.0;
    private static final double INACTIVITY_WEIGHT = 50.0;

    private int readingXp;
    private int grindingXp;
    private int listeningXp;
    private int speakingXp;
    private int writingXp;

    private final int startSpeakingMinutes;
    private final int startListeningMinutes;
    private final int startGrindingMinutes;
    private final int startWritingMinutes;
    private final int startReadingMinutes;

    LocalDate today = LocalDate.now();

    public SessionManager(Language language, int grindingHours, int speakingHours, int readingHours, int listeningHours, int writingHours) {
        this.language = language;
        sessionsByDate = new HashMap<>();
        startGrindingMinutes = grindingHours * 60;

        activeStreak = 0;
        inactiveStreak = 0;
        lastStreakUpdate = LocalDate.of(2026, 6, 1);

        startListeningMinutes = listeningHours * 60;
        startSpeakingMinutes = speakingHours * 60;
        startWritingMinutes = writingHours * 60;
        startReadingMinutes = readingHours * 60;

        grindingXp = grindingHours * 60 * 165;
        readingXp = readingHours * 60 * 165;
        speakingXp = speakingHours * 60 * 165;
        listeningXp = listeningHours * 60 * 165;
        writingXp = writingHours * 60 * 165;

        System.out.println("Estimated grinding level: " + getLevel(ActivityCategory.GRINDING));
        System.out.println("Estimated speaking level: " + getLevel(ActivityCategory.SPEAKING));
        System.out.println("Estimated reading level: " + getLevel(ActivityCategory.READING));
        System.out.println("Estimated writing level: " + getLevel(ActivityCategory.WRITING));
        System.out.println("Estimated listening level: " + getLevel(ActivityCategory.LISTENING));

        System.out.println("Estimated Overall level: " + getLevel());
    }

    public String logSession(int minutes, ActivityType activityType, LocalDate date) {

        StringBuilder stringBuilder = new StringBuilder();

        Session session = new Session(ID, minutes, activityType, date);
        ID++;

        if (this.sessionsByDate.containsKey(session.getDate())) {
            this.sessionsByDate.get(session.getDate()).add(session);
        } else {
            this.sessionsByDate.put(session.getDate(), new ArrayList<>());
            this.sessionsByDate.get(session.getDate()).add(session);
        }

        Variety variety = new Variety();
        variety.checkVariety();
        double multiplier = variety.getVariety();
        stringBuilder.append(variety.getMessage()).append("\n\n");

        int gainedXp = 0;

        XpCalculator xpCalculator = new XpCalculator();

        xpCalculator.calculateXp(session, ActivityCategory.GRINDING, multiplier);
        int gainedGrindingXp = xpCalculator.getCalculatedXp();
        stringBuilder.append(xpCalculator.getMessage()).append("\n");
        xpCalculator.calculateXp(session, ActivityCategory.LISTENING, multiplier);
        int gainedListeningXp = xpCalculator.getCalculatedXp();
        stringBuilder.append(xpCalculator.getMessage()).append("\n");
        xpCalculator.calculateXp(session, ActivityCategory.SPEAKING, multiplier);
        int gainedSpeakingXp = xpCalculator.getCalculatedXp();
        stringBuilder.append(xpCalculator.getMessage()).append("\n");
        xpCalculator.calculateXp(session, ActivityCategory.WRITING, multiplier);
        int gainedWritingXp =  xpCalculator.getCalculatedXp();
        stringBuilder.append(xpCalculator.getMessage()).append("\n");
        xpCalculator.calculateXp(session, ActivityCategory.READING, multiplier);
        int gainedReadingXp = xpCalculator.getCalculatedXp();
        stringBuilder.append(xpCalculator.getMessage()).append("\n");

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

        stringBuilder.append("\nGained Xp: ").append(gainedXp);
        session.setXp(gainedXp);


        stringBuilder.append("\nTotal grinding XP: ").append(this.grindingXp);
        stringBuilder.append("\nTotal listening XP: ").append(this.listeningXp);
        stringBuilder.append("\nTotal speaking XP: ").append(this.speakingXp);
        stringBuilder.append("\nTotal writing XP: ").append(this.writingXp);
        stringBuilder.append("\nTotal reading XP: ").append(this.readingXp);
        stringBuilder.append("\n\nTotal XP: ").append(this.getXp());

        if (lastStreakUpdate.isBefore(today)) {
            dailyUpdate();
        }

        return stringBuilder.toString();

    }

    public class XpCalculator {

        private int calculatedXp;
        private String message;

        public XpCalculator(){
            calculatedXp = 0;
            message = "";
        }

        public void calculateXp(Session session, ActivityCategory activityCategory, double multiplier){
            ActivityType activityType = session.getActivityType();
            int minutes = session.getMinutes();
            double coefficient;

            switch (activityCategory) {
                case GRINDING:
                    coefficient = activityType.getGrindingCoefficient();
                    calculatedXp = (int) (minutes * multiplier * coefficient * 100);
                    message = "Grinding XP gained: " + minutes + " (minutes) x " + multiplier + " (balance multiplier) x " + coefficient + " (grinding coefficient for " + activityType + ") x 100 = " + calculatedXp;
                    break;
                case READING:
                    coefficient = activityType.getReadingCoefficient();
                    calculatedXp = (int) (minutes * multiplier * coefficient * 100);
                    message = "Reading XP gained: " + minutes + " (minutes) x " + multiplier + " (balance multiplier) x " + coefficient + " (reading coefficient for " + activityType + ") x 100 = " + calculatedXp;
                    break;
                case SPEAKING:
                    coefficient = activityType.getSpeakingCoefficient();
                    calculatedXp = (int) (minutes * multiplier * coefficient * 100);
                    message = "Speaking XP gained: " + minutes + " (minutes) x " + multiplier + " (balance multiplier) x " + coefficient + " (speaking coefficient for " + activityType + ") x 100 = " + calculatedXp;
                    break;
                case WRITING:
                    coefficient = activityType.getWritingCoefficient();
                    calculatedXp = (int) (minutes * multiplier * coefficient * 100);
                    message = "Writing XP gained: " + minutes + " (minutes) x " + multiplier + " (balance multiplier) x " + coefficient + " (writing coefficient for " + activityType + ") x 100 = " + calculatedXp;
                    break;
                case LISTENING:
                    coefficient = activityType.getListeningCoefficient();
                    calculatedXp = (int) (minutes * multiplier * coefficient * 100);
                    message = "Listening XP gained: " + minutes + " (minutes) x " + multiplier + " (balance multiplier) x " + coefficient + " (listening coefficient for " + activityType + ") x 100 = " + calculatedXp;
            }

        }
        public int getCalculatedXp(){
            return calculatedXp;
        }

        public String getMessage(){
            return message;
        }


    }


    public class Variety {

        private double variety;
        private String message;

        public Variety() {
            this.variety = 0;
            this.message = "";
        }

        public void checkVariety(){
            double speakingMinutes = 0;
            double grindingMinutes = 0;
            double listeningMinutes = 0;
            double totalMinutes = 0;
            int balanceCount = 0;

            for (Session session : getSessionsFromLastNDays(14)) {
                switch (session.getActivityType().getMainCategory()) {

                    case SPEAKING -> speakingMinutes += session.getMinutes();
                    case GRINDING -> grindingMinutes += session.getMinutes();
                    case LISTENING -> listeningMinutes += session.getMinutes();

                }
                totalMinutes += session.getMinutes();
            }

            if (listeningMinutes / totalMinutes > 0.2) {
                balanceCount += 1;
            }

            if (speakingMinutes / totalMinutes > 0.2) {
                balanceCount += 5;
            }

            if (grindingMinutes / totalMinutes > 0.2) {
                balanceCount += 10;
            }

            if (balanceCount == 16) {
                message = "You have a healthy balance of activities in the last 14 days. Well done! Balance XP multiplier: x1.25";
                variety = 1.15;
            } else if (balanceCount == 15) {
                message = "You have a solid balance of activities in the last 14 days. You should be doing more listening. Balance XP multiplier: x1.00";
                variety = 0.85;
            } else if (balanceCount == 11) {
                message = "You have a solid balance of activities in the last 14 days. You should be doing more speaking. Balance XP multiplier: x1.00";
                variety = 0.85;
            } else if (balanceCount == 6) {
                message = "You have a solid balance of activities in the last 14 days. You should be doing more grinding. Balance XP multiplier: x1.00";
                variety = 0.85;
            } else if (balanceCount == 5) {
                message = "You have a weak balance of activities in the last 14 days. You should be doing more grinding and listening. Balance XP multiplier: x0.75";
                variety = 0.65;
            } else if (balanceCount == 10) {
                message = "You have a weak balance of activities in the last 14 days. You should be doing more speaking and listening. Balance XP multiplier: x0.75";
                variety = 0.65;
            } else if (balanceCount == 1) {
                message = "You have a weak balance of activities in the last 14 days. You should be doing more speaking and grinding. Balance XP multiplier: x0.75";
                variety = 0.65;
            } else {
                message = "error";
                variety = 1;
            }



        }

        public double getVariety() {
            return variety;
        }

        public String getMessage() {
            return message;
        }
    }

    public int getTotalMinutes() {
        int totalMinutes = startGrindingMinutes + startSpeakingMinutes + startWritingMinutes + startListeningMinutes + startReadingMinutes;
        for (Map.Entry<LocalDate, List<Session>> entry : sessionsByDate.entrySet()) {
            for (Session session : entry.getValue()) {
                totalMinutes += session.getMinutes();
            }
        }
        return totalMinutes;
    }

    public int getTotalMinutes(LocalDate date) {
        int totalMinutes = 0;
        for (Session session : this.sessionsByDate.get(date)) {
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

    public List<Session> getSessionsFromLastNDays(int days) {
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
        return sessions;
    }

    public void dailyUpdate() {
        System.out.println("Updating daily data");
        updateStreak();

        double consistencyBonus = getConsistencyBonus();
        double retention = getRetention();

        readingXp = (int) (readingXp * consistencyBonus);
        speakingXp = (int) (speakingXp * consistencyBonus);
        writingXp = (int) (writingXp * consistencyBonus);
        grindingXp = (int) (grindingXp * consistencyBonus);
        listeningXp = (int) (listeningXp * consistencyBonus);

        readingXp = (int) (readingXp * retention);
        speakingXp = (int) (speakingXp * retention);
        writingXp = (int) (writingXp * retention);
        grindingXp = (int) (grindingXp * retention);
        listeningXp = (int) (listeningXp * retention);

        lastStreakUpdate = today;


    }

    public void updateStreak() {

        for (LocalDate date = lastStreakUpdate; date.isBefore(today); date = date.plusDays(1)) {
            if ((!sessionsByDate.containsKey(date) || (sessionsByDate.get(date).isEmpty()))) {
                inactiveStreak++;
                activeStreak = 0;
            } else {
                inactiveStreak = 0;
                activeStreak++;
            }
        }
    }

    public double getRetention() {

        double totalMinutes = getTotalMinutes();

        return (totalMinutes + BASE_RETENTION)
                / (totalMinutes + BASE_RETENTION
                + (INACTIVITY_WEIGHT * inactiveStreak));
    }

    public double getConsistencyBonus() {

        return 1 + (0.05 * Math.log(activeStreak + 1));
    }


    public void displayGeneralProgress(Level level) {
        int ceiling = language.getXpCeiling(level);
        int xp = getXp();
        double percentage = ((double) xp / ceiling) * 100;
        double roundedPercentage = Math.round(percentage * Math.pow(10, 3)) / Math.pow(10, 3);

        System.out.println("Overall XP to " + level.toString() + ": " + xp + "/" + ceiling + "\nProgress: " + roundedPercentage + "%");

    }

    public void displayLevelProgress(ActivityCategory activityCategory) {

        Level level = getLevel();

        switch (level) {
            case BEGINNER -> level = Level.A1;
            case A1 -> level = Level.A2;
            case A2 -> level = Level.B1;
            case B1 -> level = Level.B2;
            case B2 -> level = Level.FLUENCY;
            case FLUENCY -> level = Level.C1;
            case C1 -> level = Level.C2;
        }

        double ceiling = language.getXpCeiling(level);


        if (activityCategory == ActivityCategory.WRITING) {
            ceiling *= 0.12;
        } else {
            ceiling *= 0.22;
        }


        int xp = getXp(activityCategory);

        double percentage = (xp / ceiling) * 100;

        double roundedPercentage = Math.round(percentage * Math.pow(10, 3)) / Math.pow(10, 3);

        System.out.println(activityCategory + " XP to " + level + ": " + xp + "/" + (int) ceiling + "\nProgress: " + roundedPercentage + "%");

        if (roundedPercentage > 100) {
            System.out.println("Your " + activityCategory + " XP has been reached. Consider doing activities of other categories so your general level and your " + activityCategory + " level match.");

        }

    }


    public int getXp() {
        return readingXp + speakingXp + writingXp + listeningXp + grindingXp;
    }

    public int getXp(ActivityCategory activityCategory) {
        return switch (activityCategory) {
            case LISTENING -> listeningXp;
            case GRINDING -> grindingXp;
            case SPEAKING -> speakingXp;
            case READING -> readingXp;
            case WRITING -> writingXp;
        };
    }

    public Level getLevel() {
        int xp = getXp();
        if (xp > language.getXpCeiling(Level.A1)) {
            if (xp > language.getXpCeiling(Level.A2)) {
                if (xp > language.getXpCeiling(Level.B1)) {
                    if (xp > language.getXpCeiling(Level.B2)) {
                        if (xp > language.getXpCeiling(Level.FLUENCY)) {
                            if (xp > language.getXpCeiling(Level.C1)) {
                                if (xp > language.getXpCeiling(Level.C2)) {
                                    return Level.C2;
                                } else {
                                    return Level.C1;
                                }
                            } else {
                                return Level.FLUENCY;
                            }
                        } else {
                            return Level.B2;
                        }
                    } else {
                        return Level.B1;
                    }
                } else {
                    return Level.A2;
                }
            } else {
                return Level.A1;
            }
        } else {
            return Level.BEGINNER;
        }
    }

    public Level getLevel(ActivityCategory activityCategory) {

        double multiplier;
        if (activityCategory == ActivityCategory.WRITING) {
            multiplier = 0.12;
        } else {
            multiplier = 0.22;
        }

        int xp = getXp(activityCategory);

        if (xp > language.getXpCeiling(Level.A1) * multiplier) {
            if (xp > language.getXpCeiling(Level.A2) * multiplier) {
                if (xp > language.getXpCeiling(Level.B1) * multiplier) {
                    if (xp > language.getXpCeiling(Level.B2) * multiplier) {
                        if (xp > language.getXpCeiling(Level.FLUENCY) * multiplier) {
                            if (xp > language.getXpCeiling(Level.C1) * multiplier) {
                                if (xp > language.getXpCeiling(Level.C2) * multiplier) {
                                    return Level.C2;
                                } else {
                                    return Level.C1;
                                }
                            } else {
                                return Level.FLUENCY;
                            }
                        } else {
                            return Level.B2;
                        }
                    } else {
                        return Level.B1;
                    }
                } else {
                    return Level.A2;
                }
            } else {
                return Level.A1;
            }
        } else {
            return Level.BEGINNER;
        }
    }

    public void removeSession(LocalDate date, int sessionID) {
        List<Session> sessionsOnDate = sessionsByDate.get(date);
        sessionsOnDate.removeIf(
                session -> session.getSessionID() == sessionID
        );
    }

    public void editSession(LocalDate date, int sessionID, LocalDate date2) {
        List<Session> sessionsOnDate = sessionsByDate.get(date);
        if (!sessionsOnDate.isEmpty()) {
            for (Session session : sessionsOnDate) {
                if (session.getSessionID() == sessionID) {
                    session.setDate(date2);
                }
            }
        }
    }

    public void editSession(LocalDate date, int sessionID, ActivityType activityType) {
        List<Session> sessionsOnDate = sessionsByDate.get(date);
        if (!sessionsOnDate.isEmpty()) {
            for (Session session : sessionsOnDate) {
                if (session.getSessionID() == sessionID) {
                    session.setActivityType(activityType);
                }
            }
        }
    }

    public void editSession(LocalDate date, int sessionID, int minutes) {
        List<Session> sessionsOnDate = sessionsByDate.get(date);
        if (!sessionsOnDate.isEmpty()) {
            for (Session session : sessionsOnDate) {
                if (session.getSessionID() == sessionID) {
                    session.setMinutes(minutes);
                }
            }
        }
    }

    public String getSessionsOfDayToString(LocalDate date) {
        if (sessionsByDate.containsKey(date)) {
            StringBuilder output = new StringBuilder();
            int count = 1;
            for (Session session : sessionsByDate.get(date)) {
                output.append("Stats for session ").append(count).append(" of day:\nMinutes: ").append(session.getMinutes()).append("\nActivity type: ").append(session.getActivityType()).append("\nXp gained: ").append(session.getXp()).append("\n");
                count++;
            }
            return output.toString();
        } else {
            return "No sessions found for " + date;
        }
    }

    public int getActiveStreak() {
        return activeStreak;
    }

    public int getInactiveStreak() {
        return inactiveStreak;
    }

    public void setLastStreakUpdate(LocalDate date) {
        lastStreakUpdate = date;
    }

    public void setToday(LocalDate date) {
        today = date;
    }

}