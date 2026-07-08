package org.example;

import java.io.IOException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.time.LocalDate;
import java.util.Objects;


public class DashFX extends Application {

    private Label selectedLabel;
    private Label selectedMLabel;
    private Session selectedSession;
    private SessionManager sessionManager;
    private LocalDate pickedDate = LocalDate.now();
    private VBox sessionDisplay = new VBox();
    public List<SessionManager> sessionManagerList = new ArrayList<>();
    private final Button changeLanguageButton = new Button("Change Language");
    private Boolean isEditing = false;
    private final VBox root = new VBox();
    private TabPane tabPane;
    VBox sessionLayout = new VBox();

    @Override
    public void start(Stage stage) throws IOException {


        sessionManagerList = JsonStorage.load();

        this.tabPane = new TabPane();

        Tab calendarTab = new Tab("Calendar");
        Tab progressTab = new Tab("Progress");

        tabPane.getTabs().addAll(calendarTab, progressTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Label logDateLabel = new Label();

        Label l = new Label("Date : " + pickedDate);

        DatePicker d = new DatePicker();
        d.setShowWeekNumbers(true);


        Label newDateLabel = new Label("New Date: " + pickedDate);


        VBox calendar = new VBox(d, l);

        Label sessionsLabel = new Label("Sessions:");

        Button createButton = new Button("Create Session Manager");
        Button deleteSessionManagerButton = new Button("Delete session manager");
        Button newSessionManagerButton = new Button("New Session Manager");

        ComboBox<Language> languageBox = new ComboBox<>();
        languageBox.setPromptText("Select a language");
        languageBox.getItems().addAll(Language.values());

        Button cancelButton = new Button("Cancel");
        TextArea newSessionManagerOutput = new TextArea();

        Label grindingHoursLabel = new Label("Grinding Hours:");
        TextField grindingHoursInput = new TextField();

        Label speakingHoursLabel = new Label("Speaking Hours:");
        TextField speakingHoursInput = new TextField();

        Label listeningHoursLabel = new Label("Listening Hours:");
        TextField listeningHoursInput = new TextField();

        Label readingHoursLabel = new Label("Reading Hours:");
        TextField readingHoursInput = new TextField();

        Label writingHoursLabel = new Label("Writing Hours:");
        TextField writingHoursInput = new TextField();


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
        Button closeManagerSelectorButton = new Button("Close");

        EventHandler<ActionEvent> managerSelector = _ -> {
            VBox sessionManagersDisplay = createSessionManagersDisplay();
            Label noSessionManager = new Label("No session managers found.");
            System.out.println(sessionManagerList.size());
            if (sessionManager == null) {
                System.out.println("null");
                noSessionManager.setVisible(true);
                noSessionManager.setManaged(true);
            } else {
                noSessionManager.setVisible(false);
                noSessionManager.setManaged(false);
            }
            VBox vBox = new VBox(newSessionManagerButton, noSessionManager, sessionManagersDisplay,deleteSessionManagerButton, closeManagerSelectorButton);
            root.getChildren().setAll(vBox);

        };

        EventHandler<ActionEvent> newSessionManager = _ -> {
            Label newSessionManagerLabel = new Label("New Session Manager");

            languageBox.setValue(null);
            grindingHoursInput.setText("");
            readingHoursInput.setText("");
            writingHoursInput.setText("");
            speakingHoursInput.setText("");
            listeningHoursInput.setText("");
            newSessionManagerOutput.setText("");

            HBox hbox = new HBox(createButton, cancelButton);
            VBox vbox = new VBox(newSessionManagerLabel, languageBox, grindingHoursLabel, grindingHoursInput, speakingHoursLabel, speakingHoursInput,
                    listeningHoursLabel, listeningHoursInput, readingHoursLabel, readingHoursInput, writingHoursLabel, writingHoursInput, hbox, newSessionManagerOutput);
            root.getChildren().setAll(vbox);


        };

        EventHandler<ActionEvent> createSessionManager = _ -> {

            try {
                if (!(((Objects.equals(grindingHoursInput.getText(), "")) || (Objects.equals(speakingHoursInput.getText(), "")) || (Objects.equals(readingHoursInput.getText(), "")) || (Objects.equals(listeningHoursInput.getText(), "")) || (Objects.equals(writingHoursInput.getText(), ""))) || (languageBox.getSelectionModel().getSelectedItem() == null))) {

                    int grindingHours = Integer.parseInt(grindingHoursInput.getText());

                    int speakingHours = Integer.parseInt(speakingHoursInput.getText());

                    int listeningHours = Integer.parseInt(listeningHoursInput.getText());

                    int readingHours = Integer.parseInt(readingHoursInput.getText());

                    int writingHours = Integer.parseInt(writingHoursInput.getText());

                    System.out.println(languageBox.getSelectionModel().getSelectedItem());

                    sessionManagerList.add(new SessionManager(languageBox.getSelectionModel().getSelectedItem(), 0, 0, LocalDate.now(), grindingHours, speakingHours, readingHours, listeningHours, writingHours));
                    JsonStorage.save(sessionManagerList);
                    managerSelector.handle(new ActionEvent());
                } else {
                    StringBuilder stringBuilder = new StringBuilder();

                    if ((Objects.equals(grindingHoursInput.getText(), "")) || (Objects.equals(speakingHoursInput.getText(), "")) || (Objects.equals(readingHoursInput.getText(), "")) || (Objects.equals(listeningHoursInput.getText(), "")) || (Objects.equals(writingHoursInput.getText(), ""))) {
                        stringBuilder.append("Hours entry missing.\nEnter the minutes.\n\n");
                    }
                    if (languageBox.getSelectionModel().getSelectedItem() == null) {
                        stringBuilder.append("No language selected!\nSelect a language.\n\n");
                    }
                    newSessionManagerOutput.setText(stringBuilder.toString());
                }

            } catch (NumberFormatException ex) {
                newSessionManagerOutput.setText("Enter valid minutes.");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        };

        EventHandler<ActionEvent> deleteSessionManager = _ -> {
            System.out.println(sessionManagerList.size());
            if (sessionManager != null) {
                sessionManagerList.remove(sessionManager);
                try {
                    JsonStorage.save(sessionManagerList);
                    managerSelector.handle(new ActionEvent());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println(sessionManagerList.size());
        };


        EventHandler<ActionEvent> setCalendar = _ -> {
            pickedDate = d.getValue();
            if (pickedDate == null) {
                pickedDate = LocalDate.now();
            }
            sessionManager.makeDayNotNull(pickedDate);

            l.setText("Date : " + pickedDate);

            logDateLabel.setText("Logging session for " + pickedDate);
            newDateLabel.setText("New Date: " + pickedDate);


            if (!isEditing) {
                System.out.println();
                sessionDisplay = createSessionDisplay();
                sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);
                HBox newCalendarLayout = new HBox(calendar, sessionLayout);
                calendarTab.setContent(newCalendarLayout);
            }
            tabPane.requestLayout();
        };


        EventHandler<ActionEvent> closeSelector = _ -> {
            if (!sessionManagerList.isEmpty()) {
                setCalendar.handle(new ActionEvent());
                System.out.println(pickedDate);
                updateDashboard();
            }
        };


        EventHandler<ActionEvent> setLogger = _ -> {
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
        EventHandler<ActionEvent> cancelLogger = _ -> {
            isEditing = false;
            sessionDisplay = createSessionDisplay();
            sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);

            HBox hBox3 = new HBox(calendar, sessionLayout);
            calendarTab.setContent(hBox3);
            tabPane.requestLayout();
        };


        EventHandler<ActionEvent> logSession = _ -> {
            try {
                if (pickedDate == null) {
                    logOutput.setText("No date selected!\nSelect a date!");
                } else {
                    if (!(Objects.equals(minutesInput.getText(), "") || (activityBox.getSelectionModel().getSelectedItem() == null))) {
                        StringBuilder stringBuilder = new StringBuilder();
                        int minutes = Integer.parseInt(minutesInput.getText());
                        ActivityType activityType = activityBox.getSelectionModel().getSelectedItem();
                        int xp = sessionManager.logSession(minutes, activityType, pickedDate);
                        double variety = sessionManager.getVariety();
                        if (variety == 1.15) {
                            stringBuilder.append("You have a healthy balance of activities in the last 14 days. Well done! Balance XP multiplier: x1.25\n");
                        } else if (variety == 1) {
                            stringBuilder.append("You have a solid balance of activities in the last 14 days. Balance XP multiplier: x1.00\n");
                        } else if (variety == 0.875) {
                            stringBuilder.append("You have a weak balance of activities in the last 14 days. Balance XP multiplier: x0.875\n");
                        } else {
                            stringBuilder.append("Error\n");
                        }
                        for (ActivityCategory activityCategory : ActivityCategory.values()) {
                            stringBuilder.append(activityCategory.toString().substring(0, 1).toUpperCase()).append(activityCategory.toString().substring(1)).append(" XP gained: ").append(sessionManager.getXpJustCalculated(activityCategory)).append("\n");
                        }
                        stringBuilder.append("Total gained XP: ").append(xp);
                        logOutput.setText(stringBuilder.toString());
                        updateDashboard();
                        JsonStorage.save(sessionManagerList);
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
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };

        EventHandler<ActionEvent> setEditor = _ -> {
            isEditing = true;
            if (selectedSession != null) {
                activityBox.setValue(selectedSession.getActivityType());
                minutesInput.setText(String.valueOf(selectedSession.getMinutes()));
                logOutput.setText("");
                Label editDateLabel = new Label("Editing session " + calculateSessionNumberOfDay() + " on " + selectedSession.getDate());
                HBox editorButtons = new HBox(saveButton, closeButton);
                VBox editorLayout = new VBox(editDateLabel, newDateLabel, activityTypeLabel, activityBox, minutesLabel, minutesInput, logOutput, editorButtons);
                HBox hBox3 = new HBox(calendar, editorLayout);
                calendarTab.setContent(hBox3);
                tabPane.requestLayout();
            }

        };

        EventHandler<ActionEvent> saveSession = _ -> {
            try {
                if (pickedDate == null) {
                    logOutput.setText("No date selected!\nSelect a date!");
                } else {
                    if (!(Objects.equals(minutesInput.getText(), "") || (activityBox.getSelectionModel().getSelectedItem() == null))) {
                        int minutes = Integer.parseInt(minutesInput.getText());
                        ActivityType activityType = activityBox.getSelectionModel().getSelectedItem();
                        sessionManager.editSession(selectedSession, pickedDate, activityType, minutes);
                        logOutput.setText("Session updated successfully.");
                        updateDashboard();
                        JsonStorage.save(sessionManagerList);
                        cancelLogger.handle(new ActionEvent());
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
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };

        EventHandler<ActionEvent> deleteSession = e -> {
            System.out.println(sessionManager.getSessions().size());
            if (selectedSession != null) {
                sessionManager.deleteSession(selectedSession);
                setCalendar.handle(e);
                try {
                    updateDashboard();
                    JsonStorage.save(sessionManagerList);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println(sessionManager.getSessions().size());
        };


        progressTab.setOnSelectionChanged(_ -> {
            Label xpLabel = new Label("Overall XP to " + sessionManager.getNextLevel(sessionManager.getLevel()).toString() + ": " + sessionManager.getXp() + "/" + sessionManager.getCeiling());
            Label totalProgressLabel = new Label("Progress to " + sessionManager.getNextLevel(sessionManager.getLevel()) + ": " + sessionManager.getTotalProgress() + "%");
            Label levelLabel = new Label(sessionManager.getLevel().toString());
            ProgressBar totalProgress = new ProgressBar();
            totalProgress.setProgress(sessionManager.getTotalProgress() / 100);
            Label nextLevelLabel = new Label(sessionManager.getNextLevel(sessionManager.getLevel()).toString());
            Label totalMinutesLabel = new Label("Total minutes: " + minutesToHours(sessionManager.getTotalMinutes()));
            Label weekMinutesLabel = new Label("Week Minutes: " + minutesToHours(sessionManager.getWeekMinutes()));

            Button toggleBars = new Button("Toggle activity progress");
            Label retentionLabel = new Label("Retention score: " + sessionManager.getRetention());
            Label consistencyLabel = new Label("Consistency bonus: " + sessionManager.getConsistencyBonus());
            Label varietyLabel = new Label("Current variety score over last 14 days: " + sessionManager.getVariety());

            HBox categoryProgress = new HBox();

            for (ActivityCategory activityCategory : ActivityCategory.values()) {
                Label categoryXpLabel = new Label(activityCategory.toString().substring(0, 1).toUpperCase() + activityCategory.toString().substring(1) + " XP to " + sessionManager.getNextLevel(sessionManager.getLevel(activityCategory)).toString() + ": " + sessionManager.getXp(activityCategory) + "/" + sessionManager.getCeiling(activityCategory));
                Label categoryProgressLabel = new Label("Progress to " + sessionManager.getNextLevel(sessionManager.getLevel(activityCategory)) + ": " + sessionManager.getXpProgress(activityCategory) + "%");
                Label categorylevelLabel = new Label(sessionManager.getLevel(activityCategory).toString());
                ProgressBar categoryProgressBar = new ProgressBar();
                categoryProgressBar.setProgress(sessionManager.getXpProgress(activityCategory) / 100);
                Label categoryNextLevelLabel = new Label(sessionManager.getNextLevel(sessionManager.getLevel(activityCategory)).toString());
                HBox categoryProgressBarBox = new HBox(categorylevelLabel, categoryProgressBar, categoryNextLevelLabel);
                VBox categoryProgressBox = new VBox(categoryXpLabel, categoryProgressLabel, categoryProgressBarBox);

                categoryProgress.getChildren().add(categoryProgressBox);
            }

//            EventHandler<ActionEvent> toggleActivityProgress = e -> {
//
//            };

            ObservableList<Data> xpChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Reading XP", sessionManager.getXp(ActivityCategory.READING)),
                    new PieChart.Data("Writing XP", sessionManager.getXp(ActivityCategory.WRITING)),
                    new PieChart.Data("Grinding XP", sessionManager.getXp(ActivityCategory.GRINDING)),
                    new PieChart.Data("Speaking XP", sessionManager.getXp(ActivityCategory.SPEAKING)),
                    new PieChart.Data("Listening Xp", sessionManager.getXp(ActivityCategory.LISTENING))
            );

            PieChart xpPieChart = new PieChart(xpChartData);
            xpPieChart.setTitle("Study XP by Activity category");
            xpPieChart.setLegendVisible(false);
            xpPieChart.setLabelsVisible(true);

            ObservableList<Data> minutesChartData = FXCollections.observableArrayList();
            for (ActivityType activityType : ActivityType.values()) {
                minutesChartData.add(new PieChart.Data(activityType.toString() + " (" + minutesToHours(sessionManager.getTotalMinutes(activityType)) + ")", sessionManager.getTotalMinutes(activityType)));
            }
            PieChart minutesPieChart = new PieChart(minutesChartData);
            minutesPieChart.setTitle("Study Minutes by Activity type");
            minutesPieChart.setLegendVisible(false);
            minutesPieChart.setLabelsVisible(true);


            HBox progressBarBox = new HBox(levelLabel, totalProgress, nextLevelLabel);
            VBox progressBox = new VBox(xpLabel, totalProgressLabel, progressBarBox);
            HBox pieChartBox = new HBox(xpPieChart, minutesPieChart);
            VBox daddyBox = new VBox(totalMinutesLabel, weekMinutesLabel, varietyLabel, consistencyLabel, retentionLabel, progressBox, categoryProgress);

            if (sessionManager.getXp() > 0) {
                daddyBox.getChildren().add(pieChartBox);
            }
            progressTab.setContent(daddyBox);
            sessionManager.displayGeneralProgress(sessionManager.getLevel());

        });


        d.setOnAction(setCalendar);
        logMenuButton.setOnAction(setLogger);
        editButton.setOnAction(setEditor);
        logButton.setOnAction(logSession);
        closeButton.setOnAction(cancelLogger);
        saveButton.setOnAction(saveSession);
        deleteButton.setOnAction(deleteSession);
        newSessionManagerButton.setOnAction(newSessionManager);
        createButton.setOnAction(createSessionManager);
        cancelButton.setOnAction(managerSelector);
        deleteSessionManagerButton.setOnAction(deleteSessionManager);
        changeLanguageButton.setOnAction(managerSelector);
        closeManagerSelectorButton.setOnAction(closeSelector);


        managerSelector.handle(new ActionEvent());
        Scene scene = new Scene(root, 540, 460);
        stage.setScene(scene);
        stage.setTitle("Language Dash");
        stage.show();


    }

    public void updateDashboard() {

        Label languageLabel = new Label("Language: " + sessionManager.getLanguage());
        Label streakLabel = new Label();
        int streak = sessionManager.getActiveStreak();
        if (streak >= 0) {
            streakLabel.setText("Streak: " + streak);
        } else {
            streakLabel.setText("Streak: -" + sessionManager.getInactiveStreak());
        }
        HBox dashBoardBox = new HBox(changeLanguageButton, languageLabel, streakLabel, getLevelIndicator());
        root.getChildren().setAll(dashBoardBox, tabPane);
    }

    public VBox createSessionDisplay() {
        System.out.println("creating session display");
        sessionDisplay = new VBox();
        try {
            if (!Objects.requireNonNull(sessionManager).getSessionsByDate(pickedDate).isEmpty()) {
                List<Session> daySessions = sessionManager.getSessionsByDate(pickedDate);
                System.out.println(daySessions.size());
                int count = 0;
                for (Session session : daySessions) {
                    Label sessionLabel = new Label(session.toString());
                    sessionDisplay.getChildren().add(sessionLabel);
                    sessionLabel.setStyle(
                            "-fx-border-color: blue;"
                    );
                    if (count == 0) {
                        selectedSession = session;
                        selectedLabel = sessionLabel;
                        selectedLabel.setStyle(
                                "-fx-background-color: lightblue;" + "-fx-border-color: blue;"
                        );
                    }
                    count++;
                    sessionLabel.setOnMouseClicked(_ -> {
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
                    System.out.println(daySessions.size());
                }
            } else {
                selectedSession = null;
                selectedLabel = null;
                System.out.println("wait its not doing anything");
            }
        } catch (NullPointerException ex) {
            sessionDisplay = new VBox();
        }

        return sessionDisplay;
    }

    public VBox createSessionManagersDisplay() {
        VBox sessionManagersDisplay = new VBox();
        System.out.println(sessionManagerList.size());
        try {
            if (!Objects.requireNonNull(sessionManagerList).isEmpty()) {
                int count = 0;
                for (SessionManager sessionManager1 : sessionManagerList) {
                    Label sessionManagerLabel = new Label(sessionManager1.getLanguage().toString());
                    sessionManagersDisplay.getChildren().add(sessionManagerLabel);
                    sessionManagerLabel.setStyle(
                            "-fx-border-color: blue;"
                    );
                    if (count == 0) {
                        sessionManager = sessionManager1;
                        selectedMLabel = sessionManagerLabel;
                        selectedMLabel.setStyle(
                                "-fx-background-color: lightblue;" + "-fx-border-color: blue;"
                        );
                    }
                    count++;
                    sessionManagerLabel.setOnMouseClicked(_ -> {
                        if (selectedMLabel != null) {
                            selectedMLabel.setStyle(
                                    "-fx-background-color: white;" + "-fx-border-color: blue;");
                        }

                        selectedMLabel = sessionManagerLabel;
                        sessionManager = sessionManager1;
                        sessionManagerLabel.setStyle(
                                "-fx-background-color: lightblue;" + "-fx-border-color: blue;"
                        );

                    });
                    System.out.println(sessionManagerList.size());
                }
            } else {
                sessionManager = null;
                selectedMLabel = null;
                System.out.println("no session manager sonion");
            }
        } catch (NullPointerException ex) {
            sessionManagersDisplay = new VBox();
        }

        return sessionManagersDisplay;
    }

    public StackPane getLevelIndicator() {
        Circle background = new Circle(15);
        background.setFill(Color.LIGHTBLUE);
        background.setCenterX(25);
        background.setCenterY(25);

        Circle ring = new Circle(19);
        ring.setFill(null);
        ring.setStroke(Color.LIGHTGRAY);
        ring.setStrokeWidth(8);
        ring.setCenterX(25);
        ring.setCenterY(25);

        Arc progress = new Arc();
        progress.setRadiusX(19);
        progress.setRadiusY(19);
        progress.setStartAngle(90);
        progress.setLength(-360 * (sessionManager.getTotalProgress() / 100));
        progress.setStroke(Color.MEDIUMPURPLE);
        progress.setStrokeWidth(8);
        progress.setFill(null);
        progress.setType(ArcType.OPEN);
        progress.setCenterX(25);
        progress.setCenterY(25);
        progress.setManaged(false);

        Label label = new Label(sessionManager.getLevel().getSymbol());

        StackPane stack = new StackPane(ring, background, label);

        stack.getChildren().add(progress);
        StackPane.setAlignment(progress, Pos.CENTER);
        StackPane.setAlignment(label, Pos.CENTER);
        StackPane.setAlignment(progress, Pos.CENTER);
        StackPane.setAlignment(background, Pos.CENTER);

        stack.setStyle("-fx-border-color: #d3d3d3;");

        return stack;
    }

    public int calculateSessionNumberOfDay() {
        List<Session> daySessions = sessionManager.getSessionsByDate(pickedDate);
        int count = 0;
        for (Session session : daySessions) {
            if (session.equals(selectedSession)) {
                break;
            }
            count++;
        }
        return count + 1;
    }

    public String minutesToHours(int num) {
        int hours = num / 60;
        int minutes = num % 60;
        if (hours < 1) {
            return minutes + "m";

        }
        return hours + "h " + minutes + "m";
    }

    //BUG NOTES
    //FIND BEST BALANCE FOR XP, MAKE DELETION MORE ACCURATE
    //SESSIONS DONT APPEAR THE FIRST TIME YOU OPEN A SESSION MANAGER


}