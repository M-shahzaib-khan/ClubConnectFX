package model;

public class ClubAdmin extends User {
    private int managedClubId;

    public ClubAdmin(int userId, String username, String password,
                     String name, String email, int managedClubId) {

        super(userId, username, password, name, email, "club_admin");
        this.managedClubId = managedClubId;
    }

    public int getManagedClubId() { return managedClubId; }
}
