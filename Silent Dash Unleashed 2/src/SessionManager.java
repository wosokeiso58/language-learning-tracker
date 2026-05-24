import java.util.List;

public class SessionManager {
    private List<Session> sessions;

    public SessionManager() {}

    public void logSession(Session session) {
        sessions.add(session);
    }
}
