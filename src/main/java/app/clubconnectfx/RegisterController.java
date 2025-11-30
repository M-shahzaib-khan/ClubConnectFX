package app.clubconnectfx;

import BL.AuthBL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class RegisterController {

    @FXML private ToggleButton studentToggle;
    @FXML private ToggleButton adminToggle;
    @FXML private ToggleGroup roleToggle;

    @FXML private StackPane formContainer;
    @FXML private Label errorLabel;

    private Node studentForm;
    private Node adminForm;

    private AuthBL authBL = new AuthBL();

    @FXML
    public void initialize() {
        loadForms();

        // Default: Student form
        formContainer.getChildren().setAll(studentForm);

        roleToggle.selectedToggleProperty().addListener((obs, old, newVal) -> {
            if (newVal == studentToggle) {
                formContainer.getChildren().setAll(studentForm);
            } else {
                formContainer.getChildren().setAll(adminForm);
            }
        });
    }

    private void loadForms() {
        try {
            studentForm = FXMLLoader.load(getClass().getResource("StudentRegister.fxml"));
            adminForm = FXMLLoader.load(getClass().getResource("AdminRegister.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        try {
            if (studentToggle.isSelected()) {
                registerStudent();
            } else {
                registerAdmin();
            }
        } catch (Exception e) {
            showError("Invalid input. Please check again.");
            e.printStackTrace();
        }
    }

    private void registerStudent() {
        TextField u = (TextField) studentForm.lookup("#sUsername");
        PasswordField p = (PasswordField) studentForm.lookup("#sPassword");
        TextField n = (TextField) studentForm.lookup("#sName");
        TextField e = (TextField) studentForm.lookup("#sEmail");
        TextField d = (TextField) studentForm.lookup("#sDepartment");
        TextField sem = (TextField) studentForm.lookup("#sSemester");

        boolean ok = authBL.registerStudent(
                u.getText(), p.getText(), n.getText(), e.getText(), sem.getText(), d.getText()
        );

        if (!ok) {
            showError("Registration failed. Username or student ID may already exist.");
        } else {
            Navigator.loadLogin();
        }
    }

    private void registerAdmin() {
        TextField u = (TextField) adminForm.lookup("#aUsername");
        PasswordField p = (PasswordField) adminForm.lookup("#aPassword");
        TextField n = (TextField) adminForm.lookup("#aName");
        TextField e = (TextField) adminForm.lookup("#aEmail");
        TextField cn = (TextField) adminForm.lookup("#aClubName");
        TextField cd = (TextField) adminForm.lookup("#aClubDesc");

        boolean ok = authBL.registerClubAdmin(
                u.getText(), p.getText(), n.getText(), e.getText(), cn.getText(), cd.getText()
        );

        if (!ok) {
            showError("Username already exists.");
        } else {
            Navigator.loadLogin();
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    @FXML
    private void goToLogin() {
        Navigator.loadLogin();
    }
}
