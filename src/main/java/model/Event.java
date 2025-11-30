package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Event {

    private int eventId;
    private String eventName;
    private String description;
    private Date eventDate;
    private Timestamp startTime;
    private Timestamp endTime;
    private String venue;
    private int capacity;
    private int clubId;     // foreign key to clubs table
    private int createdBy;  // user_id of club admin who created the event

    public Event(int eventId, String eventName, String description,
                 Date eventDate, Timestamp startTime, Timestamp endTime,
                 String venue, int capacity, int clubId, int createdBy) {

        this.eventId = eventId;
        this.eventName = eventName;
        this.description = description;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.capacity = capacity;
        this.clubId = clubId;
        this.createdBy = createdBy;
    }

    // Getters
    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public String getDescription() { return description; }
    public Date getEventDate() { return eventDate; }
    public Timestamp getStartTime() { return startTime; }
    public Timestamp getEndTime() { return endTime; }
    public String getVenue() { return venue; }
    public int getCapacity() { return capacity; }
    public int getClubId() { return clubId; }
    public int getCreatedBy() { return createdBy; }

    @Override
    public String toString() {
        return eventName + " (" + eventDate + ")";
    }
}
