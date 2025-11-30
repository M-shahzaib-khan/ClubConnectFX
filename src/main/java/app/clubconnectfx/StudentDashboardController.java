package app.clubconnectfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class StudentDashboardController {

    @FXML
    private StackPane contentArea;

    private Node loadView(String file) {
        try {
            return FXMLLoader.load(getClass().getResource(file));
        } catch (Exception e) {
            e.printStackTrace();
            return new javafx.scene.control.Label("Error loading: " + file);
        }
    }

    @FXML
    private void openClubs() {
        contentArea.getChildren().setAll(loadView("student-clubs.fxml"));
    }

    @FXML
    private void openEvents() {
        contentArea.getChildren().setAll(loadView("students-events.fxml"));
    }

    @FXML
    private void openMyRegistrations() {
        contentArea.getChildren().setAll(loadView("student-registrations.fxml"));
    }

    @FXML
    private void openNotifications() {
        contentArea.getChildren().setAll(loadView("student-notifications.fxml"));
    }

    @FXML
    private void openProfile() {
        contentArea.getChildren().setAll(loadView("student-profile.fxml"));
    }

    @FXML
    private void logout() {
        Navigator.logout();
    }
}
