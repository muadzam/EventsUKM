package com.example.eventukm.Objects;

public class UserInfos {
    private String name;
    private String matrix;
    private String college;
    private String faculty;
    private String password;
    private String email;
    private String phone;
    private String role;  // New field for user role

    // Default constructor required for Firebase deserialization
    public UserInfos() {
    }

    // Constructor to initialize all fields
    public UserInfos(String name, String matrix, String college, String faculty, String password, String email, String phone, String role) {
        this.name = name;
        this.matrix = matrix;
        this.college = college;
        this.faculty = faculty;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    // Getters and setters for the fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
