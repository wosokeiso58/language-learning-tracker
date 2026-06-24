import java.util.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.time.LocalDate;
import java.util.Objects;


public class DashFX extends Application {

    private static final String PURPLE = "#9B59B6";
    private Label selectedLabel;
    private VBox selectedCard;
    private Session selectedSession;
    private SessionManager sessionManager;
    private LocalDate pickedDate = LocalDate.now();
    private Label logDateLabel = new Label();
    private VBox sessionDisplay = createSessionDisplay();
    private Boolean isEditing = false;
    VBox sessionLayout = new VBox();

    static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        sessionManager = new SessionManager(Language.KOREAN, 0, 0, 0, 0, 0);

        TabPane tabPane = new TabPane();

        Tab calendarTab = new Tab("Calendar");
        Tab progressTab = new Tab("Progress");

        tabPane.getTabs().addAll(calendarTab, progressTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);


        Label l = new Label("no date selected");

        DatePicker d = new DatePicker();
        d.setShowWeekNumbers(true);

        TextArea sessionOutput = new TextArea();
        sessionOutput.setText(sessionManager.getSessionsOfDayToString(pickedDate));
        Label newDateLabel = new Label("New Date: " + pickedDate);




        VBox calendar = new VBox(d, l);

        Label sessionsLabel = new Label("Sessions:");

        sessionOutput.setEditable(false);

