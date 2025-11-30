package model;

public class Student extends User {

    private String department;
    private int semester;

    public Student(int userId, String username, String password,
                   String name, String email,
                   String department, int semester) {

        super(userId, username, password, name, email, "student");
        this.department = department;
        this.semester = semester;
    }

    public Student(int studentId, String name, String email, String department, int semester) {
        super(studentId, "", "", "student"); // username & password unknown
        this.name = name;
        this.email = email;
        this.department = department;
        this.semester = semester;
    }


    public String getDepartment() { return department; }
    public int getSemester() { return semester; }

    @Override
    public String toString() {
        return name + " (" + userId + ")";
    }
}
