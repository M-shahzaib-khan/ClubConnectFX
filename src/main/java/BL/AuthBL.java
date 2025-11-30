package BL;

import dao.UserDAO;
import dao.StudentDAO;
import dao.ClubAdminDAO;

import model.User;
import model.Student;

public class AuthBL {

    private UserDAO userDAO = new UserDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private ClubAdminDAO clubAdminDAO = new ClubAdminDAO();

    // ---------------------------
    // LOGIN
    // ---------------------------
    public User login(String username, String password) {
        return userDAO.login(username, password);
    }

    // ---------------------------
    // REGISTER STUDENT
    // ---------------------------
    public boolean registerStudent(String username, String password,
                                   String name, String email,
                                   String studentId, String department) {

        int userId = userDAO.createUserFull(username, password, "student" , email);
        if (userId == -1) return false;

        Student s = new Student(
                userId,
                name,
                email,
                department,
                1 // default semester
        );

        return studentDAO.createStudent(s);
    }

    // ---------------------------
    // REGISTER CLUB ADMIN (REQUEST)
    // ---------------------------
    public boolean registerClubAdmin(String username, String password,
                                     String name, String email,
                                     String clubName, String clubDesc) {

        // Do NOT create a user yet—only request
        return clubAdminDAO.createAdminRequest(
                username, password, name, email, clubName, clubDesc
        );
    }

    // ---------------------------
    // LOAD DASHBOARD
    // ---------------------------
    public Object loadUserDashboardData(User user) {

        switch (user.getRole()) {
            case "student":
                return studentDAO.getStudentById(user.getUserId());

            case "club_admin":
                return clubAdminDAO.getManagedClubId(user.getUserId());

            case "system_admin":
                return "system_admin";

            default:
                return null;
        }
    }
}
