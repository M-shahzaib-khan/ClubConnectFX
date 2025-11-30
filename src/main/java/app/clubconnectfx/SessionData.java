package app.clubconnectfx;

import model.Student;
import model.ClubAdmin;
import model.SystemAdmin;

public class SessionData {

    public static Student currentStudent = null;
    public static ClubAdmin currentClubAdmin = null;
    public static SystemAdmin currentSystemAdmin = null;

    public static void clear() {
        currentStudent = null;
        currentClubAdmin = null;
        currentSystemAdmin = null;
    }
}
