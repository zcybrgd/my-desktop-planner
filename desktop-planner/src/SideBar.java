import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SideBar extends Application {

    private BorderPane root;
    private VBox sidebar;
    private Button calendarButton, projectsButton, statsButton, historyButton, settingsButton, logoutButton;

    @Override
    public void start(Stage primaryStage) {

       // Create the image views for the icons
ImageView calendarIcon = new ImageView(new Image("assets/calendar.png"));
ImageView projectsIcon = new ImageView(new Image("assets/projects.png"));
ImageView statsIcon = new ImageView(new Image("assets/stats.png"));
ImageView historyIcon = new ImageView(new Image("assets/history.png"));

// Create the buttons with icons
Button calendarBtn = new Button("Calendar", calendarIcon);
Button projectsBtn = new Button("My Projects", projectsIcon);
Button statsBtn = new Button("Statistics", statsIcon);
Button historyBtn = new Button("History", historyIcon);

// Set the button sizes
calendarBtn.setPrefWidth(200);
projectsBtn.setPrefWidth(200);
statsBtn.setPrefWidth(200);
historyBtn.setPrefWidth(200);

// Add the buttons to the sidebar
VBox sidebar = new VBox();
sidebar.getChildren().addAll(calendarBtn, projectsBtn, statsBtn, historyBtn);

// Set the action for the buttons
calendarBtn.setOnAction(e -> {
    // Code to open the calendar page
});
projectsBtn.setOnAction(e -> {
    // Code to open the projects page
});
statsBtn.setOnAction(e -> {
    // Code to open the statistics page
});
historyBtn.setOnAction(e -> {
    // Code to open the history page
});

// Create the toggle button to open/close the sidebar
ToggleButton toggleBtn = new ToggleButton();
ImageView toggleIcon = new ImageView(new Image("assets/history.png"));
toggleBtn.setGraphic(toggleIcon);
toggleBtn.setOnAction(e -> {
    if (toggleBtn.isSelected()) {
        sidebar.setVisible(true);
    } else {
        sidebar.setVisible(false);
    }
});

// Create the top buttons for settings and logout
Button settingsBtn = new Button("Settings");
Button logoutBtn = new Button("Logout");

// Add the buttons to the top pane
HBox topPane = new HBox();
topPane.getChildren().addAll(toggleBtn, settingsBtn, logoutBtn);

// Create the main layout
BorderPane mainLayout = new BorderPane();
mainLayout.setLeft(sidebar);
mainLayout.setTop(topPane);

// Set the scene and show the stage
Scene scene = new Scene(mainLayout, 800, 600);
primaryStage.setScene(scene);
primaryStage.show();

    }

    // Define methods to create content for each page
    private Button calendarPage() {
        return new Button("Calendar Page");
    }

    private Button projectsPage() {
        return new Button("My Projects Page");
    }

    private Button statsPage() {
        return new Button("Statistics Page");
    }

    private Button historyPage() {
        return new Button("History Page");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
