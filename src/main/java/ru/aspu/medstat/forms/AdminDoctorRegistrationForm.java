package ru.aspu.medstat.forms;

public class AdminDoctorRegistrationForm extends Form {
    private String email = "";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }
}
