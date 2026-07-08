package org.example;

import java.time.LocalDate;
import java.util.List;

public record SessionManagerData(List<Session> sessions,
                                 Language language,
                                 int activeStreak,
                                 int inactiveStreak,
                                 LocalDate lastStreakUpdate,
                                 int startReadingHours,
                                 int startGrindingHours,
                                 int startSpeakingHours,
                                 int startListeningHours,
                                 int startWritingHours) {

}
