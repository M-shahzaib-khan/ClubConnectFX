package app.clubconnectfx;

import BL.AuthBL;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import model.Student;
import model.ClubAdmin;
import model.SystemAdmin;
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private AuthBL authBL = new AuthBL();

    @FXML
    private void handleLogin() {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        User user = authBL.login(username, password);

        if (user != null) {
            if (user instanceof Student) {
                SessionData.currentStudent = (Student) user;
            } else if (user instanceof ClubAdmin) {
                SessionData.currentClubAdmin = (ClubAdmin) user;
            } else if (user instanceof SystemAdmin) {
                SessionData.currentSystemAdmin = (SystemAdmin) user;
            }

            Navigator.loadDashboard(user.getRole());
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid username or password").show();
        }
    }


    @FXML
    private void goToRegister() {
        Navigator.loadRegister();
    }
}
