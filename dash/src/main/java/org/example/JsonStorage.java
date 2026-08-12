package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;


public class JsonStorage {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File file = new File("sessionManagers.json");

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            if (!file.exists()) {
                Files.writeString(file.toPath(), "{\"sessionManagers\":[]}");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create sessionManagers.json", e);
        }
    }

    public static void save(List<SessionManager> managers) throws IOException {
        List<SessionManagerData> data = new ArrayList<>();

        for (SessionManager manager : managers) {
            data.add(new SessionManagerData(
                    manager.getSessions(),
                    manager.getLanguage(),
                    manager.getActiveStreak(),
                    manager.getInactiveStreak(),
                    manager.getLastStreakUpdate(),
                    manager.getStartReadingHours(),
                    manager.getStartSpeakingHours(),
                    manager.getStartGrindingHours(),
                    manager.getStartListeningHours(),
                    manager.getStartWritingHours()


            ));
        }
        mapper.writeValue(file, new AppData(data));
    }

    public static List<SessionManager> load() throws IOException {
        List<SessionManager> sessionManagers = new ArrayList<>();

        AppData appData = mapper.readValue(file, AppData.class);

        for(SessionManagerData sessionManagerData : appData.sessionManagers()){
            SessionManager sessionManager = new SessionManager(sessionManagerData.language(),
                    sessionManagerData.activeStreak(),
                    sessionManagerData.inactiveStreak(),
                    sessionManagerData.lastStreakUpdate(),
                    sessionManagerData.startGrindingHours(),
                    sessionManagerData.startSpeakingHours(),
                    sessionManagerData.startReadingHours(),
                    sessionManagerData.startListeningHours(),
                    sessionManagerData.startWritingHours());

            for(Session session : sessionManagerData.sessions()){
                sessionManager.loadSession(session.getMinutes(),session.getActivityType(),session.getDate(), session.getVariety());
            }
            sessionManagers.add(sessionManager);
        }
        return sessionManagers;

    }

}
