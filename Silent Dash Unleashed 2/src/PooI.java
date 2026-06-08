import java.time.LocalDate;

public class PooI {
    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        String username = sc.nextLine();

        Session session1 = new Session(34, ActivityType.ANKI,Language.MANDARIN,LocalDate.of(2026,5,27));
        Session session2 = new Session(64, ActivityType.COMPREHENSIBLE_INPUT_WITHOUT_SUBS,Language.MANDARIN,LocalDate.of(2026,5,28));
        Session session3 = new Session(14, ActivityType.WRITTEN_CONVERSATION,Language.MANDARIN,LocalDate.of(2026,5,27));
        Session session4 = new Session(35,ActivityType.SPOKEN_CONVERSATION,Language.MANDARIN,LocalDate.of(2026,5,28));
        SessionManager sessionManager = new SessionManager(Language.MANDARIN,0);
        sessionManager.logSession(session1);
        sessionManager.logSession(session2);
        sessionManager.logSession(session3);
        sessionManager.logSession(session4);

        System.out.println(sessionManager.getTotalMinutes());
        System.out.println(sessionManager.getTotalMinutes(ActivityType.WRITTEN_CONVERSATION));
        System.out.println(sessionManager.getTotalMinutes(LocalDate.of(2026,5, 27)));

        sessionManager.displayGeneralProgress(Level.A1);
        sessionManager.displayGeneralProgress(Level.A2);
        sessionManager.displayGeneralProgress(Level.B1);
        sessionManager.displayGeneralProgress(Level.B2);
        sessionManager.displayGeneralProgress(Level.C1);
        sessionManager.displayGeneralProgress(Level.C2);
        sessionManager.displayGeneralProgress(Level.FLUENCY);

    }
}