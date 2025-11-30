package app.clubconnectfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class SystemDashboardController {

    @FXML
    private StackPane contentArea;

    private void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            contentArea.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openUsers() {
        loadPage("system-users.fxml");
    }

    @FXML
    private void openApprovals() {
        loadPage("system-approvals.fxml");
    }

    @FXML
    private void openClubs() {
        loadPage("system-clubs.fxml");
    }

    @FXML
    private void logout() {
        Navigator.logout();
    }
}
