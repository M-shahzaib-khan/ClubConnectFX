package dao;

import java.sql.*;

public class RegistrationDAO {

    // =======================================
    // CHECK IF STUDENT REGISTERED
    // =======================================
    public boolean isRegistered(int eventId, int studentId) {

        String sql = "SELECT 1 FROM event_registrations WHERE event_id=? AND student_id=? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, studentId);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // COUNT REGISTRATIONS
    // =======================================
    public int getRegistrationCount(int eventId) {

        String sql = "SELECT COUNT(*) FROM event_registrations WHERE event_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }

        return 999999; // safe fallback
    }

    // =======================================
    // REGISTER STUDENT
    // =======================================
    public boolean registerStudent(int eventId, int studentId) {

        String sql = "INSERT INTO event_registrations (event_id, student_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, studentId);

            stmt.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException dup) {
            return false; // already registered
        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // CANCEL REGISTRATION
    // =======================================
    public boolean cancelRegistration(int eventId, int studentId) {

        String sql = "DELETE FROM event_registrations WHERE event_id=? AND student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, studentId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // ADD TO WAITLIST
    // =======================================
    public boolean addToWaitlist(int eventId, int studentId) {

        String sql = "INSERT INTO event_waitlist (event_id, student_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, studentId);

            stmt.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException dup) {
            return false; // already waitlisted
        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // CHECK WAITLIST
    // =======================================
    public boolean isWaitlisted(int eventId, int studentId) {

        String sql = "SELECT 1 FROM event_waitlist WHERE event_id=? AND student_id=? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, studentId);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // DELETE ALL REGISTRATIONS FOR EVENT
    // =======================================
    public boolean deleteAllRegistrationsForEvent(int eventId) {

        String sql = "DELETE FROM event_registrations WHERE event_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // DELETE ALL WAITLIST FOR EVENT
    // =======================================
    public boolean deleteAllWaitlistForEvent(int eventId) {

        String sql = "DELETE FROM event_waitlist WHERE event_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // GET NEXT WAITLISTED STUDENT
    // =======================================
    public Integer getNextWaitlistedStudent(int eventId) {

        String sql = """
            SELECT student_id 
            FROM event_waitlist 
            WHERE event_id=?
            ORDER BY waitlisted_at ASC 
            LIMIT 1
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("student_id");

        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    // =======================================
    // PROMOTE ONE STUDENT FROM WAITLIST
    // =======================================
    public boolean promoteNextWaitlistedStudent(int eventId) {

        Integer studentId = getNextWaitlistedStudent(eventId);
        if (studentId == null) return false;

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            // 1. Remove from waitlist
            String remove = "DELETE FROM event_waitlist WHERE event_id=? AND student_id=?";
            PreparedStatement r = conn.prepareStatement(remove);
            r.setInt(1, eventId);
            r.setInt(2, studentId);
            r.executeUpdate();

            // 2. Add to registrations
            String add = "INSERT INTO event_registrations (event_id, student_id) VALUES (?, ?)";
            PreparedStatement a = conn.prepareStatement(add);
            a.setInt(1, eventId);
            a.setInt(2, studentId);
            a.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
