package dao;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import model.Student;
public class UserDAO {

    // ===============================
    // LOGIN
    // ===============================
    public User login(String username, String password) {

        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapUser(conn, rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ===============================
    // GET USER BY ID
    // ===============================
    public User getUserById(int userId) {

        String sql = "SELECT * FROM users WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapUser(conn, rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ===============================
    // MAP USER ROLE TO MODEL
    // ===============================
    private User mapUser(Connection conn, ResultSet rs) throws Exception {

        int userId = rs.getInt("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String role = rs.getString("role");

        switch (role) {

            case "student": {
                String sql2 = "SELECT * FROM students WHERE student_id=?";
                PreparedStatement ps = conn.prepareStatement(sql2);
                ps.setInt(1, userId);
                ResultSet srs = ps.executeQuery();

                if (srs.next()) {
                    return new Student(
                            userId,
                            username,
                            password,
                            srs.getString("name"),
                            srs.getString("email"),
                            srs.getString("department"),
                            srs.getInt("semester")
                    );
                }
                break;
            }

            case "club_admin": {
                String sql = "SELECT * FROM club_admins WHERE admin_id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, userId);
                ResultSet ars = ps.executeQuery();

                int managedClub = 0;
                if (ars.next()) managedClub = ars.getInt("club_id");

                return new ClubAdmin(
                        userId,
                        username,
                        password,
                        username,   // or fetch real name if stored somewhere
                        "",         // email also missing unless stored
                        managedClub
                );
            }


            case "system_admin": {
                return new SystemAdmin(
                        userId,
                        username,
                        password,
                        "System Admin",
                        ""
                );
            }
        }

        return null;
    }

    public int createUserFull(String username, String password, String role , String email) {

        String sql = "INSERT INTO users (username, password,email, role) VALUES (?,?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, role);

            int affected = stmt.executeUpdate();
            if (affected == 0) return -1;

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLIntegrityConstraintViolationException dup) {
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }



    // ===============================
    // CREATE NEW USER
    // ===============================
    public boolean createUser(String username, String password, String role) {

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===============================
    // DELETE USER
    // ===============================
    public boolean deleteUser(int userId) {

        String sql = "DELETE FROM users WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===============================
    // GET ALL USERS BY ROLE
    // ===============================
    public ArrayList<User> getUsersByRole(String role) {

        ArrayList<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User u = mapUser(conn, rs);
                if (u != null) list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===============================
    // GET ALL USERS
    // ===============================
    public ArrayList<User> getAllUsers() {

        ArrayList<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User u = mapUser(conn, rs);
                if (u != null) list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
