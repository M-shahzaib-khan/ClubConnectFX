package model;

public class User {

    protected int userId;
    protected String username;
    protected String password;
    protected String role;

    // Optional extra fields (used by Student & ClubAdmin)
    protected String name;
    protected String email;

    // Base constructor for normal users
    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Full constructor for Student & ClubAdmin
    public User(int userId, String username, String password,
                String name, String email, String role) {

        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public String getName() { return name; }
    public String getEmail() { return email; }
}
