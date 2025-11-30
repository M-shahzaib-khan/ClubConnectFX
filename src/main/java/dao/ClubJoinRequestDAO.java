package dao;

import model.Student;

import java.sql.*;
import java.util.ArrayList;

public class ClubJoinRequestDAO {

    // =======================================
    // SEND JOIN REQUEST
    // =======================================
    public boolean sendRequest(int clubId, int studentId) {

        String sql = """
            INSERT INTO club_join_requests (club_id, student_id)
            VALUES (?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            stmt.setInt(2, studentId);

            stmt.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException dup) {
            // unique(club_id, student_id) → already requested
            return false;
        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // CHECK IF PENDING REQUEST EXISTS
    // =======================================
    public boolean hasPendingRequest(int clubId, int studentId) {

        String sql = """
            SELECT 1 
            FROM club_join_requests
            WHERE club_id=? AND student_id=? AND status='pending'
            LIMIT 1
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            stmt.setInt(2, studentId);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }



    // =======================================
    // GET ALL PENDING REQUESTS FOR A CLUB
    // =======================================
    public ArrayList<Student> getPendingRequests(int clubId) {

        ArrayList<Student> list = new ArrayList<>();

        String sql = """
            SELECT s.student_id, s.name, s.email, s.department, s.semester
            FROM club_join_requests r
            JOIN students s ON r.student_id = s.student_id
            WHERE r.club_id=? AND r.status='pending'
            ORDER BY r.requested_at ASC
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);

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

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // =======================================
    // APPROVE REQUEST
    // =======================================
    public boolean approveRequest(int clubId, int studentId) {

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            // 1. update request status
            String update = """
                UPDATE club_join_requests
                SET status='approved'
                WHERE club_id=? AND student_id=? AND status='pending'
                """;

            PreparedStatement u = conn.prepareStatement(update);
            u.setInt(1, clubId);
            u.setInt(2, studentId);
            u.executeUpdate();

            // 2. add to club members
            String add = """
                INSERT IGNORE INTO club_members (club_id, student_id)
                VALUES (?, ?)
                """;

            PreparedStatement a = conn.prepareStatement(add);
            a.setInt(1, clubId);
            a.setInt(2, studentId);
            a.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // REJECT REQUEST
    // =======================================
    public boolean rejectRequest(int clubId, int studentId) {

        String sql = """
            UPDATE club_join_requests
            SET status='rejected'
            WHERE club_id=? AND student_id=? AND status='pending'
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            stmt.setInt(2, studentId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // DELETE ALL REQUESTS FOR CLUB (system admin cleanup)
    // =======================================
    public boolean deleteRequestsForClub(int clubId) {

        String sql = "DELETE FROM club_join_requests WHERE club_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }
}
