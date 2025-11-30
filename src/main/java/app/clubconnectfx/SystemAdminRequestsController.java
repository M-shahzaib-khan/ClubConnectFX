package app.clubconnectfx;

import BL.SystemAdminBL;
import dao.ClubAdminDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

public class SystemAdminRequestsController {

    @FXML private TableView<ClubAdminDAO.AdminRequest> reqTable;

    @FXML private TableColumn<ClubAdminDAO.AdminRequest, Integer> colId;
    @FXML private TableColumn<ClubAdminDAO.AdminRequest, String> colUser;
    @FXML private TableColumn<ClubAdminDAO.AdminRequest, String> colName;
    @FXML private TableColumn<ClubAdminDAO.AdminRequest, String> colEmail;
    @FXML private TableColumn<ClubAdminDAO.AdminRequest, String> colClub;
    @FXML private TableColumn<ClubAdminDAO.AdminRequest, String> colDesc;
    @FXML private TableColumn<ClubAdminDAO.AdminRequest, String> colTime;

    private final SystemAdminBL controller = new SystemAdminBL();

    @FXML
    public void initialize() {
        setupColumns();
        loadRequests();
    }

    private void setupColumns() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().requestId).asObject());
        colUser.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().username));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().fullName));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().email));
        colClub.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().clubName));
        colDesc.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().clubDesc));
        colTime.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().requestedAt.toString()
        ));
    }

    private void loadRequests() {
        reqTable.setItems(FXCollections.observableArrayList(
                controller.getPendingAdminRequests()
        ));
    }

    @FXML
    private void approveRequest() {
        ClubAdminDAO.AdminRequest req = reqTable.getSelectionModel().getSelectedItem();
        if (req == null) { alert("Select a request first."); return; }

        boolean ok = controller.approveAdminRequest(req);
        alert(ok ? "Approved!" : "Failed");
        loadRequests();
    }

    @FXML
    private void rejectRequest() {
        ClubAdminDAO.AdminRequest req = reqTable.getSelectionModel().getSelectedItem();
        if (req == null) { alert("Select a request first."); return; }

        boolean ok = controller.rejectAdminRequest(req.requestId);
        alert(ok ? "Rejected!" : "Failed");
        loadRequests();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.show();
    }
}
