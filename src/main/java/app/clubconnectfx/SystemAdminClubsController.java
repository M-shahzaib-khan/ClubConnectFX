package app.clubconnectfx;

import BL.SystemAdminBL;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Club;

public class SystemAdminClubsController {

    @FXML private TableView<Club> clubTable;
    @FXML private TableColumn<Club, Integer> colId;
    @FXML private TableColumn<Club, String> colName;
    @FXML private TableColumn<Club, String> colDesc;
    @FXML private TableColumn<Club, Integer> colCreated;

    private final SystemAdminBL controller = new SystemAdminBL();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("clubId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("clubName"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCreated.setCellValueFactory(new PropertyValueFactory<>("createdBy"));

        loadClubs();
    }

    private void loadClubs() {
        clubTable.getItems().setAll(controller.getAllClubs());
    }

    @FXML
    private void deleteClub() {
        Club selected = clubTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Select a club first."); return;
        }

        if (controller.deleteClub(selected.getClubId())) {
            alert("Club deleted.");
            loadClubs();
        } else alert("Failed to delete club.");
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
