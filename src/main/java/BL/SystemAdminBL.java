package BL;

import dao.*;
import model.*;
import java.sql.*;
import model.ClubAdmin;

import java.util.ArrayList;

public class SystemAdminBL {

    private UserDAO userDAO = new UserDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private ClubDAO clubDAO = new ClubDAO();
    private ClubJoinRequestDAO requestDAO = new ClubJoinRequestDAO();
    private EventDAO eventDAO = new EventDAO();
    private RegistrationDAO regDAO = new RegistrationDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();
    private ClubAdminDAO clubAdminDAO = new ClubAdminDAO();

    // =====================================================
    // USERS
    // =====================================================
    public ArrayList<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean deleteUser(int userId) {
        notificationDAO.markAllAsRead(userId);
        return userDAO.deleteUser(userId);
    }

    public ArrayList<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    // =====================================================
    // CLUBS
    // =====================================================
    public ArrayList<Club> getAllClubs() {
        return clubDAO.getAllClubs();
    }

    public int createClub(String name, String description, int adminUserId) {
        return clubDAO.createClub(name, description, adminUserId);
    }

    public boolean deleteClub(int clubId) {

        requestDAO.deleteRequestsForClub(clubId);

        ArrayList<Event> events = eventDAO.getEventsByClub(clubId);

        for (Event e : events) {
            regDAO.deleteAllRegistrationsForEvent(e.getEventId());
            regDAO.deleteAllWaitlistForEvent(e.getEventId());
        }

        eventDAO.deleteEventsByClub(clubId);
        clubDAO.deleteAllMembers(clubId);

        return clubDAO.deleteClub(clubId);
    }

    // =====================================================
    // EVENTS
    // =====================================================
    public ArrayList<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    public boolean deleteEvent(int eventId) {
        regDAO.deleteAllRegistrationsForEvent(eventId);
        regDAO.deleteAllWaitlistForEvent(eventId);
        return eventDAO.deleteEvent(eventId);
    }


    public ArrayList<ClubAdminDAO.AdminRequest> getPendingAdminRequests() {
        return clubAdminDAO.getPendingRequests();
    }

    public boolean approveAdminRequest(ClubAdminDAO.AdminRequest req) {

        // Step 1: Create user account
        int newUserId = userDAO.createUserFull(
                req.username,
                req.password,
                "club_admin",
                ""
        );

        if (newUserId == -1) return false;

        // Step 2: Create new club
        int clubId = clubDAO.createClub(req.clubName, req.clubDesc, newUserId);

        if (clubId <= 0) return false;

        // Step 3: Assign admin to created club
        clubDAO.assignAdminToClub(newUserId, clubId);

        // Step 4: Update request status
        clubAdminDAO.updateRequestStatus(req.requestId, "approved");

        return true;
    }

    /** 3️⃣ REJECT ADMIN REQUEST */
    public boolean rejectAdminRequest(int requestId) {
        return clubAdminDAO.updateRequestStatus(requestId, "rejected");
    }

    // =====================================================
    // NOTIFICATIONS
    // =====================================================
    public void sendNotificationToUser(int userId, String message) {
        notificationDAO.sendNotification(userId, message);
    }

    public void broadcastToAll(String message) {
        ArrayList<User> all = userDAO.getAllUsers();

        for (User u : all) {
            notificationDAO.sendNotification(u.getUserId(),
                    "[Broadcast] " + message);
        }
    }

    public ArrayList<ClubAdmin> getAllAdmins() {

        ArrayList<ClubAdmin> list = new ArrayList<>();

        String sql = """
        SELECT 
            ca.admin_id,
            u.username,
            u.password,
            ca.name,
            ca.email,
            ca.club_id
        FROM club_admins ca
        JOIN users u ON ca.admin_id = u.user_id
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                ClubAdmin admin = new ClubAdmin(
                        rs.getInt("admin_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("club_id")
                );

                list.add(admin);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }




}
