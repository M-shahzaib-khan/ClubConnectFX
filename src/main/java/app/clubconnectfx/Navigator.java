package app.clubconnectfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigator {

    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
        stage.setTitle("Club Connect - JavaFX Version");
        stage.show();
    }

    private static void load(String file) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Navigator.class.getResource(file)
            );
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================
    // LOGIN & REGISTER
    // ======================
    public static void loadLogin() {
        load("LoginView.fxml");
    }

    public static void loadRegister() {
        load("RegisterView.fxml");
    }

    // ======================
    // REGISTER SUB-FORMS
    // ======================
    public static void loadStudentRegister() {
        load("StudentRegister.fxml");
    }

    public static void loadAdminRegister() {
        load("AdminRegister.fxml");
    }

    // ======================
    // DASHBOARDS
    // ======================
    public static void loadDashboard(String role) {
        switch (role.toLowerCase()) {
            case "student":
                load("Student-Dashboard.fxml");
                break;

            case "club_admin":
                load("Admin-Dashboard.fxml");
                break;

            case "system_admin":
                load("System-admin-Dashboard.fxml");
                break;

            default:
                loadLogin(); // fallback
        }
    }

    public static void logout() {
        loadLogin();
    }
}
