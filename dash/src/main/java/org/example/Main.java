package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws IOException {


        SessionManager manager = new SessionManager(Language.MANDARIN, 30, 0, LocalDate.of(2026,6,1), 150, 150, 150, 150, 150);
        manager.logSession(34, ActivityType.ANKI, LocalDate.of(2026, 6, 1));
        manager.logSession(64, ActivityType.COMPREHENSIBLE_INPUT_WITHOUT_SUBS, LocalDate.of(2026, 6, 1));


        System.out.println(manager.getSessions());
        System.out.println(manager.getLanguage());
        System.out.println(manager.getActiveStreak());
        System.out.println(manager.getInactiveStreak());
        System.out.println(manager.getLastStreakUpdate());
        System.out.println(manager.getStartReadingHours());
        System.out.println(manager.getStartSpeakingHours());
        System.out.println(manager.getStartGrindingHours());
        System.out.println(manager.getStartListeningHours());
        System.out.println(manager.getStartWritingHours());

        JsonStorage.save(manager);


        SessionManager sessionManager = JsonStorage.load();

        System.out.println(sessionManager.getSessions());
        System.out.println(sessionManager.getLanguage());
        System.out.println(sessionManager.getActiveStreak());
        System.out.println(sessionManager.getInactiveStreak());
        System.out.println(sessionManager.getLastStreakUpdate());
        System.out.println(sessionManager.getStartReadingHours());
        System.out.println(sessionManager.getStartSpeakingHours());
        System.out.println(sessionManager.getStartGrindingHours());
        System.out.println(sessionManager.getStartListeningHours());
        System.out.println(sessionManager.getStartWritingHours());

    }
}