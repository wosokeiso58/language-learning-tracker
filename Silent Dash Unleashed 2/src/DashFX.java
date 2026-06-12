import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.time.LocalDate;

public class DashFX extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {

        SessionManager sessionManager = new SessionManager(Language.KOREAN,0,0,0,0,0);

        TabPane tabPane = new TabPane();

        Tab calendarTab = new Tab("Calendar");
        Tab loggerTab = new Tab("Logger");
        Tab progressTab = new Tab("Progress");

        tabPane.getTabs().addAll(calendarTab, loggerTab, progressTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        TilePane calendar = new TilePane();

        Label l = new Label("no date selected");

        DatePicker d = new DatePicker();
        TextArea sessionOutput = new TextArea();

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // get the date picker value
                LocalDate i = d.getValue();

                // get the selected date
                l.setText("Date :" + i);

                sessionOutput.setText(sessionManager.getSessionsOfDayToString(i));


            }
        };

        d.setShowWeekNumbers(true);
        d.setOnAction(event);
        calendar.getChildren().add(d);
        calendar.getChildren().add(l);
        calendarTab.setContent(calendar);

        Label sessionsLabel = new Label("Sessions:");

        sessionOutput.setEditable(false);


        VBox calendarLayout = new VBox(sessionsLabel);

        VBox root = new VBox(tabPane);
        Scene scene = new Scene(root, 540, 460);
        stage.setScene(scene);
        stage.setTitle("Language Dash");
        stage.show();
    }

}
