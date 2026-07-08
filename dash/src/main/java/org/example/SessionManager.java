package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.*;

public class SessionManager {


    private final Map<LocalDate, List<Session>> sessionsByDate;

    private final Language language;

    private int activeStreak;
    private int inactiveStreak;
    private LocalDate lastStreakUpdate;

    private static double BASE_RETENTION;
    private static double INACTIVITY_WEIGHT;

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

    private int currentSpeakingXp;
    private int currentListeningXp;
    private int currentGrindingXp;
    private int currentWritingXp;
    private int currentReadingXp;

    LocalDate today = LocalDate.now();

    public SessionManager(Language language, int activeStreak, int inactiveStreak, LocalDate lastStreakUpdate, int grindingHours, int speakingHours, int readingHours, int listeningHours, int writingHours) {
        this.language = language;
        sessionsByDate = new HashMap<>();

        this.activeStreak = activeStreak;
        this.inactiveStreak = inactiveStreak;
        this.lastStreakUpdate = lastStreakUpdate;

        startGrindingMinutes = grindingHours * 60;
        startListeningMinutes = listeningHours * 60;
        startSpeakingMinutes = speakingHours * 60;
        startWritingMinutes = writingHours * 60;
        startReadingMinutes = readingHours * 60;

        BASE_RETENTION = 1000.0;
        INACTIVITY_WEIGHT = 50.0;


        grindingXp = grindingHours * 60 * 150;
        readingXp = readingHours * 60 * 150;
        speakingXp = speakingHours * 60 * 150;
        listeningXp = listeningHours * 60 * 150;
        writingXp = writingHours * 60 * 150;
    }

    public int logSession(int minutes, ActivityType activityType, LocalDate date) {
        int xp = addSession(allocateXp(minutes, activityType, date));
        if (lastStreakUpdate.isBefore(today)) {
            dailyUpdate();
        }
        return xp;
    }

    public void loadSession(int minutes, ActivityType activityType, LocalDate date) {
        addSession(allocateXp(minutes, activityType, date));

    }

    public int addSession(Session session) {

        if (this.sessionsByDate.containsKey(session.getDate())) {
            this.sessionsByDate.get(session.getDate()).add(session);
        } else {
            this.sessionsByDate.put(session.getDate(), new ArrayList<>());
            this.sessionsByDate.get(session.getDate()).add(session);
        }

        return session.getXp();
    }

    public Session allocateXp(int minutes, ActivityType activityType, LocalDate date) {


        int calculatedXp;
        int gainedXp = 0;
        double multiplier = getVariety();

        double coefficient;

        coefficient = activityType.getGrindingCoefficient();
        calculatedXp = (int) (minutes * multiplier * coefficient * 100);
        grindingXp += calculatedXp;
        gainedXp += calculatedXp;
        currentGrindingXp = calculatedXp;
        coefficient = activityType.getReadingCoefficient();
        calculatedXp = (int) (minutes * multiplier * coefficient * 100);
        readingXp += calculatedXp;
        gainedXp += calculatedXp;
        currentReadingXp = calculatedXp;
        coefficient = activityType.getSpeakingCoefficient();
        calculatedXp = (int) (minutes * multiplier * coefficient * 100);
        speakingXp += calculatedXp;
        gainedXp += calculatedXp;
        currentSpeakingXp = calculatedXp;
        coefficient = activityType.getWritingCoefficient();
        calculatedXp = (int) (minutes * multiplier * coefficient * 100);
        writingXp += calculatedXp;
        gainedXp += calculatedXp;
        currentWritingXp = calculatedXp;
        coefficient = activityType.getListeningCoefficient();
        calculatedXp = (int) (minutes * multiplier * coefficient * 100);
        listeningXp += calculatedXp;
        gainedXp += calculatedXp;
        currentListeningXp = calculatedXp;

        return new Session(minutes, activityType, date, gainedXp);

    }

    public int getXpJustCalculated(ActivityCategory activityCategory) {
        return switch (activityCategory) {
            case WRITING -> currentWritingXp;
            case READING -> currentReadingXp;
            case GRINDING -> currentGrindingXp;
            case SPEAKING -> currentSpeakingXp;
            case LISTENING -> currentListeningXp;
        };
    }


