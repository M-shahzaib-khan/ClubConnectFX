package app.clubconnectfx;

import BL.ClubAdminBL;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Club;
import model.Student;

import java.util.ArrayList;

public class AdminRequestsController {

    @FXML private TableView<Student> requestsTable;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colDept;
    @FXML private TableColumn<Student, Integer> colSemester;

    private final ClubAdminBL adminController = new ClubAdminBL();
    private Club managedClub;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDept.setCellValueFactory(new PropertyValueFactory<>("department"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));

        if (SessionData.currentClubAdmin != null) {
            managedClub = adminController.getManagedClub(SessionData.currentClubAdmin.getUserId());
        }
        loadRequests();
    }

    private void loadRequests() {
        if (managedClub == null) return;

        ArrayList<Student> pending = adminController.getPendingRequests(managedClub.getClubId());
        requestsTable.setItems(FXCollections.observableArrayList(pending));
    }

    @FXML
    private void approveSelected() {
        Student s = requestsTable.getSelectionModel().getSelectedItem();
        if (s == null || managedClub == null) return;

        boolean ok = adminController.approveRequest(managedClub.getClubId(), s.getUserId());
        showMsg(ok ? "Approved!" : "Error approving request.");
        loadRequests();
    }

    @FXML
    private void rejectSelected() {
        Student s = requestsTable.getSelectionModel().getSelectedItem();
        if (s == null || managedClub == null) return;

        boolean ok = adminController.rejectRequest(managedClub.getClubId(), s.getUserId());
        showMsg(ok ? "Rejected." : "Error rejecting request.");
        loadRequests();
    }

    private void showMsg(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
