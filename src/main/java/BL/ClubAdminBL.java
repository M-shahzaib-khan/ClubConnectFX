package BL;

import dao.ClubDAO;
import dao.ClubJoinRequestDAO;
import dao.EventDAO;
import dao.RegistrationDAO;
import dao.NotificationDAO;

import model.Club;
import model.Student;
import model.Event;

import java.util.ArrayList;

public class ClubAdminBL {

    private ClubDAO clubDAO = new ClubDAO();
    private ClubJoinRequestDAO requestDAO = new ClubJoinRequestDAO();
    private EventDAO eventDAO = new EventDAO();
    private RegistrationDAO registrationDAO = new RegistrationDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    // ------------------------------
    // GET CLUB ADMIN'S CLUB
    // ------------------------------
    public Club getManagedClub(int adminUserId) {
        return clubDAO.getClubByAdmin(adminUserId);
    }

    // ------------------------------
    // MEMBER REQUEST OPERATIONS
    // ------------------------------

    // Now returns list of Students (pending)
    public ArrayList<Student> getPendingRequests(int clubId) {
        return requestDAO.getPendingRequests(clubId);
    }

    // Approve request by club + student (no requestId)
    public boolean approveRequest(int clubId, int studentId) {
        boolean success = requestDAO.approveRequest(clubId, studentId);

        if (success) {
            // Notify student
            notificationDAO.sendNotification(
                    studentId,
                    "Your membership request for club ID " + clubId + " has been approved!"
            );
        }

        return success;
    }

    // Reject request by club + student
    public boolean rejectRequest(int clubId, int studentId) {
        boolean success = requestDAO.rejectRequest(clubId, studentId);

        if (success) {
            notificationDAO.sendNotification(
                    studentId,
                    "Your membership request for club ID " + clubId + " was rejected."
            );
        }

        return success;
    }

    // ------------------------------
    // CLUB MEMBERS
    // ------------------------------
    public ArrayList<Student> getClubMembers(int clubId) {
        return clubDAO.getClubMembers(clubId);
    }

    // ------------------------------
    // EVENT MANAGEMENT
    // ------------------------------

    public ArrayList<Event> getEventsByClub(int clubId) {
        return eventDAO.getEventsByClub(clubId);
    }

    public boolean createEvent(Event event) {
        // Check for conflict
        if (eventDAO.hasConflict(event)) {
            return false;
        }
        return eventDAO.createEvent(event);
    }

    public boolean deleteEvent(int eventId) {
        // Optional: clear registrations & waitlist first
        registrationDAO.deleteAllRegistrationsForEvent(eventId);
        registrationDAO.deleteAllWaitlistForEvent(eventId);
        return eventDAO.deleteEvent(eventId);
    }

    // Get registration count for event
    public int getRegistrationCount(int eventId) {
        return registrationDAO.getRegistrationCount(eventId);
    }
}
