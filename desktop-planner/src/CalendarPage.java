import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarPage extends Application {

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");

    private TableView<CalendarEvent> eventTableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        // Create date picker
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setShowWeekNumbers(true);
        datePicker.setDayCellFactory(this::createDateCell);

        // Create table view
        TableColumn<CalendarEvent, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<CalendarEvent, String> eventCol = new TableColumn<>("Event");
        eventCol.setCellValueFactory(new PropertyValueFactory<>("event"));
        eventTableView.getColumns().addAll(dateCol, eventCol);
        eventTableView.setPlaceholder(new Button("No events"));
        eventTableView.setPrefHeight(400);

        // Create hbox for date picker and view selector
        HBox topBox = new HBox(10, datePicker, createViewSelector());
        topBox.setAlignment(Pos.CENTER);

        // Add components to root
        root.setTop(topBox);
        root.setCenter(eventTableView);

        // Set up scene and show stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Calendar");
        primaryStage.show();
    }

    private HBox createViewSelector() {
        Button dayButton = new Button("Day");
        Button weekButton = new Button("Week");
        Button monthButton = new Button("Month");
        Button yearButton = new Button("Year");
        List<Button> buttons = new ArrayList<>(List.of(dayButton, weekButton, monthButton, yearButton));
        buttons.forEach(button -> button.setOnAction(event -> updateCalendarView(button.getText())));
        HBox hbox = new HBox(buttons.toArray(new Button[0]));
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    private void updateCalendarView(String view) {
        // TODO: Update calendar view based on selected view
    }

    private DateCell createDateCell(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.getDayOfWeek() == DayOfWeek.SUNDAY || item.getDayOfWeek() == DayOfWeek.SATURDAY);
            }
        };
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class CalendarEvent {
        private final LocalDate date;
        private final String event;

        public CalendarEvent(LocalDate date, String event) {
            this.date = date;
            this.event = event;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getEvent() {
            return event;
        }
    }
}
