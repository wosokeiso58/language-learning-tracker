package org.example;

import java.io.IOException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
    private final Button changeLanguageButton = new Button("CHANGE LANGUAGE");
    private Boolean isEditing = false;
    private final VBox root = new VBox();
    private TabPane tabPane;
    VBox sessionLayout = new VBox();


    @Override
    public void start(Stage stage) throws IOException {


        sessionManagerList = JsonStorage.load();
        root.setPadding(new Insets(20));
        this.tabPane = new TabPane();

        changeLanguageButton.setStyle(
                "-fx-background-color: rgba(21,26,34,0.44);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: white;" +
                "-fx-border-radius: 6px;" +
                "-fx-border-width: 1px;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.4), 6, 0, 0, 2);");

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
        createButton.getStyleClass().add("new-manager-button");

        Button deleteSessionManagerButton = new Button("Delete session manager");
        Button newSessionManagerButton = new Button("New Session Manager");

        ComboBox<Language> languageBox = new ComboBox<>();
        languageBox.setPromptText("Select a language");
        languageBox.getItems().addAll(Language.values());

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("new-manager-button");
        TextArea newSessionManagerOutput = new TextArea();

        Label grindingHoursLabel = new Label("🎮 Grinding Hours:");
        grindingHoursLabel.getStyleClass().add("new-manager-label");
        TextField grindingHoursInput = new TextField();

        Label speakingHoursLabel = new Label("🗣️ Speaking Hours:");
        speakingHoursLabel.getStyleClass().add("new-manager-label");
        TextField speakingHoursInput = new TextField();

        Label listeningHoursLabel = new Label("🎧 Listening Hours:");
        listeningHoursLabel.getStyleClass().add("new-manager-label");
        TextField listeningHoursInput = new TextField();

        Label readingHoursLabel = new Label("📖 Reading Hours:");
        readingHoursLabel.getStyleClass().add("new-manager-label");
        TextField readingHoursInput = new TextField();

        Label writingHoursLabel = new Label("✏️ Writing Hours:");
        writingHoursLabel.getStyleClass().add("new-manager-label");
        TextField writingHoursInput = new TextField();


        Button logMenuButton = new Button("Log session");
        Button editButton = new Button("Edit session");
        Button deleteButton = new Button("Delete session");
        sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);
        sessionLayout.setSpacing(10);
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
            if (sessionManager == null) {
                noSessionManager.setVisible(true);
                noSessionManager.setManaged(true);
            } else {
                noSessionManager.setVisible(false);
                noSessionManager.setManaged(false);
            }
            VBox vBox = new VBox(newSessionManagerButton, noSessionManager, sessionManagersDisplay,deleteSessionManagerButton, closeManagerSelectorButton);
            vBox.setSpacing(10);
            root.getChildren().setAll(vBox);

        };

        EventHandler<ActionEvent> newSessionManager = _ -> {
            Label newSessionManagerLabel = new Label("New Session Manager");
            Label languageLabel = new Label("🌐 Language:");
            newSessionManagerLabel.getStyleClass().add("new-manager-label");
            languageLabel.getStyleClass().add("new-manager-label");
            newSessionManagerLabel.setStyle("-fx-font-size: 40");

            languageBox.setValue(null);
            grindingHoursInput.setText("");
            readingHoursInput.setText("");
            writingHoursInput.setText("");
            speakingHoursInput.setText("");
            listeningHoursInput.setText("");
            newSessionManagerOutput.setText("");

            GridPane grid = new GridPane();
            grid.add(newSessionManagerLabel,0,0,5,1);
            grid.add(languageLabel,0,1,1,1);
            grid.add(languageBox,5,1,1,1);
            grid.add(grindingHoursLabel,0,3,1,1);
            grid.add(grindingHoursInput,5,3,1,1);
            grid.add(speakingHoursLabel,0,4,1,1);
            grid.add(speakingHoursInput,5,4,1,1);
            grid.add(listeningHoursLabel,0,5,1,1);
            grid.add(listeningHoursInput,5,5,1,1);
            grid.add(readingHoursLabel,0,6,1,1);
            grid.add(readingHoursInput,5,6,1,1);
            grid.add(writingHoursLabel,0,7,1,1);
            grid.add(writingHoursInput,5,7,1,1);
            grid.add(createButton, 2,8,1,1);
            grid.add(cancelButton, 4,8,1,1);
            grid.add(newSessionManagerOutput,1,9,4,1);


            grid.setHgap(50);
            grid.setVgap(20);
            grid.setPadding(new Insets(10));
            grid.setStyle("-fx-background-color: #384c67;");
            root.getChildren().setAll(grid);


        };

        EventHandler<ActionEvent> createSessionManager = _ -> {

            try {
                if (!(((Objects.equals(grindingHoursInput.getText(), "")) || (Objects.equals(speakingHoursInput.getText(), "")) || (Objects.equals(readingHoursInput.getText(), "")) || (Objects.equals(listeningHoursInput.getText(), "")) || (Objects.equals(writingHoursInput.getText(), ""))) || (languageBox.getSelectionModel().getSelectedItem() == null))) {

                    int grindingHours = Integer.parseInt(grindingHoursInput.getText());

                    int speakingHours = Integer.parseInt(speakingHoursInput.getText());

                    int listeningHours = Integer.parseInt(listeningHoursInput.getText());

                    int readingHours = Integer.parseInt(readingHoursInput.getText());

                    int writingHours = Integer.parseInt(writingHoursInput.getText());

                    sessionManagerList.add(new SessionManager(languageBox.getSelectionModel().getSelectedItem(), 0, 0, LocalDate.now().minusDays(1), grindingHours, speakingHours, readingHours, listeningHours, writingHours));
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
            if (sessionManager != null) {
                sessionManagerList.remove(sessionManager);
                try {
                    JsonStorage.save(sessionManagerList);
                    managerSelector.handle(new ActionEvent());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
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
                sessionDisplay = createSessionDisplay();
                sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);
                sessionLayout.setSpacing(10);
                HBox newCalendarLayout = new HBox(calendar, sessionLayout);
                newCalendarLayout.setSpacing(10);
                calendarTab.setContent(newCalendarLayout);
            }
            tabPane.requestLayout();
        };


        EventHandler<ActionEvent> closeSelector = _ -> {
            if (!sessionManagerList.isEmpty()) {
                setCalendar.handle(new ActionEvent());
                updateDashboard();
            }
        };


        EventHandler<ActionEvent> setLogger = _ -> {
            isEditing = true;
            logDateLabel.setText("Logging session for " + pickedDate);
            HBox loggerButtons = new HBox(logButton, closeButton);
            loggerButtons.setSpacing(15);
            VBox loggerLayout = new VBox(logDateLabel, activityTypeLabel, activityBox, minutesLabel, minutesInput, logOutput, loggerButtons);
            activityBox.setValue(null);
            minutesInput.setText("");
            logOutput.setText("");
            HBox hBox2 = new HBox(calendar, loggerLayout);
            hBox2.setSpacing(10);
            calendarTab.setContent(hBox2);
            tabPane.requestLayout();


        };
        EventHandler<ActionEvent> cancelLogger = _ -> {
            isEditing = false;
            sessionDisplay = createSessionDisplay();
            sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);
            sessionLayout.setSpacing(10);
            HBox hBox3 = new HBox(calendar, sessionLayout);
            hBox3.setSpacing(10);
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
                editorButtons.setSpacing(15);
                VBox editorLayout = new VBox(editDateLabel, newDateLabel, activityTypeLabel, activityBox, minutesLabel, minutesInput, logOutput, editorButtons);
                HBox hBox3 = new HBox(calendar, editorLayout);
                hBox3.setSpacing(10);
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
        };


        progressTab.setOnSelectionChanged(_ -> {
            Label xpLabel = new Label("Overall XP to " + sessionManager.getNextLevel(sessionManager.getLevel()).toString() + ": " + sessionManager.getXp() + "/" + sessionManager.getCeiling());
            Label totalProgressLabel = new Label("Total progress to " + sessionManager.getNextLevel(sessionManager.getLevel()) + ": " + sessionManager.getTotalProgress() + "%");
            Label levelLabel = new Label(sessionManager.getLevel().toString());
            ProgressBar totalProgress = new ProgressBar();
            totalProgress.setProgress(sessionManager.getTotalProgress() / 100);
            Label nextLevelLabel = new Label(sessionManager.getNextLevel(sessionManager.getLevel()).toString());
            Label totalMinutesLabel = new Label("Total time: " + minutesToHours(sessionManager.getTotalMinutes()));
            Label weekMinutesLabel = new Label("Week study time: " + minutesToHours(sessionManager.getWeekMinutes()));

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
                categoryProgressBox.setSpacing(5);
                categoryProgress.getChildren().add(categoryProgressBox);
            }

            categoryProgress.setSpacing(15);

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
            progressBarBox.setSpacing(10);
            VBox progressBox = new VBox(xpLabel, totalProgressLabel, progressBarBox);
            progressBox.setSpacing(10);
            HBox pieChartBox = new HBox(xpPieChart, minutesPieChart);
            pieChartBox.setSpacing(15);
            VBox daddyBox = new VBox(totalMinutesLabel, weekMinutesLabel, varietyLabel, consistencyLabel, retentionLabel, progressBox, categoryProgress);
            daddyBox.setSpacing(10);

            if (sessionManager.getXp() > 0) {
                daddyBox.getChildren().add(pieChartBox);
            }
            progressTab.setContent(daddyBox);
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
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );
        stage.setScene(scene);
        stage.setTitle("Language Dash");
        stage.show();


    }



    public void updateDashboard() {

        Label titleLabel = new Label("LANGUAGE TRACKER");
        titleLabel.setStyle("-fx-font-size: 20;"+"-fx-text-fill: white;" + "-fx-font-weight: bold;");

        Label languageIcon = new Label("🔤");
        languageIcon.setStyle("-fx-text-fill: #b68bdd;" + "-fx-font-size: 40px;" );
        languageIcon.setPrefWidth(40);
        languageIcon.setMinWidth(40);
        languageIcon.setMaxWidth(40);
        Label languageLabel = new Label("LANGUAGE");
        languageLabel.setStyle("-fx-text-fill: #d3d3d3;");
        Label language = new Label(sessionManager.getLanguage().toString());
        language.setStyle("-fx-text-fill: #b68bdd;" + "-fx-font-size: 23px;" );
        VBox languageBox1 = new VBox(languageLabel, language);
        languageBox1.setStyle("-fx-font-weight: bold;");
        HBox languageBox = new HBox(languageIcon, languageBox1);
        languageBox.setSpacing(15);

        Label dateIcon = new Label("📅");
        dateIcon.setStyle("-fx-text-fill: #54b8b4;" + "-fx-font-size: 40px;" );
        dateIcon.setPrefWidth(40);
        dateIcon.setMinWidth(40);
        dateIcon.setMaxWidth(40);
        Label dateLabel = new Label("TODAY'S DATE");
        dateLabel.setStyle("-fx-text-fill: #d3d3d3;");
        Label dateString = new Label(LocalDate.now().toString());
        dateString.setStyle("-fx-text-fill: #54b8b4;" + "-fx-font-size: 23px;" );
        VBox dateBox1 = new VBox(dateLabel, dateString);
        dateBox1.setStyle("-fx-font-weight: bold;");
        HBox dateBox = new HBox(dateIcon, dateBox1);
        dateBox.setSpacing(15);

        Label streakIcon = new Label("🔥");
        streakIcon.setStyle("-fx-text-fill: #ed8235;" + "-fx-font-size: 40px;" );
        streakIcon.setPrefWidth(40);
        streakIcon.setMinWidth(40);
        streakIcon.setMaxWidth(40);
        Label streakLabel = new Label();
        int streak = sessionManager.getActiveStreak();
        if (streak >= 0) {
            streakLabel.setText(String.valueOf(streak));
        } else {
            streakLabel.setText("Streak: -" + sessionManager.getInactiveStreak());
        }
        Label daysLabel = new Label("days");
        if(streak==1){
            daysLabel = new Label("day");
        }

        Label streakyLabel = new Label("STREAK");
        streakyLabel.setStyle("-fx-text-fill: #d3d3d3;");
        streakLabel.setStyle("-fx-text-fill: #ed8235;" + "-fx-font-size: 23px;" );

        daysLabel.setStyle("-fx-text-fill: #d3d3d3;");

        VBox streakBox1 = new VBox(streakyLabel, streakLabel, daysLabel);
        streakBox1.setStyle("-fx-font-weight: bold;");
        HBox streakBox = new HBox(streakIcon, streakBox1);
        streakBox.setSpacing(15);

        Label weekIcon = new Label("📖");
        weekIcon.setStyle("-fx-text-fill: #32a132;" + "-fx-font-size: 40px;" );
        weekIcon.setPrefWidth(40);
        weekIcon.setMinWidth(40);
        weekIcon.setMaxWidth(40);
        Label thisWeekLabel = new Label("THIS WEEK");
        thisWeekLabel.setStyle("-fx-text-fill: #d3d3d3;");
        Label weekMinutesLabel = new Label(minutesToHours(sessionManager.getWeekMinutes()));
        weekMinutesLabel.setStyle("-fx-text-fill: #32a132;" + "-fx-font-size: 23px;" );
        Label studyTimeLabel = new Label("study time");
        studyTimeLabel.setStyle("-fx-text-fill: #d3d3d3;");

        VBox weekStudyBox1 = new VBox(thisWeekLabel, weekMinutesLabel, studyTimeLabel);
        weekStudyBox1.setStyle("-fx-font-weight: bold;");
        HBox weekStudyBox = new HBox(weekIcon, weekStudyBox1);
        weekStudyBox.setSpacing(20);

        Label totalIcon = new Label("🎯");
        totalIcon.setStyle("-fx-text-fill: #6684af;" + "-fx-font-size: 40px;" );
        totalIcon.setPrefWidth(40);
        totalIcon.setMinWidth(40);
        totalIcon.setMaxWidth(40);
        Label totalLabel = new Label("TOTAL");
        totalLabel.setStyle("-fx-text-fill: #d3d3d3;");
        Label totalMinutesLabel = new Label(minutesToHours(sessionManager.getTotalMinutes()));
        totalMinutesLabel.setStyle("-fx-text-fill: #6684af;" + "-fx-font-size: 23px;" );
        Label allTimeLabel = new Label("all time");
        allTimeLabel.setStyle("-fx-text-fill: #d3d3d3;");
        VBox totalStudyBox1 = new VBox(totalLabel, totalMinutesLabel, allTimeLabel);
        totalStudyBox1.setStyle("-fx-font-weight: bold;");
        HBox totalStudyBox = new HBox(totalIcon, totalStudyBox1);
        totalStudyBox.setSpacing(15);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        HBox statsBox = new HBox(changeLanguageButton, languageBox, dateBox, streakBox, weekStudyBox, totalStudyBox);
        statsBox.setSpacing(50);
        VBox left = new VBox(titleLabel, statsBox);
        left.setSpacing(15);

        HBox dashBoardBox = new HBox(left,spacer, getLevelIndicator());
        dashBoardBox.setSpacing(30);
        dashBoardBox.setAlignment(Pos.CENTER_LEFT);
        dashBoardBox.setStyle("-fx-background-color: #151A22;" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 25;");

        dashBoardBox.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(dashBoardBox, Priority.ALWAYS);
        root.getChildren().setAll(dashBoardBox, tabPane);
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
                }
            } else {
                selectedSession = null;
                selectedLabel = null;
            }
        } catch (NullPointerException ex) {
            sessionDisplay = new VBox();
        }
        sessionDisplay.setSpacing(10);
        return sessionDisplay;
    }

    public VBox createSessionManagersDisplay() {
        VBox sessionManagersDisplay = new VBox();
        try {
            if (!Objects.requireNonNull(sessionManagerList).isEmpty()) {
                int count = 0;
                for (SessionManager sessionManager1 : sessionManagerList) {
                    Label sessionManagerLabel = new Label(sessionManager1.getLanguage().toString());
                    sessionManagersDisplay.getChildren().add(sessionManagerLabel);
                    sessionManagerLabel.setStyle(
                            "-fx-border-color: #00bbff;"
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
                }
            } else {
                sessionManager = null;
                selectedMLabel = null;
            }
        } catch (NullPointerException ex) {
            sessionManagersDisplay = new VBox();
        }
        sessionManagersDisplay.setSpacing(10);

        return sessionManagersDisplay;
    }

    public StackPane getLevelIndicator() {
        Circle background = new Circle(34);
        background.setFill(Paint.valueOf("#2a2c30"));
        background.setCenterX(25);
        background.setCenterY(25);

        Circle ring = new Circle(40);
        ring.setFill(null);
        ring.setStroke(Paint.valueOf("#2a2c30"));
        ring.setStrokeWidth(6);
        ring.setCenterX(25);
        ring.setCenterY(25);

        Arc progress = new Arc();
        progress.setRadiusX(40);
        progress.setRadiusY(40);
        progress.setStartAngle(90);
        progress.setLength(-360 * (sessionManager.getTotalProgress() / 100));
        progress.setStroke(Color.MEDIUMPURPLE);
        progress.setStrokeWidth(6);
        progress.setFill(null);
        progress.setType(ArcType.OPEN);
        progress.setCenterX(48);
        progress.setCenterY(57);
        progress.setManaged(false);

        Label symbolLabel = new Label(" " + sessionManager.getLevel().getSymbol());
        symbolLabel.setStyle("-fx-font-size: 17;"+"-fx-text-fill: white;" + "-fx-font-weight: bold;");
        Label percentageLabel = new Label(sessionManager.getTotalProgress() + "%");
        percentageLabel.setStyle("-fx-font-size: 12;"+"-fx-text-fill: lightgrey;" + "-fx-font-weight: bold;");
        percentageLabel.setPrefWidth(50);
        percentageLabel.setMinWidth(50);
        percentageLabel.setMaxWidth(50);
        VBox labelBox = new VBox(symbolLabel, percentageLabel);

        StackPane stack = new StackPane(ring, background, labelBox);

        stack.getChildren().add(progress);
        StackPane.setMargin(labelBox, new Insets(30, 15, 30, 30));

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


}