package org.example;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.util.Objects;


public class DashFX extends Application {

    private Session selectedSession;
    private SessionManager sessionManager;
    private LocalDate pickedDate = LocalDate.now();
    public List<SessionManager> sessionManagerList = JsonStorage.load();
    Label logDateLabel = new Label();

    private final BorderPane root = new BorderPane();
    MenuPage menuPage = new MenuPage();
    TabPage tabPage;
    NewLanguagePage newLanguagePage = new NewLanguagePage();


    public DashFX() throws IOException {
    }


    @Override
    public void start(Stage stage) {

        root.setPadding(new Insets(20));

        root.setStyle("-fx-background-color: #141414;" + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 12, 0, 0, 2);");

        root.setCenter(menuPage.getMenuRoot());
        Scene scene = new Scene(root, 540, 460);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );
        stage.setScene(scene);
        stage.setTitle("Language Dash");
        stage.show();


    }

    private void flashInvalid(Control control) {

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#FF6B6B"));
        shadow.setRadius(2);

        control.setEffect(shadow);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(shadow.radiusProperty(), 20)),

                new KeyFrame(Duration.millis(700),
                        new KeyValue(shadow.radiusProperty(), 0))
        );
        timeline.play();
    }



    public StackPane getLevelIndicator(SessionManager manager, boolean main) {
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
        progress.setLength(-360 * (manager.getTotalProgress() / 100));
        progress.setStroke(Paint.valueOf("#A57CFF"));
        progress.setStrokeWidth(6);
        progress.setFill(null);
        progress.setType(ArcType.OPEN);
        if (main) {
            progress.setCenterX(49);
            progress.setCenterY(52);
        } else {
            progress.setCenterX(48);
            progress.setCenterY(57);
        }
        progress.setManaged(false);

        Label symbolLabel = new Label(" " + manager.getLevel().getSymbol());
        symbolLabel.setStyle("-fx-font-size: 17;" + "-fx-text-fill: white;" + "-fx-font-weight: bold;");
        Label percentageLabel = new Label(manager.getTotalProgress() + "%");
        percentageLabel.setStyle("-fx-font-size: 12;" + "-fx-text-fill: lightgrey;" + "-fx-font-weight: bold;");
        percentageLabel.setPrefWidth(50);
        percentageLabel.setMinWidth(50);
        percentageLabel.setMaxWidth(50);
        VBox labelBox = new VBox(symbolLabel, percentageLabel);

        StackPane stack = new StackPane(ring, background, labelBox);

        stack.getChildren().add(progress);
        StackPane.setMargin(labelBox, new Insets(30, 15, 30, 30));

        return stack;
    }

    public String minutesToHours(int num) {
        int hours = num / 60;
        int minutes = num % 60;
        if (hours < 1) {
            return minutes + "m";

        }
        return hours + "h " + minutes + "m";
    }

    public class TabPage {

        private final VBox tabRoot;

        private final BorderPane sessionsTab = new BorderPane();
        ScrollPane sessionsScroll = new ScrollPane();
        private int lastXp = 0;
        private Label selectedLabel;
        VBox selectedDateLabel;
        YearMonth month = YearMonth.now();
        Label newDateLabel = new Label("New Date: " + pickedDate);
        TextArea logOutput = new TextArea();
        TextArea editOutput = new TextArea();
        ComboBox<ActivityType> logActivityBox = new ComboBox<>();
        TextField logMinutesInput = new TextField();
        ComboBox<ActivityType> editActivityBox = new ComboBox<>();
        TextField editMinutesInput = new TextField();
        Label l = new Label("Date : " + pickedDate);
        Label monthLabel = new Label("Month:" + month);
        Label noSessionsLabel = new Label("No sessions logged today yet.");
        int priorHours = sessionManager.getStartGrindingHours()+sessionManager.getStartListeningHours()+sessionManager.getStartListeningHours()+sessionManager.getStartSpeakingHours()+sessionManager.getStartWritingHours();
        private final VBox calendar = buildCalendar();
        private final VBox mainMenu = buildMainMenu();
        private final VBox logMenu = buildLogMenu();
        private final VBox editMenu = buildEditMenu();
        private final TabPane tabPane = buildTabPane();

        public TabPage() {

            tabRoot = new VBox(updateDashboard(), tabPane);
        }

        public TabPane buildTabPane() {


            TabPane tabPane = new TabPane();
            tabPane.getStyleClass().add("tab-pane");
            Tab calendarTab = new Tab("Calendar");
            Tab progressTab = new Tab("Progress");


            progressTab.setOnSelectionChanged(_ -> {
                if (sessionManager.getXp() != lastXp) {
                    Label xpLabel = new Label("Total XP to " + sessionManager.getNextLevel(sessionManager.getLevel()).toString() + ": " + sessionManager.getXp() + "/" + sessionManager.getCeiling());
                    xpLabel.getStyleClass().add("progress-label");
                    Label totalProgressLabel = new Label("Total progress to " + sessionManager.getNextLevel(sessionManager.getLevel()) + ": " + sessionManager.getTotalProgress() + "%");
                    totalProgressLabel.getStyleClass().add("progress-label");
                    Label levelLabel = new Label(sessionManager.getLevel().toString());
                    levelLabel.getStyleClass().add("progress-label");
                    ProgressBar totalProgress = new ProgressBar();
                    totalProgress.getStyleClass().add("xp-bar");
                    totalProgress.setProgress(sessionManager.getTotalProgress() / 100);
                    Label nextLevelLabel = new Label(sessionManager.getNextLevel(sessionManager.getLevel()).toString());
                    nextLevelLabel.getStyleClass().add("progress-label");
                    Label totalMinutesLabel = new Label("Total study time: " + minutesToHours(sessionManager.getTotalMinutes()+(priorHours*60)));
                    totalMinutesLabel.getStyleClass().add("progress-label");
                    Label priorMinutesLabel = new Label("Prior study time (unlogged): " + priorHours +"h");
                    priorMinutesLabel.getStyleClass().add("progress-label");
                    Label loggedMinutesLabel = new Label("Logged study time: " + minutesToHours(sessionManager.getTotalMinutes()));
                    loggedMinutesLabel.getStyleClass().add("progress-label");
                    Label weekMinutesLabel = new Label("Week study time: " + minutesToHours(sessionManager.getWeekMinutes()));
                    weekMinutesLabel.getStyleClass().add("progress-label");
                    Label retentionLabel = new Label("Retention score: " + sessionManager.getRetention());
                    retentionLabel.getStyleClass().add("progress-label");
                    Label consistencyLabel = new Label("Consistency bonus: " + sessionManager.getConsistencyBonus());
                    consistencyLabel.getStyleClass().add("progress-label");
                    Label varietyLabel = new Label("Current variety score over last 14 days: " + sessionManager.getVariety());
                    varietyLabel.getStyleClass().add("progress-label");
                    HBox categoryProgress = new HBox();

                    for (ActivityCategory activityCategory : ActivityCategory.values()) {
                        Label categoryXpLabel = new Label(activityCategory.toString().substring(0, 1).toUpperCase() + activityCategory.toString().substring(1) + " XP to " + sessionManager.getNextLevel(sessionManager.getLevel(activityCategory)).toString() + ": " + sessionManager.getXp(activityCategory) + "/" + sessionManager.getCeiling(activityCategory));
                        categoryXpLabel.getStyleClass().add("progress-label");
                        Label categoryProgressLabel = new Label("Progress to " + sessionManager.getNextLevel(sessionManager.getLevel(activityCategory)) + ": " + sessionManager.getXpProgress(activityCategory) + "%");
                        categoryProgressLabel.getStyleClass().add("progress-label");
                        Label categorylevelLabel = new Label(sessionManager.getLevel(activityCategory).toString());
                        categorylevelLabel.getStyleClass().add("progress-label");
                        ProgressBar categoryProgressBar = new ProgressBar();
                        categoryProgressBar.getStyleClass().add(activityCategory.getProgressBarStyle());
                        categoryProgressBar.setProgress(sessionManager.getXpProgress(activityCategory) / 100);
                        Label categoryNextLevelLabel = new Label(sessionManager.getNextLevel(sessionManager.getLevel(activityCategory)).toString());
                        categoryNextLevelLabel.getStyleClass().add("progress-label");
                        HBox categoryProgressBarBox = new HBox(categorylevelLabel, categoryProgressBar, categoryNextLevelLabel);
                        categoryProgressBarBox.setSpacing(5);
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

                    HBox progressRow = new HBox(10);
                    progressRow.setAlignment(Pos.CENTER);
                    progressRow.setMinHeight(32);

                    totalProgress.setMaxWidth(Double.MAX_VALUE);

                    HBox.setHgrow(totalProgress, Priority.ALWAYS);

                    progressRow.getChildren().addAll(levelLabel, totalProgress, nextLevelLabel);

                    Label currentLevelLabel = new Label("Current Level: "+sessionManager.getLevel());
                    currentLevelLabel.getStyleClass().add("progress-label");
                    currentLevelLabel.setStyle("-fx-font-size: 32");
                    xpLabel.setStyle("-fx-font-size: 24");
                    totalProgressLabel.setStyle("-fx-font-size: 24");
                    VBox progressBox = new VBox(currentLevelLabel, progressRow, xpLabel, totalProgressLabel);
                    progressBox.setAlignment(Pos.CENTER);
                    progressBox.setSpacing(10);

                    HBox pieChartBox = new HBox(xpPieChart, minutesPieChart);
                    pieChartBox.setSpacing(15);
                    VBox daddyBox = new VBox(progressBox, totalMinutesLabel,loggedMinutesLabel,priorMinutesLabel, weekMinutesLabel, varietyLabel, consistencyLabel, retentionLabel, categoryProgress);
                    daddyBox.setSpacing(10);

                    if (sessionManager.getXp() > 0) {
                        daddyBox.getChildren().add(pieChartBox);
                    }
                    progressTab.setContent(daddyBox);
                }

                lastXp = sessionManager.getXp();
            });

            calendarTab.setContent(new HBox(calendar, sessionsTab));
            tabPane.getTabs().addAll(calendarTab, progressTab);
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            showMainMenu();

            return tabPane;
        }


        public ScrollPane createSessionDisplay() {
            VBox vBox = new VBox();
            ScrollPane scrollPane;
            try {
                if (!Objects.requireNonNull(sessionManager).getSessionsByDate(pickedDate).isEmpty()) {
                    List<Session> daySessions = sessionManager.getSessionsByDate(pickedDate);
                    int count = 0;
                    for (Session session : daySessions) {
                        Label sessionLabel = new Label(session.toString());
                        sessionLabel.getStyleClass().clear();
                        sessionLabel.getStyleClass().add("session-label");
                        vBox.getChildren().add(sessionLabel);
                        if (count == 0) {
                            selectedSession = session;
                            selectedLabel = sessionLabel;
                            sessionLabel.getStyleClass().clear();
                            sessionLabel.getStyleClass().add("selected-session-label");
                        }
                        count++;
                        sessionLabel.setOnMouseClicked(_ -> {
                            if (selectedLabel != null) {
                                selectedLabel.getStyleClass().clear();
                                selectedLabel.getStyleClass().add("session-label");

                            }

                            selectedLabel = sessionLabel;
                            selectedSession = session;
                            sessionLabel.getStyleClass().clear();
                            sessionLabel.getStyleClass().add("selected-session-label");
                        });
                    }
                } else {
                    selectedSession = null;
                    selectedLabel = null;
                }
            } catch (NullPointerException _) {
            }
            vBox.setSpacing(10);
            scrollPane = new ScrollPane(vBox);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(550);
            scrollPane.getStyleClass().add("sessions-scroll");
            return scrollPane;
        }

        public HBox updateDashboard() {

            Label titleLabel = new Label("LANGUAGE TRACKER");
            titleLabel.setStyle("-fx-font-size: 20;" + "-fx-text-fill: white;" + "-fx-font-weight: bold;");

            Label languageIcon = new Label("🔤");
            languageIcon.setStyle("-fx-text-fill: #b68bdd;" + "-fx-font-size: 40px;");
            languageIcon.setPrefWidth(40);
            languageIcon.setMinWidth(40);
            languageIcon.setMaxWidth(40);
            Label languageLabel = new Label("LANGUAGE");
            languageLabel.setStyle("-fx-text-fill: #d3d3d3;");
            Label language = new Label(sessionManager.getLanguage().toString());
            language.setStyle("-fx-text-fill: #b68bdd;" + "-fx-font-size: 23px;");
            VBox languageBox1 = new VBox(languageLabel, language);
            languageBox1.setStyle("-fx-font-weight: bold;");
            HBox languageBox = new HBox(languageIcon, languageBox1);
            languageBox.setSpacing(15);

            Label dateIcon = new Label("📅");
            dateIcon.setStyle("-fx-text-fill: #54b8b4;" + "-fx-font-size: 40px;");
            dateIcon.setPrefWidth(40);
            dateIcon.setMinWidth(40);
            dateIcon.setMaxWidth(40);
            Label dateLabel = new Label("TODAY'S DATE");
            dateLabel.setStyle("-fx-text-fill: #d3d3d3;");
            Label dateString = new Label(LocalDate.now().toString());
            dateString.setStyle("-fx-text-fill: #54b8b4;" + "-fx-font-size: 23px;");
            VBox dateBox1 = new VBox(dateLabel, dateString);
            dateBox1.setStyle("-fx-font-weight: bold;");
            HBox dateBox = new HBox(dateIcon, dateBox1);
            dateBox.setSpacing(15);

            Label streakIcon = new Label("🔥");
            streakIcon.setStyle("-fx-text-fill: #ed8235;" + "-fx-font-size: 40px;");
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
            if (streak == 1) {
                daysLabel = new Label("day");
            }

            Label streakyLabel = new Label("STREAK");
            streakyLabel.setStyle("-fx-text-fill: #d3d3d3;");
            streakLabel.setStyle("-fx-text-fill: #ed8235;" + "-fx-font-size: 23px;");

            daysLabel.setStyle("-fx-text-fill: #d3d3d3;");

            VBox streakBox1 = new VBox(streakyLabel, streakLabel, daysLabel);
            streakBox1.setStyle("-fx-font-weight: bold;");
            HBox streakBox = new HBox(streakIcon, streakBox1);
            streakBox.setSpacing(15);

            Label weekIcon = new Label("📖");
            weekIcon.setStyle("-fx-text-fill: #32a132;" + "-fx-font-size: 40px;");
            weekIcon.setPrefWidth(40);
            weekIcon.setMinWidth(40);
            weekIcon.setMaxWidth(40);
            Label thisWeekLabel = new Label("THIS WEEK");
            thisWeekLabel.setStyle("-fx-text-fill: #d3d3d3;");
            Label weekMinutesLabel = new Label(minutesToHours(sessionManager.getWeekMinutes()));
            weekMinutesLabel.setStyle("-fx-text-fill: #32a132;" + "-fx-font-size: 23px;");
            Label studyTimeLabel = new Label("study time");
            studyTimeLabel.setStyle("-fx-text-fill: #d3d3d3;");

            VBox weekStudyBox1 = new VBox(thisWeekLabel, weekMinutesLabel, studyTimeLabel);
            weekStudyBox1.setStyle("-fx-font-weight: bold;");
            HBox weekStudyBox = new HBox(weekIcon, weekStudyBox1);
            weekStudyBox.setSpacing(20);

            Label totalIcon = new Label("🎯");
            totalIcon.setStyle("-fx-text-fill: #6684af;" + "-fx-font-size: 40px;");
            totalIcon.setPrefWidth(40);
            totalIcon.setMinWidth(40);
            totalIcon.setMaxWidth(40);
            Label totalLabel = new Label("TOTAL");
            totalLabel.setStyle("-fx-text-fill: #d3d3d3;");
            Label totalMinutesLabel = new Label(minutesToHours(sessionManager.getTotalMinutes()));
            totalMinutesLabel.setStyle("-fx-text-fill: #6684af;" + "-fx-font-size: 23px;");
            Label allTimeLabel = new Label("all time");
            allTimeLabel.setStyle("-fx-text-fill: #d3d3d3;");
            VBox totalStudyBox1 = new VBox(totalLabel, totalMinutesLabel, allTimeLabel);
            totalStudyBox1.setStyle("-fx-font-weight: bold;");
            HBox totalStudyBox = new HBox(totalIcon, totalStudyBox1);
            totalStudyBox.setSpacing(15);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button changeLanguageButton = new Button("Change language");
            changeLanguageButton.getStyleClass().add("manager-card");

            changeLanguageButton.setOnMouseClicked(_ -> root.setCenter(menuPage.getMenuRoot()));

            HBox statsBox = new HBox(changeLanguageButton, languageBox, dateBox, streakBox, weekStudyBox, totalStudyBox);
            statsBox.setSpacing(50);
            VBox left = new VBox(titleLabel, statsBox);
            left.setSpacing(15);

            HBox dashBoardBox = new HBox(left, spacer, getLevelIndicator(sessionManager, false));
            dashBoardBox.setSpacing(30);
            dashBoardBox.setAlignment(Pos.CENTER_LEFT);
            dashBoardBox.setStyle("-fx-background-color: #151A22;" +
                    "-fx-background-radius: 15;" +
                    "-fx-padding: 25;");

            dashBoardBox.setMaxWidth(Double.MAX_VALUE);
            return dashBoardBox;
        }

        public VBox buildMainMenu() {

            Button logMenuButton = new Button("Log session");
            logMenuButton.getStyleClass().add("calendar-menu-button");
            Button editButton = new Button("Edit session");
            editButton.getStyleClass().add("calendar-menu-button");
            FontIcon deleteButton = new FontIcon("fas-trash");
            deleteButton.setIconSize(30);
            deleteButton.setIconColor(Color.GREY);
            Label sessionsLabel = new Label("Sessions:");
            sessionsLabel.setPrefWidth(400);
            sessionsLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 30;");
            noSessionsLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 20;"
            +"-fx-border-color: white;" + "-fx-border-width: 1;" + "-fx-padding: 10;");


            logMenuButton.setOnMouseClicked(_ -> showLogMenu());


            editButton.setOnMouseClicked(_ -> showEditMenu());


            deleteButton.setOnMouseClicked(_ -> {
                if (selectedSession != null) {
                    sessionManager.deleteSession(selectedSession);
                    try {
                        updateDashboard();
                        showMainMenu();
                        tabSave();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            VBox buttonsBox = new VBox(10, logMenuButton, editButton);

            noSessionsLabel.setManaged(false);
            noSessionsLabel.setVisible(false);
            HBox hbox = new HBox(sessionsLabel, deleteButton);
            hbox.setSpacing(500);
            VBox.setVgrow(sessionsScroll, Priority.ALWAYS);
            VBox.setMargin(buttonsBox, new Insets(75, 0, 0, 0));
            VBox.setMargin(noSessionsLabel, new Insets(0, 0, 250, 0));

            VBox sessionLayout = new VBox(hbox, sessionsScroll, noSessionsLabel ,buttonsBox);
            if (!Objects.requireNonNull(sessionManager).getSessionsByDate(pickedDate).isEmpty()){
                noSessionsLabel.setVisible(true);
                noSessionsLabel.setManaged(true);
            }
            sessionLayout.setSpacing(10);
            return sessionLayout;
        }

        public void refreshMainMenu() {
            noSessionsLabel.setVisible(true);
            noSessionsLabel.setManaged(true);
            mainMenu.getChildren().remove(1);
            sessionsScroll = createSessionDisplay();
            mainMenu.getChildren().add(1, sessionsScroll);
            sessionsScroll.setManaged(false);
            sessionsScroll.setVisible(false);
            if(Objects.equals(pickedDate, LocalDate.now())){
                noSessionsLabel.setText("No sessions logged today yet.");
            }
            else{
                noSessionsLabel.setText("Nothing recorded for this day yet.");
            }
            if (!Objects.requireNonNull(sessionManager).getSessionsByDate(pickedDate).isEmpty()){

                noSessionsLabel.setManaged(false);
                noSessionsLabel.setVisible(false);
                sessionsScroll.setManaged(true);
                sessionsScroll.setVisible(true);
            }
        }

        public VBox buildLogMenu() {
            Button closeButton = new Button("Close");
            closeButton.getStyleClass().add("close-button");
            Label minutesLabel = new Label("Minutes:");
            minutesLabel.setStyle("-fx-text-fill: #fadaff;" + "-fx-font-size: 25;");
            logMinutesInput.setPromptText("Enter session minutes:");
            logActivityBox.setPromptText("Select an activity");
            logActivityBox.setPromptText("Select an activity");
            logActivityBox.getItems().addAll(ActivityType.values());
            logActivityBox.getStyleClass().add("default-box");
            Label activityTypeLabel = new Label("Activity Type:");
            activityTypeLabel.setStyle("-fx-text-fill: #fadaff;" + "-fx-font-size: 25;");
            Button logButton = new Button("Log");
            logButton.getStyleClass().add("log-button");
            logOutput.getStyleClass().add("default-box");


            logOutput.setEditable(false);

            logDateLabel.setText("Logging session for " + pickedDate);
            logActivityBox.setValue(null);
            logMinutesInput.setText("");
            logMinutesInput.getStyleClass().add("default-box");
            logOutput.setText("");
            logOutput.setEditable(false);


            closeButton.setOnMouseClicked(_ -> showMainMenu());


            logButton.setOnMouseClicked(_ -> {
                try {
                    if (pickedDate == null) {
                        logOutput.setText("No date selected!\nSelect a date!");
                    } else {
                        if (!(Objects.equals(logMinutesInput.getText(), "") || (logActivityBox.getSelectionModel().getSelectedItem() == null))) {
                            StringBuilder stringBuilder = new StringBuilder();
                            int minutes = Integer.parseInt(logMinutesInput.getText());
                            ActivityType activityType = logActivityBox.getSelectionModel().getSelectedItem();
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
                            tabSave();
                            showMainMenu();
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();

                            if (Objects.equals(logMinutesInput.getText(), "")) {
                                flashInvalid(logMinutesInput);
                                stringBuilder.append("No minutes entered!\nEnter the minutes!\n\n");
                            }
                            if (logActivityBox.getSelectionModel().getSelectedItem() == null) {
                                stringBuilder.append("No activity selected!\nSelect an activity!");
                                flashInvalid(logActivityBox);
                            }
                            logOutput.setText(stringBuilder.toString());
                        }
                    }
                } catch (NumberFormatException ex) {
                    logOutput.setText("Enter valid minutes.");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });


            logButton.setPrefSize(100,50);
            closeButton.setPrefSize(100,50);

            HBox loggerButtons = new HBox(logButton, closeButton);
            loggerButtons.setAlignment(Pos.CENTER);
            loggerButtons.setSpacing(25);
            VBox.setMargin(loggerButtons, new Insets(125));

            VBox vBox = new VBox(logDateLabel, activityTypeLabel, logActivityBox, minutesLabel, logMinutesInput, loggerButtons);
            vBox.setSpacing(15);
            return vBox;
        }

        public void refreshLogMenu() {
            logDateLabel.setText("Logging session for " + pickedDate);
            logActivityBox.getSelectionModel().clearSelection();
            logActivityBox.setValue(null);
            logActivityBox.setPromptText("Select an activity");
            logMinutesInput.setText("");
            logOutput.setText("");


        }

        public VBox buildEditMenu() {

            Button closeButton = new Button("Close");
            Label activityTypeLabel = new Label("Activity Type:");
            activityTypeLabel.setStyle("-fx-text-fill: #fadaff;" + "-fx-font-size: 25;");
            Label minutesLabel = new Label("Minutes:");
            minutesLabel.setStyle("-fx-text-fill: #fadaff;" + "-fx-font-size: 25;");
            newDateLabel.setStyle("-fx-text-fill: #fadaff;" + "-fx-font-size: 25;");
            editActivityBox.getItems().addAll(ActivityType.values());
            editActivityBox.getStyleClass().add("default-box");

            editOutput.setEditable(false);
            Button saveButton = new Button("Save");


            closeButton.setOnMouseClicked(_ -> showMainMenu());


            saveButton.setOnMouseClicked(_ -> {
                try {
                    if (pickedDate == null) {
                        editOutput.setText("No date selected!\nSelect a date!");
                    } else {
                        if (!(Objects.equals(editMinutesInput.getText(), "") || (editActivityBox.getSelectionModel().getSelectedItem() == null))) {
                            int minutes = Integer.parseInt(editMinutesInput.getText());
                            ActivityType activityType = editActivityBox.getSelectionModel().getSelectedItem();
                            sessionManager.editSession(selectedSession, pickedDate, activityType, minutes);
                            editOutput.setText("Session updated successfully.");
                            updateDashboard();
                            tabSave();
                            showMainMenu();
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();

                            if (Objects.equals(editMinutesInput.getText(), "")) {
                                stringBuilder.append("No minutes entered!\nEnter the minutes!\n\n");
                                flashInvalid(editMinutesInput);
                            }
                            if (editActivityBox.getSelectionModel().getSelectedItem() == null) {
                                stringBuilder.append("No activity selected!\nSelect an activity!");
                            }
                            editOutput.setText(stringBuilder.toString());
                        }
                    }
                } catch (NumberFormatException ex) {
                    editOutput.setText("Enter valid minutes.");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            });

            editMinutesInput.getStyleClass().add("default-box");
            editActivityBox.getStyleClass().add("default-box");
            saveButton.getStyleClass().add("log-button");
            closeButton.getStyleClass().add("close-button");

            HBox editorButtons = new HBox(saveButton, closeButton);
            editorButtons.setSpacing(25);
            VBox.setMargin(editorButtons, new Insets(125));


            VBox vBox = new VBox(newDateLabel, activityTypeLabel, editActivityBox, minutesLabel, editMinutesInput, editorButtons);
            vBox.setSpacing(15);
            return vBox;

        }

        public void refreshEditMenu() {
            editOutput.setText("");

            if (selectedSession != null) {
                editActivityBox.setValue(selectedSession.getActivityType());
                editMinutesInput.setText(String.valueOf(selectedSession.getMinutes()));
                editOutput.setText("");
            } else {
                editActivityBox.getSelectionModel().clearSelection();
                logActivityBox.setValue(null);
                editMinutesInput.setText("");
            }
        }

        public VBox buildCalendar() {

            l.setStyle("-fx-text-fill: white;" + "-fx-font-size: 30;");

            month = YearMonth.now();

            monthLabel.setPrefWidth(131);
            monthLabel.setMinWidth(131);
            monthLabel.setMaxWidth(131);
            monthLabel.setStyle("-fx-text-fill: #E5E5E5;" + "-fx-font-size: 18;");

            FontIcon rightLabel = new FontIcon("fas-arrow-right");
            rightLabel.setIconSize(22);
            rightLabel.setIconColor(Paint.valueOf("#A57CFF"));
            FontIcon leftLabel = new FontIcon("fas-arrow-left");
            leftLabel.setIconSize(22);
            leftLabel.setIconColor(Paint.valueOf("#A57CFF"));

            HBox hbox = new HBox(leftLabel, monthLabel, rightLabel);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(15);

            rightLabel.setOnMouseClicked(_ -> {
                month = month.plusMonths(1);
                updateCalendar();
            });

            leftLabel.setOnMouseClicked(_ -> {
                month = month.minusMonths(1);
                updateCalendar();
            });


            GridPane calendarPane = buildCalendarMonth();

            return new VBox(hbox, calendarPane, l);
        }

        public GridPane buildCalendarMonth() {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(25, 25, 25, 25));


            Label mondayLabel = new Label("mon");
            mondayLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 20;");
            Label tuesdayLabel = new Label("tue");
            tuesdayLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 20;");
            Label wednesdayLabel = new Label("wed");
            wednesdayLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 20;");
            Label thursdayLabel = new Label("thu");
            thursdayLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 20;");
            Label fridayLabel = new Label("fri");
            fridayLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 20;");
            Label saturdayLabel = new Label("sat");
            saturdayLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 20;");
            Label sundayLabel = new Label("sun");
            sundayLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 20;");

            ArrayList<Label> dayLabels = new ArrayList<>();
            dayLabels.add(mondayLabel);
            dayLabels.add(tuesdayLabel);
            dayLabels.add(wednesdayLabel);
            dayLabels.add(thursdayLabel);
            dayLabels.add(fridayLabel);
            dayLabels.add(saturdayLabel);
            dayLabels.add(sundayLabel);

            for (int k = 0; k <= 6; k++) {
                gridPane.add(dayLabels.get(k), k, 0);
            }


            int days = month.lengthOfMonth();
            LocalDate first = month.atDay(1);
            DayOfWeek day = first.getDayOfWeek();
            int column = day.getValue() - 2;
            int row = 1;

            for (int i = 1; i <= days; i++) {
                column++;
                if (column == 7) {
                    column = 0;
                    row++;
                }
                int dayMinutes = sessionManager.getTotalMinutes(month.atDay(i));
                Label dayLabel = new Label(""+i);
                Label dayMinutesLabel = new Label(minutesToHours(dayMinutes));

                if(dayMinutes>0){
                    if(dayMinutes>29){
                        if(dayMinutes>59){
                            dayLabel.setStyle("-fx-text-fill: lightgreen;");
                            dayMinutesLabel.setStyle("-fx-text-fill: lightgreen;");
                        }
                        else{
                            dayLabel.setStyle("-fx-text-fill: yellow;");
                            dayMinutesLabel.setStyle("-fx-text-fill: yellow;");
                        }
                    }
                    else{
                        dayLabel.setStyle("-fx-text-fill: orange;");
                        dayMinutesLabel.setStyle("-fx-text-fill: orange;");
                    }
                }
                else{
                    dayLabel.setStyle("-fx-text-fill: lightgrey;");
                    dayMinutesLabel.setStyle("-fx-text-fill: lightgrey;");
                }
                VBox label = new VBox (dayLabel, dayMinutesLabel);
                label.getStyleClass().add("day-label");
                label.setAlignment(Pos.CENTER);



                label.setPrefSize(75,75);

                if(Objects.equals(pickedDate, month.atDay(i))){
                    label.getStyleClass().add("selected-day-label");
                    selectedDateLabel = label;
                }

                int finalI = i;
                label.setOnMouseClicked(_ -> {
                    if(selectedDateLabel!=label){
                        selectedDateLabel.getStyleClass().clear();
                        selectedDateLabel.getStyleClass().add("day-label");
                    selectedDateLabel = label;
                    selectedDateLabel.getStyleClass().clear();
                    selectedDateLabel.getStyleClass().add("selected-day-label");

                    pickedDate = month.atDay(finalI);
                    sessionManager.makeDayNotNull(pickedDate);

                    if(sessionsTab.getCenter()==mainMenu){
                        refreshMainMenu();
                    }

                    l.setText("Date : " + pickedDate);
                    logDateLabel.setText("Logging session for " + pickedDate);
                    newDateLabel.setText("New Date: " + pickedDate);

                    }
                });

                gridPane.add(label, column, row);
            }

            return gridPane;


        }

        public void updateCalendar() {
            monthLabel.setText("Month: " + month.toString());

            calendar.getChildren().remove(1);
            calendar.getChildren().add(1, buildCalendarMonth());
        }


        public VBox getTabRoot() {
            tabRoot.getChildren().removeFirst();
            tabRoot.getChildren().addFirst(updateDashboard());
            return tabRoot;
        }

        public void showMainMenu() {
            updateCalendar();
            refreshMainMenu();
            sessionsTab.setCenter(mainMenu);
            if (tabPane != null) {
                tabPane.requestLayout();
            }
        }

        public void showLogMenu() {
            refreshLogMenu();
            sessionsTab.setCenter(logMenu);
            if (tabPane != null) {
                tabPane.requestLayout();
            }
        }

        public void showEditMenu() {
            if (selectedSession != null) {
                refreshEditMenu();
                sessionsTab.setCenter(editMenu);
                if (tabPane != null) {
                    tabPane.requestLayout();
                }
            }

        }

        public void tabSave() throws IOException {
            JsonStorage.save(sessionManagerList);
            tabRoot.getChildren().removeFirst();
            tabRoot.getChildren().addFirst(updateDashboard());
        }
    }


    public class MenuPage {

        VBox menuRoot;
        TilePane sessionManagersDisplay;
        private VBox selectedMLabel;
        Label yapLabel = new Label("What did you work on today?");
        Label welcomeLabel = new Label("Welcome back.");
        Random random = new Random();
        int num;

        public MenuPage() {

            Button deleteSessionManagerButton = new Button("Delete");
            deleteSessionManagerButton.getStyleClass().add("main-menu-button");
            Button newSessionManagerButton = new Button("+ Add Language");
            newSessionManagerButton.getStyleClass().add("main-menu-button");

            newSessionManagerButton.setOnMouseClicked(_ -> root.setCenter(newLanguagePage.getNewLanguageRoot()));

            deleteSessionManagerButton.setOnMouseClicked(_ -> {
                if (sessionManager != null) {
                    sessionManagerList.remove(sessionManager);
                    try {
                        JsonStorage.save(sessionManagerList);
                        root.setCenter(menuPage.getMenuRoot());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            sessionManagersDisplay = createSessionManagersDisplay();
            welcomeLabel = new Label("Welcome back.");
            welcomeLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 30;");
            yapLabel.setStyle("-fx-text-fill: white;" + "-fx-font-size: 20;");

            HBox hBox = new HBox(newSessionManagerButton, deleteSessionManagerButton);
            hBox.setSpacing(30);
            hBox.setAlignment(Pos.CENTER);
            menuRoot = new VBox(welcomeLabel, yapLabel, sessionManagersDisplay, hBox);
            menuRoot.setSpacing(10);
            menuRoot.setAlignment(Pos.CENTER);
            menuRoot.setStyle("-fx-padding: 10;");

        }

        public void updateMainMenu() {


            if (sessionManagersDisplay != null) {
                menuRoot.getChildren().remove(sessionManagersDisplay);
            }
            sessionManagersDisplay = createSessionManagersDisplay();
            menuRoot.getChildren().add(2, sessionManagersDisplay);

            if (sessionManagerList.isEmpty()) {
                welcomeLabel.setText("Welcome.");
                yapLabel.setText("Add a language to start.");
            } else {
                welcomeLabel.setText("Welcome back.");
                num = random.nextInt(4);
                switch (num) {
                    case 0 -> yapLabel.setText("Which language did you study today?");
                    case 1 -> yapLabel.setText("Ready to log today's progress?");
                    case 2 -> yapLabel.setText("Choose a language to update.");
                    default -> yapLabel.setText("What did you work on today?");
                }
            }

        }

        public TilePane createSessionManagersDisplay() {
            TilePane sessionManagersDisplay = new TilePane();
            sessionManagersDisplay.setAlignment(Pos.CENTER);
            sessionManagersDisplay.setPadding(new Insets(10));
            sessionManagersDisplay.setVgap(30);
            sessionManagersDisplay.setHgap(30);
            try {
                if (!Objects.requireNonNull(sessionManagerList).isEmpty()) {
                    for (SessionManager sessionManager1 : sessionManagerList) {
                        Label sessionManagerStats = new Label(sessionManager1.getLanguage().toString() + "\n" + minutesToHours(sessionManager1.getTotalMinutes()));
                        VBox sessionManagerLabel = new VBox(sessionManagerStats, getLevelIndicator(sessionManager1, true));
                        sessionManagersDisplay.getChildren().add(sessionManagerLabel);
                        sessionManagerLabel.getStyleClass().remove("selected-manager-card");
                        sessionManagerLabel.getStyleClass().add("manager-card");
                        sessionManagerLabel.setOnMouseClicked(_ -> {
                            if (selectedMLabel != null) {
                                selectedMLabel.getStyleClass().remove("selected-manager-card");
                                selectedMLabel.getStyleClass().add("manager-card");
                                yapLabel.setText("Log your " + sessionManager1.getLanguage().toString() + " progress.");
                            }
                            if (selectedMLabel == sessionManagerLabel) {
                                root.setCenter(tabPage.getTabRoot());
                            }

                            selectedMLabel = sessionManagerLabel;
                            sessionManager = sessionManager1;
                            sessionManagerLabel.getStyleClass().remove("manager-card");
                            sessionManagerLabel.getStyleClass().add("selected-manager-card");

                            if (selectedMLabel != null) {
                                yapLabel.setText("Log your " + sessionManager1.getLanguage().toString() + " progress.");
                            }
                            tabPage = new TabPage();

                        });
                    }
                } else {
                    sessionManager = null;
                    selectedMLabel = null;
                }
            } catch (NullPointerException ex) {
                sessionManagersDisplay = new TilePane();
            }

            return sessionManagersDisplay;
        }


        public VBox getMenuRoot() {
            updateMainMenu();
            return menuRoot;
        }

    }

    public class NewLanguagePage {

        private final GridPane newLanguageRoot;
        private final TextField grindingHoursInput = new TextField();
        private final TextField readingHoursInput = new TextField();
        private final TextField speakingHoursInput = new TextField();
        private final TextField listeningHoursInput = new TextField();
        private final TextField writingHoursInput = new TextField();
        private final ComboBox<Language> languageBox = new ComboBox<>();

        public NewLanguagePage() {
            Button createButton = new Button("Create");
            createButton.getStyleClass().add("default-box");

            Button cancelButton = new Button("Cancel");

            Label grindingHoursLabel = new Label("🎮 Grinding:");

            Label speakingHoursLabel = new Label("\uD83C\uDFA4 Speaking:");

            Label listeningHoursLabel = new Label("🎧 Listening:");

            Label readingHoursLabel = new Label("📖 Reading:");

            Label writingHoursLabel = new Label("✏ Writing:");

            Label newSessionManagerLabel = new Label("New Session Manager");
            Label languageLabel = new Label("🌐 Language:");
            newSessionManagerLabel.getStyleClass().add("new-manager-label");
            languageLabel.getStyleClass().add("new-manager-label");
            newSessionManagerLabel.setStyle("-fx-font-size: 40");

            cancelButton.getStyleClass().add("default-box");



            grindingHoursLabel.getStyleClass().add("new-manager-label");
            speakingHoursLabel.getStyleClass().add("new-manager-label");
            listeningHoursLabel.getStyleClass().add("new-manager-label");
            readingHoursLabel.getStyleClass().add("new-manager-label");
            writingHoursLabel.getStyleClass().add("new-manager-label");
            languageLabel.getStyleClass().add("new-manager-label");

            languageBox.getStyleClass().add("default-box");
            languageBox.getItems().addAll(Language.values());
            languageBox.setPromptText("Select a language");

            grindingHoursInput.getStyleClass().add("default-box");
            speakingHoursInput.getStyleClass().add("default-box");
            listeningHoursInput.getStyleClass().add("default-box");
            readingHoursInput.getStyleClass().add("default-box");
            writingHoursInput.getStyleClass().add("default-box");


            grindingHoursInput.setPromptText("Enter hours");
            readingHoursInput.setPromptText("Enter hours");
            writingHoursInput.setPromptText("Enter hours");
            speakingHoursInput.setPromptText("Enter hours");
            listeningHoursInput.setPromptText("Enter hours");

            GridPane grid = new GridPane();
            grid.add(newSessionManagerLabel, 0, 0, 5, 1);
            grid.add(languageLabel, 1, 1, 1, 1);
            grid.add(languageBox, 4, 1, 1, 1);
            grid.add(grindingHoursLabel, 1, 3, 1, 1);
            grid.add(grindingHoursInput, 4, 3, 1, 1);
            grid.add(speakingHoursLabel, 1, 4, 1, 1);
            grid.add(speakingHoursInput, 4, 4, 1, 1);
            grid.add(listeningHoursLabel, 1, 5, 1, 1);
            grid.add(listeningHoursInput, 4, 5, 1, 1);
            grid.add(readingHoursLabel, 1, 6, 1, 1);
            grid.add(readingHoursInput, 4, 6, 1, 1);
            grid.add(writingHoursLabel, 1, 7, 1, 1);
            grid.add(writingHoursInput, 4, 7, 1, 1);
            grid.add(createButton, 2, 8, 1, 1);
            grid.add(cancelButton, 3, 8, 1, 1);


            grid.setHgap(50);
            grid.setVgap(20);
            grid.setPadding(new Insets(10));
            grid.setStyle("-fx-background-color: #384c67;");

            newLanguageRoot = grid;

            cancelButton.setOnMouseClicked(_ -> root.setCenter(menuPage.getMenuRoot()));

            createButton.setOnMouseClicked(_ -> {

                try {
                    int grindingHours = Integer.parseInt(grindingHoursInput.getText());
                    grindingHoursInput.setPromptText("Enter hours");

                } catch (NumberFormatException ex) {
                    flashInvalid(grindingHoursInput);
                    grindingHoursInput.setPromptText("Input invalid");
                }

                try {
                    int speakingHours = Integer.parseInt(speakingHoursInput.getText());
                    speakingHoursInput.setPromptText("Enter hours");

                } catch (NumberFormatException ex) {
                    flashInvalid(speakingHoursInput);
                    speakingHoursInput.setPromptText("Input invalid");
                }
                try {
                    int listeningHours = Integer.parseInt(listeningHoursInput.getText());
                    listeningHoursInput.setPromptText("Enter hours");

                } catch (NumberFormatException ex) {
                    flashInvalid(listeningHoursInput);
                    listeningHoursInput.setPromptText("Input invalid");
                }
                try {
                    int readingHours = Integer.parseInt(readingHoursInput.getText());
                    readingHoursInput.setPromptText("Enter hours");

                } catch (NumberFormatException ex) {
                    flashInvalid(readingHoursInput);
                    readingHoursInput.setPromptText("Input invalid");
                }

                try {
                    int writingHours = Integer.parseInt(writingHoursInput.getText());
                    writingHoursInput.setPromptText("Enter hours");

                } catch (NumberFormatException ex) {
                    flashInvalid(writingHoursInput);
                    writingHoursInput.setPromptText("Input invalid");
                }


                try {
                    if (!(((Objects.equals(grindingHoursInput.getText(), "")) || (Objects.equals(speakingHoursInput.getText(), "")) || (Objects.equals(readingHoursInput.getText(), "")) || (Objects.equals(listeningHoursInput.getText(), "")) || (Objects.equals(writingHoursInput.getText(), ""))) || (languageBox.getSelectionModel().getSelectedItem() == null))) {

                        int grindingHours = Integer.parseInt(grindingHoursInput.getText());

                        int speakingHours = Integer.parseInt(speakingHoursInput.getText());

                        int listeningHours = Integer.parseInt(listeningHoursInput.getText());

                        int readingHours = Integer.parseInt(readingHoursInput.getText());

                        int writingHours = Integer.parseInt(writingHoursInput.getText());

                        sessionManagerList.add(new SessionManager(languageBox.getSelectionModel().getSelectedItem(), 0, 0, LocalDate.now().minusDays(1), grindingHours, speakingHours, readingHours, listeningHours, writingHours));
                        JsonStorage.save(sessionManagerList);

                        grindingHoursLabel.getStyleClass().add("new-manager-label");
                        grindingHoursInput.setPromptText("Enter hours");

                        root.setCenter(menuPage.getMenuRoot());


                    } else {


                        if (languageBox.getSelectionModel().getSelectedItem() == null) {
                            languageBox.setPromptText("Select a language");
                            flashInvalid(languageBox);
                        } else {
                            languageBox.setPromptText("Select a language");
                        }
                    }

                } catch (NumberFormatException _) {

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });


        }

        public void refreshNewLanguagePage() {

            languageBox.getSelectionModel().clearSelection();
            languageBox.setValue(null);
            Platform.runLater(() -> {
                languageBox.setPromptText("");
                languageBox.setPromptText("Select a language");
            });
            System.out.println(languageBox.getSelectionModel().getSelectedItem());
            System.out.println(languageBox.getPromptText());
            System.out.println(languageBox.getButtonCell());
            System.out.println(languageBox.getCellFactory());

            grindingHoursInput.setText("");
            readingHoursInput.setText("");
            writingHoursInput.setText("");
            speakingHoursInput.setText("");
            listeningHoursInput.setText("");

        }

        public GridPane getNewLanguageRoot() {
            refreshNewLanguagePage();
            return newLanguageRoot;
        }
    }


    //BUG NOTES

    //EVERYTHING JUST BECOMES ... WHEN THE PAGE ISN'T BIG ENOUGH
    //IF YOU HOVER OVER A BOX WHILE THE INVALID FLASH IS STILL HAPPENING IT LOOKS WEIRD
    //EDITING SESSIONS DOESN'T UPDATE XP PROPERLY
    //FIND BEST BALANCE FOR XP, MAKE DELETION MORE ACCURATE
}