    public double getVariety() {
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
            return 1.15;
        } else if (balanceCount == 15) {
            return 1;
        } else if (balanceCount == 11) {
            return 1;
        } else if (balanceCount == 6) {
            return 1;
        } else if (balanceCount == 5) {
            return 0.875;
        } else if (balanceCount == 10) {
            return 0.875;
        } else if (balanceCount == 1) {
            return 0.875;
        } else {
            return 0.5;
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

    public int getWeekMinutes() {
        int weekMinutes = 0;
        for (Session session : getSessionsFromLastNDays(7)) {
            weekMinutes += session.getMinutes();
        }
        return weekMinutes;
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

    public List<Session> getSessionsByDate(LocalDate date) {
        if (sessionsByDate.containsKey(date)) {
            return sessionsByDate.get(date);
        } else {
            return new ArrayList<>();
        }
    }

    public void dailyUpdate() {

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
        System.out.println("updating streak");

        for (LocalDate date = lastStreakUpdate; date.isBefore(today); date = date.plusDays(1)) {
            if ((!sessionsByDate.containsKey(date) || (sessionsByDate.get(date).isEmpty()))) {
                inactiveStreak++;
                System.out.println("adding to inactive streak");
                activeStreak = 0;
            } else {
                System.out.println("adding to active streak");
                inactiveStreak = 0;
                activeStreak++;
            }
        }
        System.out.println("inactive streak " + inactiveStreak);
        System.out.println("active streak " + activeStreak);
    }

    public double getRetention() {

        double totalMinutes = getTotalMinutes();
        return Math.round((totalMinutes + BASE_RETENTION) / (totalMinutes + BASE_RETENTION + (INACTIVITY_WEIGHT * inactiveStreak)) * Math.pow(10, 3)) / Math.pow(10, 3);
    }

    public double getConsistencyBonus() {

        double percentage = 1 + (0.05 * Math.log(activeStreak + 1));
        return Math.round(percentage * Math.pow(10, 3)) / Math.pow(10, 3);
    }


    public void displayGeneralProgress(Level level) {
        int ceiling = language.getXpCeiling(level);
        int xp = getXp()-getFloor();
        double percentage = ((double) xp / ceiling) * 100;
        double roundedPercentage = Math.round(percentage * Math.pow(10, 3)) / Math.pow(10, 3);

        System.out.println("Overall XP to " + level.toString() + ": " + xp + "/" + ceiling + "\nProgress: " + roundedPercentage + "%");

    }


    public int getCeiling() {
        return language.getXpCeiling(getNextLevel(getLevel()));

    }

    public int getCeiling(ActivityCategory activityCategory) {
        return language.getXpCeiling(getNextLevel(getLevel(activityCategory)));
    }


    public int getCategoryCeiling(ActivityCategory activityCategory) {
        if (activityCategory == ActivityCategory.WRITING) {
            return (int) (getCeiling(activityCategory) * 0.12);
        } else {
            return (int) (getCeiling(activityCategory) * 0.22);
        }
    }

    public int getFloor(){
        return language.getXpCeiling(getLevel());
    }

    public int getFloor(ActivityCategory activityCategory){
        return language.getXpCeiling(getLevel(activityCategory));
    }

    public int getCategoryFloor(ActivityCategory activityCategory){
        if (activityCategory == ActivityCategory.WRITING) {
            return (int) (getFloor(activityCategory) * 0.12);
        }
        else{
            return (int) (getFloor(activityCategory) * 0.22);
        }
    }

    public double getXpProgress(ActivityCategory activityCategory) {
        int xp = getXp(activityCategory)-getCategoryFloor(activityCategory);
        double percentage = ((double) xp / (getCategoryCeiling(activityCategory)-getCategoryFloor(activityCategory))) * 100;
        return Math.round(percentage * Math.pow(10, 3)) / Math.pow(10, 3);
    }

    public double getTotalProgress() {
        int xp = getXp()-getFloor();
        double percentage = ((double) xp / (getCeiling()-getFloor())) * 100;
        return Math.round(percentage * Math.pow(10, 3)) / Math.pow(10, 3);

    }


    public Level getNextLevel(Level level) {
        return switch (level) {
            case BEGINNER -> Level.A1;
            case A1 -> Level.A2;
            case A2 -> Level.B1;
            case B1 -> Level.B2;
            case B2 -> Level.FLUENCY;
            case FLUENCY -> Level.C1;
            case C1, C2 -> Level.C2;
        };
    }


    public int getXp() {
        return readingXp + speakingXp + writingXp + listeningXp + grindingXp;
    }
    @JsonIgnore
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


    public void editSession(Session session, LocalDate newDate, ActivityType activityType, int minutes) {
        System.out.println("Before editing" + this.getSessionsByDate(newDate).size());
//        if(!((session.getDate().equals(newDate))&&(session.getActivityType().equals(activityType))&&(session.getMinutes() == minutes))) {
            deleteSession(session);
            addSession(allocateXp(minutes, activityType, newDate));
//        }
    }


    public void deleteSession(Session session) {

        allocateXp(session.getMinutes(), session.getActivityType(), session.getDate());

        grindingXp -= currentGrindingXp * 2;
        readingXp -= currentReadingXp * 2;
        speakingXp -= currentSpeakingXp * 2;
        writingXp -= currentWritingXp * 2;
        listeningXp -= currentListeningXp * 2;

        if (grindingXp < 0) {
            grindingXp = 0;
        }
        if (readingXp < 0) {
            readingXp = 0;
        }
        if (speakingXp < 0) {
            speakingXp = 0;
        }
        if (writingXp < 0) {
            writingXp = 0;
        }
        if (listeningXp < 0) {
            listeningXp = 0;
        }

        sessionsByDate.get(session.getDate()).remove(session);
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

    public void makeDayNotNull(LocalDate date) {
        if (!sessionsByDate.containsKey(date)) {
            sessionsByDate.put(date, new ArrayList<>());
        }
    }

    public Language getLanguage() {
        return language;
    }

    public LocalDate getLastStreakUpdate() {
        return lastStreakUpdate;
    }

    public int getStartReadingHours() {
        return startReadingMinutes/60;
    }

    public int getStartWritingHours() {
        return startWritingMinutes/60;
    }

    public int getStartGrindingHours() {
        return startGrindingMinutes/60;
    }

    public int getStartListeningHours() {
        return startListeningMinutes/60;
    }

    public int getStartSpeakingHours() {
        return startSpeakingMinutes/60;
    }

    public List<Session> getSessions() {
        List<Session> sessions = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Session>> entry : sessionsByDate.entrySet()) {
            sessions.addAll(entry.getValue());
        }
        return sessions;
    }

    }