package app.clubconnectfx;

import BL.StudentBL;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Event;
import model.Student;

import java.util.List;

public class StudentRegistrationsController {

    @FXML private TableView<Event> regTable;
    @FXML private TableColumn<Event, Integer> colEventId;
    @FXML private TableColumn<Event, String> colName;
    @FXML private TableColumn<Event, String> colStart;
    @FXML private TableColumn<Event, String> colEnd;
    @FXML private TableColumn<Event, String> colVenue;

    private final StudentBL studentBL = new StudentBL();

    @FXML
    public void initialize() {

        colEventId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(
                data.getValue().getEventId()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getEventName()));
        colStart.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getStartTime())));
        colEnd.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getEndTime())));
        colVenue.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getVenue()));

        loadRegistrations();
    }

    private void loadRegistrations() {
        Student s = SessionData.currentStudent;
        List<Event> list = studentBL.getRegisteredEvents(s.getUserId());
        regTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    private void cancelRegistration() {
        Event e = regTable.getSelectionModel().getSelectedItem();
        if (e == null) return;

        Student s = SessionData.currentStudent;

        boolean ok = studentBL.cancelEventRegistration(e.getEventId(), s.getUserId());

        new Alert(Alert.AlertType.INFORMATION,
                ok ? "Registration cancelled." : "Unable to cancel.").show();

        loadRegistrations();
    }
}
