package dao;

import model.Student;
import java.sql.*;
import java.util.ArrayList;

public class StudentDAO {

    // =======================================
    // GET ALL STUDENTS
    // =======================================
    public ArrayList<Student> getAllStudents() {

        ArrayList<Student> list = new ArrayList<>();

        String sql = """
            SELECT s.student_id, s.name, s.email, s.department, s.semester,
                   u.username, u.password
            FROM students s
            JOIN users u ON s.student_id = u.user_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getInt("semester")
                ));
            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    // =======================================
    // GET SINGLE STUDENT BY ID
    // =======================================
    public Student getStudentById(int studentId) {

        String sql = """
            SELECT s.student_id, s.name, s.email, s.department, s.semester,
                   u.username, u.password
            FROM students s
            JOIN users u ON s.student_id = u.user_id
            WHERE s.student_id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("student_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getInt("semester")
                );
            }

        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    // =======================================
    // CREATE STUDENT (requires user already created)
    // =======================================
    public boolean createStudent(Student s) {

        String sql = """
            INSERT INTO students (student_id, name, email, department, semester)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, s.getUserId());
            stmt.setString(2, s.getName());
            stmt.setString(3, s.getEmail());
            stmt.setString(4, s.getDepartment());
            stmt.setInt(5, s.getSemester());

            System.out.println(s.getUserId());
            return stmt.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // UPDATE STUDENT
    // =======================================
    public boolean updateStudent(Student s) {

        String sql = """
            UPDATE students
            SET name=?, email=?, department=?, semester=?
            WHERE student_id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getName());
            stmt.setString(2, s.getEmail());
            stmt.setString(3, s.getDepartment());
            stmt.setInt(4, s.getSemester());
            stmt.setInt(5, s.getUserId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    // =======================================
    // DELETE STUDENT
    // =======================================
    public boolean deleteStudent(int studentId) {

        String sql = "DELETE FROM students WHERE student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }
}
