import java.time.LocalDate;

public class PooI {
    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        String username = sc.nextLine();



        SessionManager sessionManager = new SessionManager(Language.MANDARIN,450,150,150,150,150);

        sessionManager.logSession(34, ActivityType.ANKI,LocalDate.of(2026,6,1));

        System.out.println("Active Streak: "+sessionManager.getActiveStreak());
        System.out.println("Inactive Streak: "+sessionManager.getInactiveStreak());

        sessionManager.logSession(64, ActivityType.COMPREHENSIBLE_INPUT_WITHOUT_SUBS,LocalDate.of(2026,6,1));

        System.out.println("Active Streak: "+sessionManager.getActiveStreak());
        System.out.println("Inactive Streak: "+sessionManager.getInactiveStreak());

        sessionManager.setLastStreakUpdate(LocalDate.of(2026,6,1));
        sessionManager.logSession(14, ActivityType.WRITTEN_CONVERSATION,LocalDate.of(2026,6,10));

        System.out.println("Active Streak: "+sessionManager.getActiveStreak());
        System.out.println("Inactive Streak: "+sessionManager.getInactiveStreak());

        sessionManager.setToday(LocalDate.of(2026,6,12));
        sessionManager.logSession(35,ActivityType.SPOKEN_CONVERSATION,LocalDate.of(2026,6,10));


        sessionManager.setToday(LocalDate.of(2026,6,13));
        sessionManager.logSession(24,ActivityType.SHADOWING,LocalDate.of(2026,6,13));
        System.out.println("Active Streak: "+sessionManager.getActiveStreak());
        System.out.println("Inactive Streak: "+sessionManager.getInactiveStreak());

        sessionManager.setToday(LocalDate.of(2026,6,14));
        sessionManager.logSession(24,ActivityType.SHADOWING,LocalDate.of(2026,6,14));
        System.out.println("Active Streak: "+sessionManager.getActiveStreak());
        System.out.println("Inactive Streak: "+sessionManager.getInactiveStreak());

        sessionManager.setToday(LocalDate.of(2026,6,15));
        sessionManager.logSession(24,ActivityType.SHADOWING,LocalDate.of(2026,6,15));
        System.out.println("Active Streak: "+sessionManager.getActiveStreak());
        System.out.println("Inactive Streak: "+sessionManager.getInactiveStreak());

        System.out.println(sessionManager.getSessionsOfDayToString(LocalDate.of(2026,6,1)));
//
//        sessionManager.displayGeneralProgress(Level.A1);
//        sessionManager.displayGeneralProgress(Level.A2);
//        sessionManager.displayGeneralProgress(Level.B1);
//        sessionManager.displayGeneralProgress(Level.B2);
//        sessionManager.displayGeneralProgress(Level.C1);
//        sessionManager.displayGeneralProgress(Level.C2);
//        sessionManager.displayGeneralProgress(Level.FLUENCY);
//
//        sessionManager.displayLevelProgress(ActivityCategory.GRINDING);
//        sessionManager.displayLevelProgress(ActivityCategory.SPEAKING);
    }
}