package BL;

import dao.EventDAO;
import dao.NotificationDAO;
import dao.RegistrationDAO;
import model.Event;

import java.util.ArrayList;

public class EventBL {

    private EventDAO eventDAO = new EventDAO();
    private RegistrationDAO regDAO = new RegistrationDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    // ==============================
    // GET ALL EVENTS
    // ==============================
    public ArrayList<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    // ==============================
    // GET EVENTS BY CLUB
    // ==============================
    public ArrayList<Event> getEventsByClub(int clubId) {
        return eventDAO.getEventsByClub(clubId);
    }

    // ==============================
    // SEARCH EVENTS (name or venue)
    // ==============================
    public ArrayList<Event> searchEvents(String keyword) {
        keyword = keyword.toLowerCase();
        ArrayList<Event> all = eventDAO.getAllEvents();
        ArrayList<Event> filtered = new ArrayList<>();

        for (Event e : all) {
            if (e.getEventName().toLowerCase().contains(keyword) ||
                    e.getVenue().toLowerCase().contains(keyword)) {

                filtered.add(e);
            }
        }

        return filtered;
    }

    // ==============================
    // REGISTER FOR EVENT
    // ==============================
    public String registerForEvent(int studentId, int eventId) {

        Event event = eventDAO.getEventById(eventId);
        if (event == null) return "EVENT_NOT_FOUND";

        // Already registered?
        if (regDAO.isRegistered(eventId, studentId)) {
            return "ALREADY_REGISTERED";
        }

        // Registered count
        int count = regDAO.getRegistrationCount(eventId);

        if (count < event.getCapacity()) {
            // register normally
            boolean ok = regDAO.registerStudent(eventId, studentId);

            if (ok) {
                notificationDAO.sendNotification(
                        studentId,
                        "You have been registered for event: " + event.getEventName()
                );
                return "REGISTERED";
            }

            return "ERROR";
        }

        // Capacity full → add to waitlist
        boolean wl = regDAO.addToWaitlist(eventId, studentId);

        if (wl) {
            notificationDAO.sendNotification(
                    studentId,
                    "The event '" + event.getEventName() + "' is full. You are added to the waitlist."
            );
            return "WAITLISTED";
        }

        return "ALREADY_WAITLISTED";
    }

    // ==============================
    // CREATE EVENT
    // ==============================
    public boolean createEvent(Event event) {

        // Optional: conflict detection
        if (eventDAO.hasConflict(event)) {
            return false; // UI can show "conflict"
        }

        return eventDAO.createEvent(event);
    }

    // ==============================
    // DELETE EVENT (System Admin)
    // ==============================
    public boolean deleteEvent(int eventId) {
        regDAO.deleteAllRegistrationsForEvent(eventId);
        regDAO.deleteAllWaitlistForEvent(eventId);
        return eventDAO.deleteEvent(eventId);
    }

    // ==============================
    // PROMOTE FROM WAITLIST (when someone cancels)
    // ==============================
    public void promoteWaitlistedStudent(int eventId) {
        regDAO.promoteNextWaitlistedStudent(eventId);
    }
}
