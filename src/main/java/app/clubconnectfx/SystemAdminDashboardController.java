package app.clubconnectfx;

import BL.SystemAdminBL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import model.SystemAdmin;

public class SystemAdminDashboardController {

    @FXML private StackPane contentArea;

    private SystemAdmin loggedAdmin;
    private final SystemAdminBL controller = new SystemAdminBL();

    @FXML
    public void initialize() {
        loadPage("SystemAdminHome.fxml");
    }

    // Inject logged admin (Navigator will call this)
    private void loadPage(String fxml) {
        try {
            // Load view into a Node
            javafx.scene.Node page = FXMLLoader.load(
                    getClass().getResource(fxml)
            );

            // Clear old content and add new page
            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Menu actions
    @FXML private void loadHome()      { loadPage("SystemAdminHome.fxml"); }
    @FXML private void loadClubs()     { loadPage("SystemAdminClubs.fxml"); }
    @FXML private void loadStudents()  { loadPage("SystemAdminStudents.fxml"); }
    @FXML private void loadAdmins()    { loadPage("SystemAdminAdmins.fxml"); }
    @FXML private void loadEvents()    { loadPage("SystemAdminEvents.fxml"); }
    @FXML private void loadBroadcast() { loadPage("SystemAdminBroadcast.fxml"); }
    @FXML private void loadRequests()  { loadPage("SystemAdminRequests.fxml"); }

    @FXML private void logout() {
        Navigator.logout();
    }
}
