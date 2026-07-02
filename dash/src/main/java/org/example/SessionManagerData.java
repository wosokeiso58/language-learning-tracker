package org.example;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class SessionManagerData {

    private final List<Session> sessions;
    private final Language language;
    private final int activeStreak;
    private final int inactiveStreak;
    private final LocalDate lastStreakUpdate;

    private final int startSpeakingHours;
    private final int startListeningHours;
    private final int startGrindingHours;
    private final int startWritingHours;
    private final int startReadingHours;

    @JsonCreator
    public SessionManagerData(@JsonProperty("sessions")List<Session> sessions,
                              @JsonProperty("language")Language language,
                              @JsonProperty("activeStreak")int activeStreak,
                              @JsonProperty("inactiveStreak")int inactiveStreak,
                              @JsonProperty("lastStreakUpdate")LocalDate lastStreakUpdate,
                              @JsonProperty("startReadingHours")int startReadingHours,
                              @JsonProperty("startGrindingHours")int startGrindingHours,
                              @JsonProperty("startSpeakingHours")int startSpeakingHours,
                              @JsonProperty("startListeningHours")int startListeningHours,
                              @JsonProperty("startWritingHours")int startWritingHours) {

        this.sessions = sessions;
        this.language = language;
        this.activeStreak = activeStreak;
        this.inactiveStreak = inactiveStreak;
        this.lastStreakUpdate = lastStreakUpdate;
        this.startReadingHours = startReadingHours;
        this.startGrindingHours = startGrindingHours;
        this.startSpeakingHours = startSpeakingHours;
        this.startListeningHours = startListeningHours;
        this.startWritingHours = startWritingHours;

    }


    public List<Session> getSessions() {
        return sessions;
    }

    public Language getLanguage() {
        return language;
    }

    public int getActiveStreak() {
        return activeStreak;
    }

    public int getInactiveStreak() {
        return inactiveStreak;
    }

    public LocalDate getLastStreakUpdate() {
        return lastStreakUpdate;
    }

    public int getStartSpeakingHours() {
        return startSpeakingHours;
    }

    public int getStartListeningHours() {
        return startListeningHours;
    }

    public int getStartGrindingHours() {
        return startGrindingHours;
    }

    public int getStartWritingHours() {
        return startWritingHours;
    }

    public int getStartReadingHours() {
        return startReadingHours;
    }
}
