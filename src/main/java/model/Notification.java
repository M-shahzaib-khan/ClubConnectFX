package model;

import java.sql.Timestamp;

public class Notification {
    private int notificationId;
    private int userId;
    private String message;
    private Timestamp createdAt;
    private boolean isRead;

    public Notification(int notificationId, int userId, String message,
                        Timestamp createdAt, boolean isRead) {

        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    // Getters and Setters
    public int getNotificationId() { return notificationId; }
    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public Timestamp getCreatedAt() { return createdAt; }
    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }
}
