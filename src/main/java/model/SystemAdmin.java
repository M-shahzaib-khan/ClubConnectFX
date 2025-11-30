package model;

public class SystemAdmin extends User {

    private String name;
    private String email;

    public SystemAdmin(int userId, String username, String password,
                       String name, String email) {

        super(userId, username, password, "system_admin");
        this.name = name;
        this.email = email;
    }


}
