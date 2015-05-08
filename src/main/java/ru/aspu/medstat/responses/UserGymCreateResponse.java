package ru.aspu.medstat.responses;

public class UserGymCreateResponse extends BaseResponse {
	public long lastId;
	
	public UserGymCreateResponse(long lastId) {
		this.type = "success";
		this.lastId = lastId;
	}
}
