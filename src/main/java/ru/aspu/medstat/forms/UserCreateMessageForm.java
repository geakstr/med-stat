package ru.aspu.medstat.forms;

public class UserCreateMessageForm extends Form {
	private long toUserId;
	private String header;
	private String message;
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getToUserId() {
		return toUserId;
	}
	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}
}
