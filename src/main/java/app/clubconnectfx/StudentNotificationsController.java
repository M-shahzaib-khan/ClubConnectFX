package app.clubconnectfx;

import dao.NotificationDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Notification;
import model.Student;

import java.util.List;

public class StudentNotificationsController {

    @FXML private TableView<Notification> notifTable;
    @FXML private TableColumn<Notification, String> colMessage;
    @FXML private TableColumn<Notification, String> colTime;
    @FXML private TableColumn<Notification, Boolean> colStatus;

    private final NotificationDAO notificationDAO = new NotificationDAO();

    @FXML
    public void initialize() {

        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("read"));

        loadNotifications();
    }

    private void loadNotifications() {
        Student s = SessionData.currentStudent;
        List<Notification> list = notificationDAO.getAllNotifications(s.getUserId());
        notifTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    private void markAllAsRead() {
        Student s = SessionData.currentStudent;
        notificationDAO.markAllAsRead(s.getUserId());
        loadNotifications();
    }
}
