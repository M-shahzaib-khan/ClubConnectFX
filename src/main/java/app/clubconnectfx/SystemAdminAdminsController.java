package app.clubconnectfx;

import BL.SystemAdminBL;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ClubAdmin;

public class SystemAdminAdminsController {

    @FXML private TableView<ClubAdmin> adminTable;
    @FXML private TableColumn<ClubAdmin, Integer> colId;
    @FXML private TableColumn<ClubAdmin, String> colUser;
    @FXML private TableColumn<ClubAdmin, String> colName;
    @FXML private TableColumn<ClubAdmin, String> colEmail;
    @FXML private TableColumn<ClubAdmin, Integer> colClub;

    private final SystemAdminBL controller = new SystemAdminBL();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colClub.setCellValueFactory(new PropertyValueFactory<>("managedClubId"));

        loadAdmins();
    }

    private void loadAdmins() {
        adminTable.getItems().setAll(controller.getAllAdmins());
    }
}
