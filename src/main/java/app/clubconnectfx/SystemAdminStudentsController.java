package app.clubconnectfx;

import BL.SystemAdminBL;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Student;

public class SystemAdminStudentsController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colDept;
    @FXML private TableColumn<Student, String> colSem;

    private final SystemAdminBL controller = new SystemAdminBL();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDept.setCellValueFactory(new PropertyValueFactory<>("department"));
        colSem.setCellValueFactory(new PropertyValueFactory<>("semester"));

        loadStudents();
    }

    private void loadStudents() {
        studentTable.getItems().setAll(controller.getAllStudents());
    }

    @FXML
    private void deleteStudent() {
        Student s = studentTable.getSelectionModel().getSelectedItem();
        if (s == null) {
            alert("Please select a student.");
            return;
        }

        if (controller.deleteUser(s.getUserId())) {
            alert("Student deleted.");
            loadStudents();
        } else alert("Delete failed.");
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
