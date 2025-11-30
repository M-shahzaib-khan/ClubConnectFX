package dao;

import model.Student;
import java.sql.*;
import java.util.ArrayList;

public class WaitlistDAO {

    // Add student to waitlist
    public boolean addToWaitlist(int eventId, int studentId) {
        String sql = "INSERT INTO event_waitlist (event_id, student_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, studentId);

            stmt.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            // student is already on waitlist
            return false;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Check if student is already waitlisted
    public boolean isWaitlisted(int eventId, int studentId) {
        String sql = "SELECT * FROM event_waitlist WHERE event_id=? AND student_id=? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, studentId);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true; // Safe default
    }

    // Count waitlisted students
    public int getWaitlistCount(int eventId) {
        String sql = "SELECT COUNT(*) FROM event_waitlist WHERE event_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Remove a student from waitlist
    public boolean removeFromWaitlist(int eventId, int studentId) {
        String sql = "DELETE FROM event_waitlist WHERE event_id=? AND student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, studentId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Get all waitlisted students for admin view
    public ArrayList<Student> getWaitlistedStudents(int eventId) {
        ArrayList<Student> list = new ArrayList<>();

        String sql = """
            SELECT s.student_id, s.name, s.email, s.department, s.semester
            FROM event_waitlist ew
            JOIN students s ON ew.student_id = s.student_id
            WHERE ew.event_id=?
            ORDER BY ew.waitlisted_at ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getInt("semester")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
