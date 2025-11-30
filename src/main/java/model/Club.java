package model;

public class Club {

    private int clubId;
    private String clubName;
    private String description;
    private int createdBy; // user_id of the admin who created the club

    public Club(int clubId, String clubName, String description, int createdBy) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.description = description;
        this.createdBy = createdBy;
    }

    public int getClubId() {
        return clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public String getDescription() {
        return description;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    @Override
    public String toString() {
        return clubName;
    }
}
