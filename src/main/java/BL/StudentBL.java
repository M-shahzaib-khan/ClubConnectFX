package BL;

import dao.*;
import model.Event;
import model.Student;
import model.Notification;

import java.util.ArrayList;

public class StudentBL {

    private StudentDAO studentDAO = new StudentDAO();
    private EventDAO eventDAO = new EventDAO();
    private RegistrationDAO registrationDAO = new RegistrationDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();
    private ClubJoinRequestDAO requestDAO = new ClubJoinRequestDAO();

    // Load student profile
    public Student getStudentProfile(int studentId) {
        return studentDAO.getStudentById(studentId);
    }

    // Load all events available to student
    public ArrayList<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    // Register student for an event
    public String registerForEvent(Event event, int studentId) {

        int eventId = event.getEventId();

        // -------------------------------
        // 1. Already registered?
        // -------------------------------
        if (registrationDAO.isRegistered(eventId, studentId)) {
            return "already_registered";
        }

        // -------------------------------
        // 2. Check capacity
        // -------------------------------
        int currentCount = registrationDAO.getRegistrationCount(eventId);

        if (currentCount < event.getCapacity()) {

            boolean ok = registrationDAO.registerStudent(eventId, studentId);

            if (ok) {
                notificationDAO.sendNotification(
                        studentId,
                        "You have successfully registered for: " + event.getEventName()
                );
                return "registered";
            }

            return "error";
        }

        // -------------------------------
        // 3. Event FULL → add to waitlist
        // -------------------------------
        if (!registrationDAO.isWaitlisted(eventId, studentId)) {

            boolean added = registrationDAO.addToWaitlist(eventId, studentId);

            if (added) {
                notificationDAO.sendNotification(
                        studentId,
                        "Event is full. You were added to the waitlist for " + event.getEventName()
                );
                return "waitlisted";
            }

            return "already_waitlisted";
        }

        return "already_waitlisted";
    }

    // Cancel event registration
    public boolean cancelEventRegistration(int eventId, int studentId) {

        boolean removed = registrationDAO.cancelRegistration(eventId, studentId);

        if (removed) {
            // Promote first waitlisted student
            registrationDAO.promoteNextWaitlistedStudent(eventId);
        }

        return removed;
    }

    // Get notifications
    public ArrayList<Notification> getNotifications(int studentId) {
        return notificationDAO.getUnreadNotifications(studentId);
    }

    // Mark notifications as read
    public void clearNotifications(int studentId) {
        notificationDAO.markAllAsRead(studentId);
    }

    // Join a club → send join request
    public boolean sendJoinRequest(int clubId, int studentId) {
        return requestDAO.sendRequest(clubId, studentId);
    }

    public ArrayList<Event> getRegisteredEvents(int studentId) {
        return eventDAO.getEventsRegisteredByStudent(studentId);
    }


}
