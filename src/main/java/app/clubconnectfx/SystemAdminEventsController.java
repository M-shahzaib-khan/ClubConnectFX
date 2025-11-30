package app.clubconnectfx;

import BL.SystemAdminBL;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Event;

public class SystemAdminEventsController {

    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> colId;
    @FXML private TableColumn<Event, String> colName;
    @FXML private TableColumn<Event, Integer> colClub;
    @FXML private TableColumn<Event, Object> colDate;
    @FXML private TableColumn<Event, Object> colStart;
    @FXML private TableColumn<Event, Object> colEnd;
    @FXML private TableColumn<Event, String> colVenue;
    @FXML private TableColumn<Event, Integer> colCap;

    private final SystemAdminBL controller = new SystemAdminBL();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        colClub.setCellValueFactory(new PropertyValueFactory<>("clubId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        colCap.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        loadEvents();
    }

    private void loadEvents() {
        eventTable.getItems().setAll(controller.getAllEvents());
    }

    @FXML
    private void deleteEvent() {
        Event e = eventTable.getSelectionModel().getSelectedItem();
        if (e == null) {
            alert("Select an event first.");
            return;
        }

        if (controller.deleteEvent(e.getEventId())) {
            alert("Event deleted.");
            loadEvents();
        } else alert("Failed.");
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
