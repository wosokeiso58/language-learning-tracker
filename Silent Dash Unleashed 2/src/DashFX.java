import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
        Tab progressTab = new Tab("Progress");

        tabPane.getTabs().addAll(calendarTab, progressTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);


        Label l = new Label("no date selected");

        DatePicker d = new DatePicker();
        TextArea sessionOutput = new TextArea();
        Label dateLabel = new Label("Logging session for "+ d.getValue());

        EventHandler<ActionEvent> setCalendar = e -> {
            // get the date picker value
            LocalDate i = d.getValue();


            // get the selected date
            l.setText("Date : " + i);

            sessionOutput.setText(sessionManager.getSessionsOfDayToString(i));

            dateLabel.setText("Logging session for "+ d.getValue());


        };

        d.setShowWeekNumbers(true);
        d.setOnAction(setCalendar);

        VBox calendar = new VBox(d,l);

        Label sessionsLabel = new Label("Sessions:");

        sessionOutput.setEditable(false);

        Button logButton = new Button("Log session");
        Button editButton = new Button("Edit session");
        Button deleteButton = new Button("Delete session");

        VBox sessionLayout = new VBox(sessionsLabel,sessionOutput, logButton,editButton,deleteButton);
        HBox calendarLayout = new HBox(calendar, sessionLayout);

        Label activityTypeLabel = new Label("Activity Type:");
        ComboBox<ActivityType> activityBox = new ComboBox<>();
        activityBox.getItems().addAll(ActivityType.values());
        Label minutesLabel = new Label("Minutes:");
        TextField minutesInput = new TextField();
        TextArea logOutput = new TextArea();
        logOutput.setEditable(false);
        Button logButton2  = new Button("Log");
        Button cancelButton = new Button("Cancel");
        Button closeButton = new Button("Close");
        HBox loggerButtons =  new HBox(logButton2,cancelButton);


        VBox loggerLayout = new VBox(dateLabel, activityTypeLabel, activityBox, minutesLabel, minutesInput, logOutput,loggerButtons);



        EventHandler<ActionEvent> setLogger = e -> {

            HBox hBox2 = new HBox(calendar, loggerLayout);
            calendarTab.setContent(hBox2);
        };
        EventHandler<ActionEvent> cancelLogger = e -> {
            HBox hBox3 = new HBox(calendar, sessionLayout);
            calendarTab.setContent(hBox3);
            sessionOutput.setText(sessionManager.getSessionsOfDayToString(d.getValue()));
        };

        EventHandler<ActionEvent> logSession = e -> {

            int minutes = Integer.parseInt(minutesInput.getText());
            ActivityType activityType = activityBox.getSelectionModel().getSelectedItem();
            cancelButton.setOnAction(setLogger);
            logOutput.setText(sessionManager.logSession(minutes,activityType,d.getValue()));
            HBox loggerButtons2 =  new HBox(logButton2,closeButton);
            VBox loggerLayout2 = new VBox(dateLabel, activityTypeLabel, activityBox, minutesLabel, minutesInput, logOutput,loggerButtons2);
            HBox hBox4 = new HBox(calendar, loggerLayout2);
            calendarTab.setContent(hBox4);

        };



        logButton.setOnAction(setLogger);
        cancelButton.setOnAction(cancelLogger);
        logButton2.setOnAction(logSession);

        calendarTab.setContent(calendarLayout);

        VBox root = new VBox(tabPane);
        Scene scene = new Scene(root, 540, 460);
        stage.setScene(scene);
        stage.setTitle("Language Dash");
        stage.show();
    }

}
