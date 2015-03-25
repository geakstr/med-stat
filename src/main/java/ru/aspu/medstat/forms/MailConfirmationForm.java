package ru.aspu.medstat.forms;

public class MailConfirmationForm {
    private String password = "";
    private String emailToken = "";
    
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailToken() {
		return emailToken;
	}
	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}
}
