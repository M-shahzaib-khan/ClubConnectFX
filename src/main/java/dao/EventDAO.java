package dao;

import model.Event;

import java.sql.*;
import java.util.ArrayList;

public class EventDAO {

    // ===========================================
    // CREATE EVENT
    // ===========================================
    public boolean createEvent(Event event) {

        String sql = """
            INSERT INTO events 
            (event_name, description, event_date, start_time, end_time, 
             venue, capacity, club_id, created_by)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getDescription());
            stmt.setDate(3, event.getEventDate());
            stmt.setTimestamp(4, event.getStartTime());
            stmt.setTimestamp(5, event.getEndTime());
            stmt.setString(6, event.getVenue());
            stmt.setInt(7, event.getCapacity());
            stmt.setInt(8, event.getClubId());
            stmt.setInt(9, event.getCreatedBy());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // ===========================================
    // GET EVENT BY ID
    // ===========================================
    public Event getEventById(int eventId) {

        String sql = "SELECT * FROM events WHERE event_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapEvent(rs);
            }

        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    // ===========================================
    // GET ALL EVENTS
    // ===========================================
    public ArrayList<Event> getAllEvents() {

        ArrayList<Event> list = new ArrayList<>();

        String sql = "SELECT * FROM events ORDER BY event_date, start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapEvent(rs));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // ===========================================
    // GET EVENTS BY CLUB
    // ===========================================
    public ArrayList<Event> getEventsByClub(int clubId) {

        ArrayList<Event> list = new ArrayList<>();

        String sql = "SELECT * FROM events WHERE club_id=? ORDER BY event_date, start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapEvent(rs));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // ===========================================
    // DELETE EVENT BY ID
    // ===========================================
    public boolean deleteEvent(int eventId) {

        String sql = "DELETE FROM events WHERE event_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // ===========================================
    // DELETE ALL EVENTS FOR ONE CLUB
    // ===========================================
    public boolean deleteEventsByClub(int clubId) {

        String sql = "DELETE FROM events WHERE club_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clubId);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // ===========================================
    // BASIC CONFLICT CHECK (same time & date & venue)
    // ===========================================
    public boolean hasConflict(Event event) {

        String sql = """
            SELECT 1 FROM events
            WHERE venue = ?
            AND event_date = ?
            AND (
                (start_time BETWEEN ? AND ?)
                OR (end_time BETWEEN ? AND ?)
                OR (? BETWEEN start_time AND end_time)
            )
            LIMIT 1
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getVenue());
            stmt.setDate(2, event.getEventDate());
            stmt.setTimestamp(3, event.getStartTime());
            stmt.setTimestamp(4, event.getEndTime());
            stmt.setTimestamp(5, event.getStartTime());
            stmt.setTimestamp(6, event.getEndTime());
            stmt.setTimestamp(7, event.getStartTime());

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // ===========================================
    // MAP RESULTSET → EVENT OBJECT
    // ===========================================
    private Event mapEvent(ResultSet rs) throws Exception {

        return new Event(
                rs.getInt("event_id"),
                rs.getString("event_name"),
                rs.getString("description"),
                rs.getDate("event_date"),
                rs.getTimestamp("start_time"),
                rs.getTimestamp("end_time"),
                rs.getString("venue"),
                rs.getInt("capacity"),
                rs.getInt("club_id"),
                rs.getInt("created_by")
        );
    }

    public ArrayList<Event> getEventsRegisteredByStudent(int studentId) {
        ArrayList<Event> list = new ArrayList<>();
        String sql = """
        SELECT e.* 
        FROM events e
        JOIN event_registrations r ON e.event_id = r.event_id
        WHERE r.student_id=?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapEvent(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
