package app.clubconnectfx;

import BL.StudentBL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Student;

public class StudentProfileController {

    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblDepartment;
    @FXML private Label lblSemester;

    private final StudentBL studentBL = new StudentBL();

    @FXML
    public void initialize() {
        Student student = SessionData.currentStudent;  // from Login
        if (student != null) {
            lblName.setText(student.getName());
            lblEmail.setText(student.getEmail());
            lblDepartment.setText(student.getDepartment());
            lblSemester.setText(String.valueOf(student.getSemester()));
        }
    }
}
