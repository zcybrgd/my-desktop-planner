import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private LocalDate date;
    private List<FreeTimeSlot> freeTimeSlots;
    private ListView<FreeTimeSlot> freeTimeSlotListView;
    private TextArea taskTextArea;
    
    @Override
    public void start(Stage primaryStage) {
        date = LocalDate.now();
        freeTimeSlots = new ArrayList<>();
        
        // create UI components
        Label dateLabel = new Label(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, u")));
        Button prevButton = new Button("Previous");
        Button nextButton = new Button("Next");
        HBox header = new HBox(prevButton, dateLabel, nextButton);
        header.setSpacing(10);
        
        freeTimeSlotListView = new ListView<>();
        freeTimeSlotListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                taskTextArea.setText(newValue.getTask());
            } else {
                taskTextArea.setText("");
            }
        });
        
        taskTextArea = new TextArea();
        taskTextArea.setPrefRowCount(4);
        
        Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FreeTimeSlot newSlot = new FreeTimeSlot(LocalTime.now(), LocalTime.now().plusHours(1), "");
                freeTimeSlots.add(newSlot);
                freeTimeSlotListView.getItems().add(newSlot);
                freeTimeSlotListView.getSelectionModel().select(newSlot);
            }
        });
        
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FreeTimeSlot selectedSlot = freeTimeSlotListView.getSelectionModel().getSelectedItem();
                if (selectedSlot != null) {
                    freeTimeSlots.remove(selectedSlot);
                    freeTimeSlotListView.getItems().remove(selectedSlot);
                    taskTextArea.setText("");
                }
            }
        });
        
        VBox controls = new VBox(addButton, removeButton);
        controls.setSpacing(10);
        
        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(freeTimeSlotListView);
        root.setBottom(new HBox(taskTextArea, controls));
        Scene scene = new Scene(root, 600, 400);
        
        // set up event handlers
        prevButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                date = date.minusDays(1);
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, u")));
                freeTimeSlots.clear();
                freeTimeSlotListView.getItems().clear();
                taskTextArea.setText("");
            }
        });
        
        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                date = date.plusDays(1);
                dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, u")));
                freeTimeSlots.clear();
                freeTimeSlotListView.getItems().clear();
                taskTextArea.setText("");
            }
        });
        
        // show the
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private class FreeTimeSlot {
        private LocalTime startTime;
        private LocalTime endTime;
        private String task;
        
        public FreeTimeSlot(LocalTime startTime, LocalTime endTime, String task) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.task = task;
        }
        
        public LocalTime getStartTime() {
            return startTime;
        }
        
        public LocalTime getEndTime() {
            return endTime;
        }
        
        public String getTask() {
            return task;
        }
        
        public void setTask(String task) {
            this.task = task;
        }
        
        @Override
        public String toString() {
            return startTime.format(DateTimeFormatter.ofPattern("h:mm a")) + " - " + endTime.format(DateTimeFormatter.ofPattern("h:mm a"));
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}    