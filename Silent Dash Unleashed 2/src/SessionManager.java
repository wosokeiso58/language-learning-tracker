import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private List<Session> sessions;
    private Language language;

    public SessionManager(Language language) {
        sessions = new ArrayList<>();
        this.language = language;
    }

    public void logSession(Session session) {
        sessions.add(session);
    }

    public int getTotalMinutes() {
        int totalMinutes = 0;
        for (Session session : sessions) {
            totalMinutes += session.getMinutes();
        }
        return totalMinutes;
    }
}
