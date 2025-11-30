package app.clubconnectfx;
import javafx.scene.control.cell.PropertyValueFactory;

import BL.EventBL;
import BL.StudentBL;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Event;
import model.Student;

import java.util.ArrayList;

public class StudentEventsController {

    @FXML private TableView<Event> eventTable;

    @FXML private TableColumn<Event, String> colName;
    @FXML private TableColumn<Event, String> colDate;
    @FXML private TableColumn<Event, String> colStart;
    @FXML private TableColumn<Event, String> colEnd;
    @FXML private TableColumn<Event, String> colVenue;
    @FXML private TableColumn<Event, Number> colCapacity;

    @FXML private TextField searchField;

    private EventBL eventBL = new EventBL();
    private StudentBL studentBL = new StudentBL();

    private Student loggedStudent;

    @FXML
    public void initialize() {

        colName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        loadEvents();
    }


    /** Set the logged student from dashboard */
    public void setStudent(Student s) {
        this.loggedStudent = s;
    }

    /** Load all events from DB */
    private void loadEvents() {
        ArrayList<Event> events = eventBL.getAllEvents();
        eventTable.setItems(FXCollections.observableArrayList(events));
    }

    /** Search bar */
    @FXML
    private void searchEvents() {
        String keyword = searchField.getText().trim().toLowerCase();
        ArrayList<Event> filtered = eventBL.searchEvents(keyword);
        eventTable.setItems(FXCollections.observableArrayList(filtered));
    }

    /** Register student for selected event */
    @FXML
    private void registerForEvent() {
        Event event = eventTable.getSelectionModel().getSelectedItem();

        if (event == null) {
            showAlert("Please select an event.");
            return;
        }

        String result = studentBL.registerForEvent(event, loggedStudent.getUserId());

        switch (result) {
            case "registered":
                showAlert("Successfully registered!");
                break;

            case "waitlisted":
                showAlert("Event full. You were added to the waitlist.");
                break;

            case "already_registered":
                showAlert("You are already registered.");
                break;

            default:
                showAlert("Registration failed.");
        }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }



}
