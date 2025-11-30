package BL;

import dao.*;
import model.Club;
import model.Student;

import java.util.ArrayList;

public class ClubBL {

    private ClubDAO clubDAO = new ClubDAO();
    private ClubJoinRequestDAO requestDAO = new ClubJoinRequestDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();
    private StudentDAO studentDAO = new StudentDAO();

    // ==============================
    // LIST ALL CLUBS
    // ==============================
    public ArrayList<Club> getAllClubs() {
        return clubDAO.getAllClubs();
    }

    // ==============================
    // GET CLUB DETAILS
    // ==============================
    public Club getClubById(int clubId) {
        return clubDAO.getClubById(clubId);
    }

    // ==============================
    // GET MEMBERS OF A CLUB
    // ==============================
    public ArrayList<Student> getClubMembers(int clubId) {
        return clubDAO.getClubMembers(clubId);
    }

    // ==============================
    // SEND JOIN REQUEST
    // ==============================
    public boolean sendJoinRequest(int clubId, int studentId) {

        // already a member?
        if (clubDAO.isMember(clubId, studentId)) {
            return false;
        }

        // already requested?
        if (requestDAO.hasPendingRequest(clubId, studentId)) {
            return false;
        }

        boolean ok = requestDAO.sendRequest(clubId, studentId);

        if (ok) {
            notificationDAO.sendNotification(
                    studentId,
                    "Your join request for club ID " + clubId + " has been submitted."
            );
        }

        return ok;
    }

    // ==============================
    // GET PENDING REQUESTS FOR CLUB
    // ==============================
    public ArrayList<Student> getPendingRequests(int clubId) {
        return requestDAO.getPendingRequests(clubId);
    }

    // ==============================
    // APPROVE MEMBER REQUEST
    // ==============================
    public boolean approveMember(int clubId, int studentId) {

        boolean ok = requestDAO.approveRequest(clubId, studentId);

        if (ok) {
            notificationDAO.sendNotification(
                    studentId,
                    "Your membership request for club ID " + clubId + " was approved!"
            );
        }

        return ok;
    }

    // ==============================
    // REJECT MEMBER REQUEST
    // ==============================
    public boolean rejectMember(int clubId, int studentId) {

        boolean ok = requestDAO.rejectRequest(clubId, studentId);

        if (ok) {
            notificationDAO.sendNotification(
                    studentId,
                    "Your membership request for club ID " + clubId + " was rejected."
            );
        }

        return ok;
    }

    // ==============================
    // SEARCH CLUBS BY NAME
    // ==============================
    public ArrayList<Club> searchClubs(String keyword) {
        ArrayList<Club> clubs = clubDAO.getAllClubs();
        ArrayList<Club> filtered = new ArrayList<>();

        keyword = keyword.toLowerCase();

        for (Club c : clubs) {
            if (c.getClubName().toLowerCase().contains(keyword)) {
                filtered.add(c);
            }
        }

        return filtered;
    }
}
