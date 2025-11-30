package app.clubconnectfx;

import BL.ClubAdminBL;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Club;
import model.ClubAdmin;
import model.Event;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AdminEventsController {

    @FXML private TableView<Event> eventsTable;
    @FXML private TableColumn<Event, Integer> colId;
    @FXML private TableColumn<Event, String> colName;
    @FXML private TableColumn<Event, String> colStart;
    @FXML private TableColumn<Event, String> colEnd;
    @FXML private TableColumn<Event, String> colVenue;
    @FXML private TableColumn<Event, Integer> colCap;
    @FXML private TableColumn<Event, Integer> colReg;

    private final ClubAdminBL adminController = new ClubAdminBL();
    private Club managedClub;
    private ClubAdmin admin;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEventId()).asObject());
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEventName()));
        colStart.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStartTime())));
        colEnd.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEndTime())));
        colVenue.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVenue()));
        colCap.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCapacity()).asObject());
        colReg.setCellValueFactory(data ->
                new SimpleIntegerProperty(adminController.getRegistrationCount(data.getValue().getEventId())).asObject()
        );

        admin = SessionData.currentClubAdmin;
        if (admin != null) {
            managedClub = adminController.getManagedClub(admin.getUserId());
        }

        loadEvents();
    }

    private void loadEvents() {
        if (managedClub == null) return;
        ArrayList<Event> events = adminController.getEventsByClub(managedClub.getClubId());
        eventsTable.setItems(FXCollections.observableArrayList(events));
    }

    @FXML
    private void deleteSelected() {
        Event e = eventsTable.getSelectionModel().getSelectedItem();
        if (e == null) return;

        boolean ok = adminController.deleteEvent(e.getEventId());
        showMsg(ok ? "Event deleted." : "Delete failed.");
        loadEvents();
    }

    @FXML
    private void openCreateDialog() {
        if (managedClub == null || admin == null) {
            showMsg("No club assigned.");
            return;
        }

        Dialog<Event> dialog = new Dialog<>();
        dialog.setTitle("Create Event");

        Label nameL = new Label("Name:");
        TextField nameF = new TextField();
        Label descL = new Label("Description:");
        TextField descF = new TextField();
        Label venueL = new Label("Venue:");
        TextField venueF = new TextField();
        Label capL = new Label("Capacity:");
        TextField capF = new TextField();
        Label durL = new Label("Duration (hours):");
        TextField durF = new TextField("2");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, nameL, nameF);
        grid.addRow(1, descL, descF);
        grid.addRow(2, venueL, venueF);
        grid.addRow(3, capL, capF);
        grid.addRow(4, durL, durF);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    String name = nameF.getText().trim();
                    String desc = descF.getText().trim();
                    String venue = venueF.getText().trim();
                    int cap = Integer.parseInt(capF.getText().trim());
                    int hours = Integer.parseInt(durF.getText().trim());

                    Timestamp start = Timestamp.valueOf(LocalDateTime.now().plusHours(1));
                    Timestamp end = Timestamp.valueOf(start.toLocalDateTime().plusHours(hours));
                    java.sql.Date eventDate = new java.sql.Date(start.getTime());

                    return new Event(
                            0, name, desc, eventDate, start, end,
                            venue, cap, managedClub.getClubId(), admin.getUserId()
                    );
                } catch (Exception ex) {
                    showMsg("Invalid input.");
                    return null;
                }
            }
            return null;
        });

        Event created = dialog.showAndWait().orElse(null);
        if (created != null) {
            boolean ok = adminController.createEvent(created);
            showMsg(ok ? "Event created!" : "Event conflict or error.");
            loadEvents();
        }
    }

    private void showMsg(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
