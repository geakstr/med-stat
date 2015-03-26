package ru.aspu.medstat.forms;

public class UserRegistrationForm {
    private String firstName = "";
    private String lastName = "";
    private String birthDateDay = "";
    private String birthDateMonth = "";
    private String birthDateYear = "";
    private String email = "";
    private String password = "";
    private String phone = "";

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getBirthDateDay() {
        return birthDateDay;
    }

    public void setBirthDateDay(String birthDateDay) {
        this.birthDateDay = birthDateDay;
    }

    public String getBirthDateMonth() {
        return birthDateMonth;
    }

    public void setBirthDateMonth(String birthDateMonth) {
        this.birthDateMonth = birthDateMonth;
    }

    public String getBirthDateYear() {
        return birthDateYear;
    }

    public void setBirthDateYear(String birthDateYear) {
        this.birthDateYear = birthDateYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone.trim();
    }
}