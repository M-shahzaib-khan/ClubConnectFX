package dao;

import model.Notification;

import java.sql.*;
import java.util.ArrayList;

public class NotificationDAO {

    // Send a notification to a user
    public boolean sendNotification(int userId, String message) {
        String sql = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, message);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // Get unread notifications for a user
    public ArrayList<Notification> getUnreadNotifications(int userId) {
        ArrayList<Notification> list = new ArrayList<>();

        String sql = """
            SELECT * FROM notifications
            WHERE user_id=? AND is_read=FALSE
            ORDER BY created_at DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        rs.getTimestamp("created_at"),
                        rs.getBoolean("is_read")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Mark a notification as read
    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read=TRUE WHERE notification_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Mark all notifications of a user as read
    public boolean markAllAsRead(int userId) {
        String sql = "UPDATE notifications SET is_read=TRUE WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteNotificationsForUser(int userId) {
        String sql = "DELETE FROM notifications WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // Get ALL notifications (read + unread)
    public ArrayList<Notification> getAllNotifications(int userId) {
        ArrayList<Notification> list = new ArrayList<>();

        String sql = """
            SELECT * FROM notifications
            WHERE user_id=?
            ORDER BY created_at DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        rs.getTimestamp("created_at"),
                        rs.getBoolean("is_read")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


}
