package ru.aspu.medstat.forms;

public class DoctorAddGymToPacient extends Form {
	private long userId;
	private long gymId;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getGymId() {
		return gymId;
	}
	public void setGymId(long gymId) {
		this.gymId = gymId;
	}
}
