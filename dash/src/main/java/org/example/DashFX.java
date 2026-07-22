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

    private Session selectedSession;
    private SessionManager sessionManager;
    private LocalDate pickedDate = LocalDate.now();
    public List<SessionManager> sessionManagerList = JsonStorage.load();

    Label logDateLabel = new Label();

    private final BorderPane root = new BorderPane();
    MenuPage menuPage = new MenuPage();
    TabPage tabPage = new TabPage();
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
        progress.setStroke(Color.MEDIUMPURPLE);
        progress.setStrokeWidth(6);
        progress.setFill(null);
        progress.setType(ArcType.OPEN);
        if(main){
            progress.setCenterX(49);
            progress.setCenterY(52);
        }
        else{
            progress.setCenterX(48);
            progress.setCenterY(57);
        }
        progress.setManaged(false);

        Label symbolLabel = new Label(" " + manager.getLevel().getSymbol());
        symbolLabel.setStyle("-fx-font-size: 17;"+"-fx-text-fill: white;" + "-fx-font-weight: bold;");
        Label percentageLabel = new Label(manager.getTotalProgress() + "%");
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

    public String minutesToHours(int num) {
        int hours = num / 60;
        int minutes = num % 60;
        if (hours < 1) {
            return minutes + "m";

        }
        return hours + "h " + minutes + "m";
    }

    public class TabPage{

        private final VBox tabRoot;

        private final BorderPane sessionsTab = new BorderPane();

        private final VBox calendar = buildCalendar();
        private VBox sessionDisplay = new VBox();

        private int lastXp = 0;
        private Label selectedLabel;

        Label newDateLabel = new Label("New Date: " + pickedDate);
        TextArea logOutput = new TextArea();
        TextArea editOutput  = new TextArea();
        ComboBox<ActivityType> logActivityBox = new ComboBox<>();
        TextField logMinutesInput = new TextField();

        private final VBox mainMenu = buildMainMenu();
        private final VBox logMenu = buildLogMenu();
        private final VBox editMenu = buildEditMenu();
        private final TabPane tabPane = buildTabPane();

        public TabPage() {

            tabRoot = new VBox(updateDashboard(), tabPane);
        }

        public TabPane buildTabPane() {


            TabPane tabPane = new TabPane();
            Tab calendarTab = new Tab("Calendar");
            Tab progressTab = new Tab("Progress");



            progressTab.setOnSelectionChanged(_ -> {
                if(sessionManager.getXp()!=lastXp){
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
                    System.out.println("updated progress stuffs");}

                lastXp = sessionManager.getXp();
            });

            calendarTab.setContent(new HBox(calendar,sessionsTab));
            tabPane.getTabs().addAll(calendarTab, progressTab);
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            showMainMenu();

            return tabPane;
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

        public HBox updateDashboard() {

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

            Button changeLanguageButton = new Button("CHANGE LANGUAGE");
            changeLanguageButton.getStyleClass().add("manager-card");

            changeLanguageButton.setOnMouseClicked(_ -> root.setCenter(menuPage.getMenuRoot()));

            HBox statsBox = new HBox(changeLanguageButton, languageBox, dateBox, streakBox, weekStudyBox, totalStudyBox);
            statsBox.setSpacing(50);
            VBox left = new VBox(titleLabel, statsBox);
            left.setSpacing(15);

            HBox dashBoardBox = new HBox(left,spacer, getLevelIndicator(sessionManager,false));
            dashBoardBox.setSpacing(30);
            dashBoardBox.setAlignment(Pos.CENTER_LEFT);
            dashBoardBox.setStyle("-fx-background-color: #151A22;" +
                    "-fx-background-radius: 15;" +
                    "-fx-padding: 25;");

            dashBoardBox.setMaxWidth(Double.MAX_VALUE);
            return dashBoardBox;
        }

        public VBox buildMainMenu(){
            Button logMenuButton = new Button("Log session");
            Button editButton = new Button("Edit session");
            Button deleteButton = new Button("Delete session");
            Label sessionsLabel = new Label("Sessions:");

            logMenuButton.setOnMouseClicked(_ -> showLogMenu());


            editButton.setOnMouseClicked(_ -> showEditMenu());


            deleteButton.setOnMouseClicked(_ -> {
                if (selectedSession != null) {
                    sessionManager.deleteSession(selectedSession);
                    try {
                        updateDashboard();
                        refreshMainMenu();
                        JsonStorage.save(sessionManagerList);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            sessionDisplay = createSessionDisplay();

            VBox sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);
            sessionLayout.setSpacing(10);


            return sessionLayout;
        }

        public void refreshMainMenu(){
            mainMenu.getChildren().remove(sessionDisplay);
            mainMenu.getChildren().add(1,createSessionDisplay());
        }

        public VBox buildLogMenu(){
            Button closeButton = new Button("Close");
            Label minutesLabel = new Label("Minutes:");

            logActivityBox.setPromptText("Select an activity");
            logActivityBox.setPromptText("Select an activity");
            logActivityBox.getItems().addAll(ActivityType.values());

            logActivityBox.getItems().addAll(ActivityType.values());
            Label activityTypeLabel = new Label("Activity Type:");

            Button logButton = new Button("Log");
            logOutput.setEditable(false);

                logDateLabel.setText("Logging session for " + pickedDate);
            logActivityBox.setValue(null);
            logMinutesInput.setText("");
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
                            JsonStorage.save(sessionManagerList);
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();

                            if (Objects.equals(logMinutesInput.getText(), "")) {
                                stringBuilder.append("No minutes entered!\nEnter the minutes!\n\n");
                            }
                            if (logActivityBox.getSelectionModel().getSelectedItem() == null) {
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
            });

            HBox loggerButtons = new HBox(logButton, closeButton);
            loggerButtons.setSpacing(15);

            return new VBox(logDateLabel, activityTypeLabel, logActivityBox, minutesLabel, logMinutesInput, logOutput, loggerButtons);
        }

        public void refreshLogMenu(){
            logDateLabel.setText("Logging session for " + pickedDate);
            logActivityBox.setValue(null);
            logMinutesInput.setText("");
            logOutput.setText("");



        }

        public VBox buildEditMenu(){

            Button closeButton = new Button("Close");
            Label activityTypeLabel = new Label("Activity Type:");
            Label minutesLabel = new Label("Minutes:");
            ComboBox<ActivityType> activityBox = new ComboBox<>();
            activityBox.setValue(selectedSession.getActivityType());
            activityBox.getItems().addAll(ActivityType.values());
            TextField minutesInput = new TextField();
            minutesInput.setPromptText(String.valueOf(selectedSession.getMinutes()));

            editOutput.setEditable(false);
            Button saveButton = new Button("Save");

            if(selectedSession != null) {
                activityBox.setValue(selectedSession.getActivityType());
                minutesInput.setText(String.valueOf(selectedSession.getMinutes()));
                editOutput.setText("");
            }

            else{
                activityBox.setValue(null);
                minutesInput.setText("");
            }

            closeButton.setOnMouseClicked(_ -> showMainMenu());



            saveButton.setOnMouseClicked(_ -> {
                try {
                    if (pickedDate == null) {
                        editOutput.setText("No date selected!\nSelect a date!");
                    } else {
                        if (!(Objects.equals(minutesInput.getText(), "") || (activityBox.getSelectionModel().getSelectedItem() == null))) {
                            int minutes = Integer.parseInt(minutesInput.getText());
                            ActivityType activityType = activityBox.getSelectionModel().getSelectedItem();
                            sessionManager.editSession(selectedSession, pickedDate, activityType, minutes);
                            editOutput.setText("Session updated successfully.");
                            updateDashboard();
                            JsonStorage.save(sessionManagerList);
                            showMainMenu();
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();

                            if (Objects.equals(minutesInput.getText(), "")) {
                                stringBuilder.append("No minutes entered!\nEnter the minutes!\n\n");
                            }
                            if (activityBox.getSelectionModel().getSelectedItem() == null) {
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

            HBox editorButtons = new HBox(saveButton, closeButton);
            editorButtons.setSpacing(15);

            return new VBox(newDateLabel, activityTypeLabel, activityBox, minutesLabel, minutesInput, editOutput, editorButtons);
        }

        public void refreshEditMenu(){
            editOutput.setText("");
        }

        public VBox buildCalendar(){

            Label l = new Label("Date : " + pickedDate);
            DatePicker d = new DatePicker();

            d.setShowWeekNumbers(true);

            EventHandler<ActionEvent> setCalendar = _ -> {
                pickedDate = d.getValue();
                if (pickedDate == null) {
                    pickedDate = LocalDate.now();
                }
                sessionManager.makeDayNotNull(pickedDate);

                l.setText("Date : " + pickedDate);

                logDateLabel.setText("Logging session for " + pickedDate);
                newDateLabel.setText("New Date: " + pickedDate);
                if(sessionsTab.getCenter()==mainMenu){
                    showMainMenu();
                }


//                    sessionDisplay = createSessionDisplay();
//                    sessionLayout = new VBox(sessionsLabel, sessionDisplay, logMenuButton, editButton, deleteButton);
//                    sessionLayout.setSpacing(10);
//                    HBox newCalendarLayout = new HBox(calendar, sessionLayout);
//                    newCalendarLayout.setSpacing(10);
//               calendarTab.setContent(newCalendarLayout);
//                }
            };

            d.setOnAction(setCalendar);


            return new VBox(d,l);
        }


        public VBox getTabRoot(){
            tabRoot.getChildren().removeFirst();
            tabRoot.getChildren().addFirst(updateDashboard());
            return tabRoot;
        }

        public void showMainMenu(){
            refreshMainMenu();
            sessionsTab.setCenter(mainMenu);
            if(tabPane!=null){
                tabPane.requestLayout();
            }
        }

        public void showLogMenu(){
            refreshLogMenu();
            sessionsTab.setCenter(logMenu);
            if(tabPane!=null){
                tabPane.requestLayout();
            }
        }

        public void showEditMenu(){
            if(selectedSession != null) {
                refreshEditMenu();
                sessionsTab.setCenter(editMenu);
                if(tabPane!=null){
                    tabPane.requestLayout();
                }
            }

        }
    }



    public class MenuPage {

        VBox menuRoot;
        TilePane sessionManagersDisplay;
        private VBox selectedMLabel;

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
            }});

            sessionManagersDisplay = createSessionManagersDisplay();
            Label noSessionManager = new Label("No session managers found.");
            Label welcomeLabel = new Label("Welcome back.\nWhat have you studied this time?");
            welcomeLabel.setStyle("-fx-text-fill: white;"+"-fx-font-size: 30;");
            if (sessionManager == null) {
                noSessionManager.setVisible(true);
                noSessionManager.setManaged(true);
            } else {
                noSessionManager.setVisible(false);
                noSessionManager.setManaged(false);
            }
            HBox hBox = new HBox(newSessionManagerButton, deleteSessionManagerButton);
            hBox.setSpacing(30);
            hBox.setAlignment(Pos.CENTER);
            menuRoot = new VBox(welcomeLabel, noSessionManager, sessionManagersDisplay ,hBox);
            menuRoot.setSpacing(10);
            menuRoot.setAlignment(Pos.CENTER);
            menuRoot.setStyle("-fx-padding: 10;");

        }

        public void updateSessionDisplay(){
            menuRoot.getChildren().remove(sessionManagersDisplay);
            sessionManagersDisplay = createSessionManagersDisplay();
            menuRoot.getChildren().add(2, sessionManagersDisplay);

        }

        public TilePane createSessionManagersDisplay() {
            TilePane sessionManagersDisplay = new TilePane();
            sessionManagersDisplay.setAlignment(Pos.CENTER);
            sessionManagersDisplay.setPadding(new Insets(10));
            sessionManagersDisplay.setVgap(30);
            sessionManagersDisplay.setHgap(30);
            try {
                if (!Objects.requireNonNull(sessionManagerList).isEmpty()) {
                    int count = 0;
                    for (SessionManager sessionManager1 : sessionManagerList) {
                        Label sessionManagerStats = new Label(sessionManager1.getLanguage().toString()+"\n" + minutesToHours(sessionManager1.getTotalMinutes()));
                        VBox sessionManagerLabel = new VBox(sessionManagerStats,getLevelIndicator(sessionManager1,true));
                        sessionManagersDisplay.getChildren().add(sessionManagerLabel);
                        sessionManagerLabel.getStyleClass().remove("selected-manager-card");
                        sessionManagerLabel.getStyleClass().add("manager-card");
                        if (count == 0) {
                            sessionManager = sessionManager1;
                            selectedMLabel = sessionManagerLabel;
                            selectedMLabel.getStyleClass().remove("manager-card");
                            selectedMLabel.getStyleClass().add("selected-manager-card");
                        }
                        count++;
                        sessionManagerLabel.setOnMouseClicked(_ -> {
                            if (selectedMLabel != null) {
                                selectedMLabel.getStyleClass().remove("selected-manager-card");
                                selectedMLabel.getStyleClass().add("manager-card");
                            }
                            if (selectedMLabel == sessionManagerLabel) {
                                root.setCenter(tabPage.getTabRoot());
                            }

                            selectedMLabel = sessionManagerLabel;
                            sessionManager = sessionManager1;
                            sessionManagerLabel.getStyleClass().remove("manager-card");
                            sessionManagerLabel.getStyleClass().add("selected-manager-card");

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
            updateSessionDisplay();
            return menuRoot;
        }

    }

    public class NewLanguagePage{

        private final GridPane newLanguageRoot;
        private final TextField grindingHoursInput;
        private final TextField readingHoursInput;
        private final TextField speakingHoursInput;
        private final TextField listeningHoursInput;
        private final TextField writingHoursInput;
        private final ComboBox<Language> languageBox;

        public NewLanguagePage() {
            Button createButton = new Button("Create");
            createButton.getStyleClass().add("new-manager-button");

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

                cancelButton.getStyleClass().add("new-manager-button");

                languageBox = new ComboBox<>();
                languageBox.getItems().addAll(Language.values());

                grindingHoursInput = new TextField();
                readingHoursInput = new TextField();
                listeningHoursInput = new TextField();
                writingHoursInput = new TextField();
                speakingHoursInput = new TextField();


                grindingHoursInput.getStyleClass().removeAll("new-manager-bad-box");
                speakingHoursInput.getStyleClass().removeAll("new-manager-bad-box");
                listeningHoursInput.getStyleClass().removeAll("new-manager-bad-box");
                readingHoursInput.getStyleClass().removeAll("new-manager-bad-box");
                writingHoursInput.getStyleClass().removeAll("new-manager-bad-box");


                grindingHoursLabel.getStyleClass().add("new-manager-label");
                speakingHoursLabel.getStyleClass().add("new-manager-label");
                listeningHoursLabel.getStyleClass().add("new-manager-label");
                readingHoursLabel.getStyleClass().add("new-manager-label");
                writingHoursLabel.getStyleClass().add("new-manager-label");
                languageLabel.getStyleClass().add("new-manager-label");

                speakingHoursInput.getStyleClass().add("new-manager-box");
                readingHoursInput.getStyleClass().add("new-manager-box");
                writingHoursInput.getStyleClass().add("new-manager-box");
                grindingHoursInput.getStyleClass().add("new-manager-box");
                listeningHoursInput.getStyleClass().add("new-manager-box");


                languageBox.getStyleClass().remove("new-manager-bad-box");
                languageBox.getStyleClass().add("new-manager-box");


                languageBox.setValue(null);
                languageBox.setPromptText("Select a language");


                grindingHoursInput.setPromptText("Enter hours");
                readingHoursInput.setPromptText("Enter hours");
                writingHoursInput.setPromptText("Enter hours");
                speakingHoursInput.setPromptText("Enter hours");
                listeningHoursInput.setPromptText("Enter hours");

                GridPane grid = new GridPane();
                grid.add(newSessionManagerLabel,0,0,5,1);
                grid.add(languageLabel,1,1,1,1);
                grid.add(languageBox,4,1,1,1);
                grid.add(grindingHoursLabel,1,3,1,1);
                grid.add(grindingHoursInput,4,3,1,1);
                grid.add(speakingHoursLabel,1,4,1,1);
                grid.add(speakingHoursInput,4,4,1,1);
                grid.add(listeningHoursLabel,1,5,1,1);
                grid.add(listeningHoursInput,4,5,1,1);
                grid.add(readingHoursLabel,1,6,1,1);
                grid.add(readingHoursInput,4,6,1,1);
                grid.add(writingHoursLabel,1,7,1,1);
                grid.add(writingHoursInput,4,7,1,1);
                grid.add(createButton, 2,8,1,1);
                grid.add(cancelButton, 3,8,1,1);


                grid.setHgap(50);
                grid.setVgap(20);
                grid.setPadding(new Insets(10));
                grid.setStyle("-fx-background-color: #384c67;");

                newLanguageRoot = grid;

                cancelButton.setOnMouseClicked(_ -> root.setCenter(menuPage.getMenuRoot()));

            createButton.setOnMouseClicked(_ -> {

                try{
                    int grindingHours = Integer.parseInt(grindingHoursInput.getText());
                    grindingHoursInput.getStyleClass().removeAll("new-manager-bad-box");
                    grindingHoursInput.getStyleClass().add("new-manager-box");
                    grindingHoursInput.setPromptText("Enter hours");

                } catch (NumberFormatException ex) {
                    grindingHoursInput.getStyleClass().add("new-manager-bad-box");
                    grindingHoursInput.setPromptText("Input invalid");
                }

                try{
                    int speakingHours = Integer.parseInt(speakingHoursInput.getText());
                    speakingHoursInput.getStyleClass().removeAll("new-manager-bad-box");
                    speakingHoursInput.getStyleClass().add("new-manager-box");
                    speakingHoursInput.setPromptText("Enter hours");

                } catch (NumberFormatException ex) {
                    speakingHoursInput.getStyleClass().add("new-manager-bad-box");
                    speakingHoursInput.setPromptText("Input invalid");
                }
                try{
                    int listeningHours = Integer.parseInt(listeningHoursInput.getText());
                    listeningHoursInput.getStyleClass().removeAll("new-manager-bad-box");
                    listeningHoursInput.getStyleClass().add("new-manager-box");
                    listeningHoursInput.setPromptText("Enter hours");

                }  catch (NumberFormatException ex) {
                    listeningHoursInput.getStyleClass().add("new-manager-bad-box");
                    listeningHoursInput.setPromptText("Input invalid");
                }
                try{
                    int readingHours = Integer.parseInt(readingHoursInput.getText());
                    readingHoursInput.getStyleClass().removeAll("new-manager-bad-box");
                    readingHoursInput.getStyleClass().add("new-manager-box");
                    readingHoursInput.setPromptText("Enter hours");

                }catch (NumberFormatException ex) {
                    readingHoursInput.getStyleClass().add("new-manager-bad-box");
                    readingHoursInput.setPromptText("Input invalid");
                }

                try{
                    int  writingHours = Integer.parseInt(writingHoursInput.getText());
                    writingHoursInput.getStyleClass().removeAll("new-manager-bad-box");
                    writingHoursInput.getStyleClass().add("new-manager-box");
                    writingHoursInput.setPromptText("Enter hours");

                } catch (NumberFormatException ex) {
                    writingHoursInput.getStyleClass().add("new-manager-bad-box");
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
                            languageBox.getStyleClass().remove("new-manager-box");
                            languageBox.getStyleClass().add("new-manager-bad-box");}
                        else {
                            languageBox.setPromptText("Select a language");
                            languageBox.getStyleClass().remove("new-manager-bad-box");
                            languageBox.getStyleClass().add("new-manager-box");
                        }
                    }

                } catch (NumberFormatException _) {

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });


        }

        public GridPane getNewLanguageRoot() {
            return newLanguageRoot;
        }
    }




    //TODO you're trying to get rid of the close button on the welcome menu
    //TODO then switch the title to
    // What did you work on today?
    //or
    //Ready to log today's progress?
    //or
    //Choose a language to update.
    //or
    //Which language did you study today?
    //TODO then consider making the hierarchy of each session manager card language -> level ring -> hours
    //TODO consider adding more animations to stuff
    //TODO make the message change when a language is selected. something like "Log your (language) session.\n click again to continue"


    //BUG NOTES
    //EDITING SESSIONS DOESN'T UPDATE XP PROPERLY
    //FIND BEST BALANCE FOR XP, MAKE DELETION MORE ACCURATE
}