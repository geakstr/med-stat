package ru.aspu.medstat.forms;

public class UserRegistrationForm extends Form {
    protected String firstName = "";
    protected String lastName = "";
    protected String birthDateDay = "";
    protected String birthDateMonth = "";
    protected String birthDateYear = "";
    protected String email = "";
    protected String password = "";
    protected String phone = "";
    protected String emailToken = "";

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

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
        this.birthDateDay = birthDateDay.trim();
    }

    public String getBirthDateMonth() {
        return birthDateMonth;
    }

    public void setBirthDateMonth(String birthDateMonth) {
        this.birthDateMonth = birthDateMonth.trim();
    }

    public String getBirthDateYear() {
        return birthDateYear;
    }

    public void setBirthDateYear(String birthDateYear) {
        this.birthDateYear = birthDateYear.trim();
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

    public String getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(String emailToken) {
        this.emailToken = emailToken;
    }
}