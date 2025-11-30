package app.clubconnectfx;

import dao.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;

import java.util.ArrayList;

public class SystemUsersController {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> colId;

    @FXML
    private TableColumn<User, String> colUsername;

    @FXML
    private TableColumn<User, String> colRole;

    @FXML
    private TableColumn<User, String> colEmail;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        setupColumns();
        loadUsers();
    }

    private void setupColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadUsers() {
        ArrayList<User> all = userDAO.getAllUsers();
        ObservableList<User> list = FXCollections.observableArrayList(all);
        usersTable.setItems(list);
    }
}
