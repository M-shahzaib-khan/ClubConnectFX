package model;

import java.sql.Timestamp;

public class ClubJoinRequest {
    private int requestId;
    private int clubId;
    private int studentId;

    private String studentName;
    private String studentEmail;
    private String department;
    private int semester;

    private String status;
    private Timestamp requestedAt;

    public ClubJoinRequest(int requestId, int clubId, int studentId,
                           String studentName, String studentEmail, String department, int semester,
                           String status, Timestamp requestedAt) {

        this.requestId = requestId;
        this.clubId = clubId;
        this.studentId = studentId;

        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.department = department;
        this.semester = semester;

        this.status = status;
        this.requestedAt = requestedAt;
    }

    // Getters and Setters
    public int getRequestId() { return requestId; }
    public int getClubId() { return clubId; }
    public int getStudentId() { return studentId; }

    public String getStudentName() { return studentName; }
    public String getStudentEmail() { return studentEmail; }
    public String getDepartment() { return department; }
    public int getSemester() { return semester; }

    public String getStatus() { return status; }
    public Timestamp getRequestedAt() { return requestedAt; }

    public void setStatus(String status) { this.status = status; }
}