        Button logMenuButton = new Button("Log session");
        Button editButton = new Button("Edit session");
        Button deleteButton = new Button("Delete session");
        sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);
        HBox calendarLayout = new HBox(calendar, sessionLayout);
        calendarTab.setContent(calendarLayout);



        Label activityTypeLabel = new Label("Activity Type:");
        ComboBox<ActivityType> activityBox = new ComboBox<>();
        activityBox.setPromptText("Select an activity");
        activityBox.getItems().addAll(ActivityType.values());
        Label minutesLabel = new Label("Minutes:");
        TextField minutesInput = new TextField();
        TextArea logOutput = new TextArea();
        logOutput.setEditable(false);
        Button logButton = new Button("Log");
        Button closeButton = new Button("Close");
        Button saveButton = new Button("Save");

        EventHandler<ActionEvent> setCalendar = e -> {
            pickedDate = d.getValue();
            sessionManager.makeDayNotNull(pickedDate);

            l.setText("Date : " + pickedDate);

            sessionOutput.setText(sessionManager.getSessionsOfDayToString(pickedDate));
            logDateLabel.setText("Logging session for " + pickedDate);
            newDateLabel.setText("New Date: " + pickedDate);


            if(!isEditing){
                sessionDisplay = createSessionDisplay();
                sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);
                HBox newCalendarLayout = new HBox(calendar, sessionLayout);
                calendarTab.setContent(newCalendarLayout);
            }
        };


        EventHandler<ActionEvent> setLogger = e -> {
            isEditing = true;
            logDateLabel.setText("Logging session for " + pickedDate);
            HBox loggerButtons = new HBox(logButton, closeButton);
            VBox loggerLayout = new VBox(logDateLabel, activityTypeLabel, activityBox, minutesLabel, minutesInput, logOutput, loggerButtons);
            activityBox.setValue(null);
            minutesInput.setText("");
            logOutput.setText("");
            HBox hBox2 = new HBox(calendar, loggerLayout);
            calendarTab.setContent(hBox2);
            tabPane.requestLayout();


        };
        EventHandler<ActionEvent> cancelLogger = e -> {
            isEditing = false;
            sessionDisplay = createSessionDisplay();
            sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);

            HBox hBox3 = new HBox(calendar, sessionLayout);
            calendarTab.setContent(hBox3);
            sessionOutput.setText(sessionManager.getSessionsOfDayToString(pickedDate));

        };


        EventHandler<ActionEvent> logSession = e -> {
            try {
                if (pickedDate == null) {
                    logOutput.setText("No date selected!\nSelect a date!");
                } else {
                    if (!(Objects.equals(minutesInput.getText(), "") || (activityBox.getSelectionModel().getSelectedItem() == null))) {
                        int minutes = Integer.parseInt(minutesInput.getText());
                        ActivityType activityType = activityBox.getSelectionModel().getSelectedItem();
                        logOutput.setText(sessionManager.logSession(minutes, activityType, pickedDate));

                    } else {
                        StringBuilder stringBuilder = new StringBuilder();

                        if (Objects.equals(minutesInput.getText(), "")) {
                            stringBuilder.append("No minutes entered!\nEnter the minutes!\n\n");
                        }
                        if (activityBox.getSelectionModel().getSelectedItem() == null) {
                            stringBuilder.append("No activity selected!\nSelect an activity!");
                        }
                        logOutput.setText(stringBuilder.toString());
                    }
                }
            } catch (NumberFormatException ex) {
                logOutput.setText("Enter valid minutes.");
            }
        };

        EventHandler<ActionEvent> setEditor = e -> {
            isEditing = true;
            if(selectedSession != null) {
                activityBox.setValue(selectedSession.getActivityType());
                minutesInput.setText(String.valueOf(selectedSession.getMinutes()));
                logOutput.setText("");
                Label editDateLabel = new Label("Editing session "+ calculateSessionNumberOfDay() + " on " + selectedSession.getDate());
                Label multiplierLabel = new Label("Variety multiplier: " +selectedSession.getVariety());
                HBox editorButtons = new HBox(saveButton,closeButton);
                VBox editorLayout = new VBox(editDateLabel,newDateLabel, multiplierLabel, activityTypeLabel, activityBox, minutesLabel, minutesInput, logOutput, editorButtons);
                HBox hBox3 = new HBox(calendar, editorLayout);
                calendarTab.setContent(hBox3);
            }

        };

        EventHandler<ActionEvent> saveSession = e -> {
            try {
                if (pickedDate == null) {
                    logOutput.setText("No date selected!\nSelect a date!");
                } else {
                    if (!(Objects.equals(minutesInput.getText(), "") || (activityBox.getSelectionModel().getSelectedItem() == null))) {
                        int minutes = Integer.parseInt(minutesInput.getText());
                        ActivityType activityType = activityBox.getSelectionModel().getSelectedItem();
                        logOutput.setText(sessionManager.editSession(selectedSession.getDate(),selectedSession, pickedDate,activityType,minutes));

                    } else {
                        StringBuilder stringBuilder = new StringBuilder();

                        if (Objects.equals(minutesInput.getText(), "")) {
                            stringBuilder.append("No minutes entered!\nEnter the minutes!\n\n");
                        }
                        if (activityBox.getSelectionModel().getSelectedItem() == null) {
                            stringBuilder.append("No activity selected!\nSelect an activity!");
                        }
                        logOutput.setText(stringBuilder.toString());
                    }
                }
            } catch (NumberFormatException ex) {
                logOutput.setText("Enter valid minutes.");
            }
        };

        EventHandler<ActionEvent> deleteSession = e -> {
            if(selectedSession != null) {
                sessionManager.deleteSession(selectedSession);
                setCalendar.handle(e);
            }
        };



        progressTab.setOnSelectionChanged(event -> {
            Label xpLabel = new Label("Overall XP to " + sessionManager.getNextLevel().toString() + ": " + sessionManager.getXp() + "/" + sessionManager.getCeiling());
            Label totalProgressLabel = new Label("Progress to " + sessionManager.getNextLevel() + ": "+ sessionManager.getTotalProgress() + "%");
            Label levelLabel = new Label(sessionManager.getLevel().toString());
            ProgressBar totalProgress = new ProgressBar();
            totalProgress.setProgress(sessionManager.getTotalProgress()/100);
            Label nextLevelLabel = new Label(sessionManager.getNextLevel().toString());
            Label totalMinutesLabel = new Label("Total minutes: " + sessionManager.getTotalMinutes());
            Button toggleBars = new Button("Toggle category progress");


            HBox progressBarBox =  new HBox(levelLabel, totalProgress, nextLevelLabel);
            VBox progressBox = new VBox(xpLabel, totalProgressLabel, progressBarBox,  totalMinutesLabel);
            progressTab.setContent(progressBox);
            System.out.println(sessionManager.getLevel());
            sessionManager.displayGeneralProgress(sessionManager.getLevel());

            //TODO fix the bar and display the XP and like 100 other things
        });


        d.setOnAction(setCalendar);
        logMenuButton.setOnAction(setLogger);
        editButton.setOnAction(setEditor);
        logButton.setOnAction(logSession);
        closeButton.setOnAction(cancelLogger);
        saveButton.setOnAction(saveSession);
        deleteButton.setOnAction(deleteSession);


        VBox root = new VBox(tabPane);
        Scene scene = new Scene(root, 540, 460);
        stage.setScene(scene);
        stage.setTitle("Language Dash");
        stage.show();
    }

    public VBox createSessionDisplay() {
        sessionDisplay = new VBox();
        try {
            if (!Objects.requireNonNull(sessionManager).getSessionsByDate(pickedDate).isEmpty()) {
                List<Session> daySessions = sessionManager.getSessionsByDate(pickedDate);
                int count = 0;
                for (Session session : daySessions) {
                    Label sessionLabel = new Label(session.toString());
                    sessionDisplay.getChildren().add(sessionLabel);
                    sessionLabel.setStyle(
                            "-fx-border-color: blue;"
                    );
                    if(count==0){
                        selectedSession = session;
                        selectedLabel = sessionLabel;
                        selectedLabel.setStyle(
                                "-fx-background-color: lightblue;" + "-fx-border-color: blue;"
                        );
                    }
                    count++;
                    sessionLabel.setOnMouseClicked(e -> {
                        if (selectedLabel != null) {
                            selectedLabel.setStyle(
                                    "-fx-background-color: white;" + "-fx-border-color: blue;");
                        }

                        selectedLabel = sessionLabel;
                        selectedSession = session;
                        sessionLabel.setStyle(
                                "-fx-background-color: lightblue;" + "-fx-border-color: blue;"
                        );

                    });
                }
            }
            else{
                selectedSession = null;
                selectedLabel = null;
            }
        } catch (NullPointerException ex) {
            sessionDisplay = new VBox();
        }

        return sessionDisplay;
    }

    public int calculateSessionNumberOfDay() {
        List<Session> daySessions = sessionManager.getSessionsByDate(pickedDate);
        int count = 0;
        for (Session session : daySessions) {
            if(session.equals(selectedSession)) {
                break;
            }
            count++;
        }
        return count+1;
    }


}
