package app.clubconnectfx;

import BL.ClubAdminBL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Club;
import model.Student;

import java.util.ArrayList;

public class AdminClubInfoController {

    @FXML private Label lblClubName;
    @FXML private Label lblClubDesc;
    @FXML private Label lblMembers;
    @FXML private Label lblPending;

    private final ClubAdminBL adminController = new ClubAdminBL();

    @FXML
    public void initialize() {
        if (SessionData.currentClubAdmin == null) return;

        int adminId = SessionData.currentClubAdmin.getUserId();
        Club club = adminController.getManagedClub(adminId);

        if (club == null) {
            lblClubName.setText("No club assigned.");
            lblClubDesc.setText("Ask system admin to assign you a club.");
            lblMembers.setText("");
            lblPending.setText("");
            return;
        }

        lblClubName.setText(club.getClubName());
        lblClubDesc.setText(club.getDescription());

        ArrayList<Student> members = adminController.getClubMembers(club.getClubId());
        ArrayList<Student> pending = adminController.getPendingRequests(club.getClubId());

        lblMembers.setText("Members: " + members.size());
        lblPending.setText("Pending join requests: " + pending.size());
    }
}
