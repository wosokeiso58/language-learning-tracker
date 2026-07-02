package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JsonStorage {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File file = new File("sessionManagers.json");

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void save(SessionManager manager) throws IOException {

        SessionManagerData writingData =
                new SessionManagerData(
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


            );

        mapper.writeValue(file, writingData);
    }

    public static SessionManager load() throws IOException {

        SessionManagerData loadedData = mapper.readValue(file,SessionManagerData.class);

        SessionManager sessionManager = new SessionManager(loadedData.getLanguage(),
                loadedData.getActiveStreak(),
                loadedData.getInactiveStreak(),
                loadedData.getLastStreakUpdate(),
                loadedData.getStartGrindingHours(),
                loadedData.getStartSpeakingHours(),
                loadedData.getStartReadingHours(),
                loadedData.getStartListeningHours(),
                loadedData.getStartWritingHours());

        for(Session session : loadedData.getSessions()){
            sessionManager.loadSession(session.getMinutes(),session.getActivityType(),session.getDate());
        }

        return sessionManager;


    }

}
