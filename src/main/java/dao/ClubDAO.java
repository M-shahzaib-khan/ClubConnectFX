package dao;

import model.Club;
import model.Student;

import java.sql.*;
import java.util.ArrayList;

public class ClubDAO {

    // =====================================================
    // CREATE CLUB → returns generated club_id
    // =====================================================
    public int createClub(String name, String description, int createdBy) {

        String sql = """
            INSERT INTO clubs (club_name, description, created_by)
            VALUES (?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, createdBy);

            int rows = stmt.executeUpdate();
            if (rows == 0) return -1;

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // return club_id
            }

        } catch (SQLIntegrityConstraintViolationException ex) {
            System.err.println("Duplicate club name!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // =====================================================
    // GET CLUB BY ID
    // =====================================================
    public Club getClubById(int clubId) {

        String sql = "SELECT * FROM clubs WHERE club_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Club(
                        rs.getInt("club_id"),
                        rs.getString("club_name"),
                        rs.getString("description"),
                        rs.getInt("created_by")
                );
            }

        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    // =====================================================
    // GET ALL CLUBS
    // =====================================================
    public ArrayList<Club> getAllClubs() {

        ArrayList<Club> list = new ArrayList<>();
        String sql = "SELECT * FROM clubs";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Club(
                        rs.getInt("club_id"),
                        rs.getString("club_name"),
                        rs.getString("description"),
                        rs.getInt("created_by")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // =====================================================
    // GET CLUB BY ADMIN
    // =====================================================
    public Club getClubByAdmin(int adminUserId) {

        String sql = """
            SELECT c.*
            FROM club_admins ca
            JOIN clubs c ON ca.club_id = c.club_id
            WHERE ca.admin_id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, adminUserId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Club(
                        rs.getInt("club_id"),
                        rs.getString("club_name"),
                        rs.getString("description"),
                        rs.getInt("created_by")
                );
            }

        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    // =====================================================
    // GET CLUB MEMBERS
    // =====================================================
    public ArrayList<Student> getClubMembers(int clubId) {

        ArrayList<Student> list = new ArrayList<>();

        String sql = """
            SELECT s.*
            FROM club_members cm
            JOIN students s ON cm.student_id = s.student_id
            WHERE cm.club_id = ?
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

    // =====================================================
    // ADD MEMBER
    // =====================================================
    public boolean addMember(int clubId, int studentId) {

        String sql = "INSERT IGNORE INTO club_members (club_id, student_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            stmt.setInt(2, studentId);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =====================================================
    // REMOVE MEMBER
    // =====================================================
    public boolean removeMember(int clubId, int studentId) {

        String sql = "DELETE FROM club_members WHERE club_id=? AND student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            stmt.setInt(2, studentId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =====================================================
    // CHECK MEMBERSHIP
    // =====================================================
    public boolean isMember(int clubId, int studentId) {

        String sql = "SELECT 1 FROM club_members WHERE club_id=? AND student_id=? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            stmt.setInt(2, studentId);

            return stmt.executeQuery().next();

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =====================================================
    // DELETE ALL MEMBERS (club deletion cleanup)
    // =====================================================
    public boolean deleteAllMembers(int clubId) {

        String sql = "DELETE FROM club_members WHERE club_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =====================================================
    // DELETE CLUB
    // =====================================================
    public boolean deleteClub(int clubId) {

        String sql = "DELETE FROM clubs WHERE club_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =====================================================
    // ASSIGN ADMIN TO CLUB
    // =====================================================
    public boolean assignAdminToClub(int adminId, int clubId) {

        String sql = "INSERT INTO club_admins (admin_id, club_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, adminId);
            stmt.setInt(2, clubId);

            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Admin already assigned to a club!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
