package app.clubconnectfx;

import BL.ClubBL;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Club;
import model.Student;

import java.util.ArrayList;

public class StudentClubsController {

    @FXML private ListView<Club> clubList;
    @FXML private TextArea detailsArea;
    @FXML private TextField searchField;

    private ClubBL clubBL = new ClubBL();
    private Student loggedStudent; // Set from Dashboard

    @FXML
    public void initialize() {
        loadClubs();

        clubList.getSelectionModel().selectedItemProperty().addListener((obs, old, club) -> {
            if (club != null) showDetails(club);
        });
    }

    public void setStudent(Student s) {
        this.loggedStudent = s;
    }

    private void loadClubs() {
        ArrayList<Club> clubs = clubBL.getAllClubs();
        clubList.getItems().setAll(clubs);
    }

    private void showDetails(Club club) {
        detailsArea.setText(
                "Club: " + club.getClubName() + "\n\n" +
                        "Description:\n" + club.getDescription()
        );
    }

    @FXML
    private void searchClubs() {
        String keyword = searchField.getText().trim().toLowerCase();
        ArrayList<Club> filtered = clubBL.searchClubs(keyword);
        clubList.getItems().setAll(filtered);
    }

    @FXML
    private void sendJoinRequest() {
        Club club = clubList.getSelectionModel().getSelectedItem();

        if (club == null) {
            showAlert("Please select a club.");
            return;
        }

        boolean ok = clubBL.sendJoinRequest(club.getClubId(), loggedStudent.getUserId());

        showAlert(ok ? "Request Sent!" : "Request already sent or you're already a member.");
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }
}
