import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Scanner;

public class PooI {
    public static void main(String[] args) {
        //Scanner sc = new Scanner(System.in);
        //String username = sc.nextLine();
        Session session1 = new Session(34, ActivityType.ANKI,Language.ESPERANTO,LocalDate.of(2026,5,27));
        Session session2 = new Session(64, ActivityType.COMPREHENSIBLE_INPUT_WITHOUT_SUBS,Language.ESPERANTO,LocalDate.of(2026,5,28));
        Session session3 = new Session(14, ActivityType.WRITTEN_CONVERSATION,Language.ESPERANTO,LocalDate.of(2026,5,27));
        SessionManager sessionManager = new SessionManager(Language.ESPERANTO,0);
        sessionManager.logSession(session1);
        sessionManager.logSession(session2);
        sessionManager.logSession(session3);

        System.out.println(sessionManager.getTotalMinutes());
    }
}