package dao;

import java.sql.*;
import java.util.ArrayList;

public class ClubAdminDAO {

    private static final String INSERT_REQUEST = """
                INSERT INTO club_admin_requests
                (username, password, full_name, email, club_name, club_description)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_PENDING = """
                SELECT * FROM club_admin_requests WHERE status = 'pending'
            """;

    private static final String UPDATE_STATUS = """
                UPDATE club_admin_requests SET status=? WHERE request_id=?
            """;

    // CREATE REQUEST
    public boolean createAdminRequest(String username, String password,
                                      String fullName, String email,
                                      String clubName, String clubDesc) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_REQUEST)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, clubName);
            stmt.setString(6, clubDesc);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // GET ALL PENDING REQUESTS
    public ArrayList<AdminRequest> getPendingRequests() {
        ArrayList<AdminRequest> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_PENDING);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new AdminRequest(
                        rs.getInt("request_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("club_name"),
                        rs.getString("club_description"),
                        rs.getString("status"),
                        rs.getTimestamp("requested_at")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE STATUS
    public boolean updateRequestStatus(int requestId, String status) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STATUS)) {

            stmt.setString(1, status);
            stmt.setInt(2, requestId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // INNER CLASS
    public static class AdminRequest {
        public int requestId;
        public String username, password, fullName, email, clubName, clubDesc, status;
        public Timestamp requestedAt;

        public AdminRequest(int requestId, String username, String password,
                            String fullName, String email,
                            String clubName, String clubDesc,
                            String status, Timestamp requestedAt) {
            this.requestId = requestId;
            this.username = username;
            this.password = password;
            this.fullName = fullName;
            this.email = email;
            this.clubName = clubName;
            this.clubDesc = clubDesc;
            this.status = status;
            this.requestedAt = requestedAt;
        }
    }

    public int getManagedClubId(int adminId) {
        String sql = "SELECT club_id FROM club_admins WHERE admin_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, adminId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("club_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0; // means admin has no club assigned yet
    }

}
